/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import communication.AccessDatabase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import monitor.DatabaseMonitor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MySQLAccess {

    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private String ip = null;
    private String username = null;
    private String password = null;
    public static Timestamp pre = null;

    public MySQLAccess() {
        //select * from mysql.slow_log;
        ip = "146.169.35.102";
        username = "root";
        password = "21vianet";

    }

    //interval in seconds
    public String readDatabase1() throws Exception {

        String json = "";

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/mysql?"
                    + "user=" + username + "&password=" + password);

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();

            Date date = new java.util.Date();
            Timestamp now = new Timestamp(date.getTime());

            if (pre == null) {
                pre = now;
            }

            // Result set get the result of the SQL query
            resultSet = statement.executeQuery("select * from mysql.slow_log where start_time between '" + pre + "' and '" + now.toString() + "'");
            pre = now;
            //writeResultSet(resultSet);
            json = writeToJson(resultSet);

        } catch (Exception e) {
            throw e;
        } finally {
            close();
            return json;
        }

    }

    //interval in seconds
    public String readDatabase(Timestamp preT) throws Exception {

        String json = "";

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/mysql?"
                    + "user=" + username + "&password=" + password);

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
//            Date date = new java.util.Date();
//            Timestamp now = new Timestamp(date.getTime());

            // Result set get the result of the SQL query
         //   System.out.println("select * from mysql.slow_log where start_time between '" + preT.toString() + "' and now()");
            resultSet = statement.executeQuery("select * from mysql.slow_log where start_time between '" + preT.toString() + "' and now()");
        //    System.out.println("select * from mysql.slow_log where start_time between '" + preT.toString() + "' and now()");
            
          //  writeResultSet(resultSet);
            json = writeToJson(resultSet);

        } catch (Exception e) {
            System.out.println(e.toString());
            throw e;
        } finally {
            close();
            return json;
        }

    }

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            Timestamp start_time = resultSet.getTimestamp("start_time");
            String user_host = resultSet.getString("user_host");
            String query_time = resultSet.getString("query_time");
            int rows_sent = resultSet.getInt("rows_sent");
            String sql_text = resultSet.getString("sql_text");
            System.out.println("start_time: " + start_time);
            System.out.println("user_host: " + user_host);
            System.out.println("query_time: " + query_time);
            System.out.println("rows_sent: " + rows_sent);
            System.out.println("sql_text: " + sql_text);
            //convert to json
        }
    }

    private String writeToJson(ResultSet resultSet) throws SQLException {

        String json = null;

        JSONObject jobj = new JSONObject();

        jobj.put("type", "mysql");

        jobj.put("detail", writeDetailInJson(resultSet));

        json = jobj.toString();

        //   System.out.println(json);

        return json;
    }
    // You need to close the resultSet

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) throws Exception {

            AccessDatabase ad = new AccessDatabase();
            Timestamp pre = ad.getLattestTime();
            MySQLAccess ma = new MySQLAccess();
            ma.readDatabase(pre);


    }

    private String writeDetailInJson(ResultSet resultSet) throws SQLException {



        String json = "";

        JSONArray array = new JSONArray();

        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            Timestamp start_time = resultSet.getTimestamp("start_time");
            String user_host = resultSet.getString("user_host");
            String query_time = resultSet.getString("query_time");
            int rows_sent = resultSet.getInt("rows_sent");
            String sql_text = resultSet.getString("sql_text").toLowerCase();
            // System.out.println(sql_text);
            //  System.out.println(query_time);
            //sql_text.matches("(?i)select.*");
            if (sql_text.startsWith("select") && !user_host.startsWith("root[root]")) {

                JSONObject jobj = new JSONObject();

                jobj.put("execution_time", start_time);
                jobj.put("username", user_host);
                jobj.put("query_time", query_time);
                jobj.put("query_size", rows_sent);
                jobj.put("sql", sql_text);

                if (DatabaseMonitor.localdb) {
                    AccessDatabase dao = new AccessDatabase();
                    try {
                        dao.writeDatabaseTable(start_time, user_host, query_time, rows_sent, sql_text);
                    } catch (Exception ex) {
                        Logger.getLogger(MySQLAccess.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                array.add(jobj);
            }
            //       System.out.println(jobj.toString());

        }

        json = array.toString();

        return json;

    }
}
