/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import attacktype.CacheProperties;
import attacktype.DatabaseProperties;
import attacktype.NetworkProperties;
import attacktype.ResourceProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sijin
 */
public class AccessDatabase {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private String ip = null;
    private String username = null;
    private String password = null;

    public AccessDatabase() {
        //select * from mysql.slow_log;
        ip = "146.169.35.22";
        username = "l8b6u2gd";
        password = "l8b6u2gd";

    }

    public void writeCacheTable(Timestamp time, int duration) throws Exception {

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/Monitor?"
                    + "user=" + username + "&password=" + password);

            String id = UUID.randomUUID().toString();
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect.prepareStatement("insert into Monitor.invm_cache values (?, ?, ?)");
            // "myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
            // Parameters start with 1
            preparedStatement.setString(1, id);
            preparedStatement.setTimestamp(2, time);
            preparedStatement.setInt(3, duration);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    public void writeNetoworkTable(Timestamp time, String src, String dec, String uri, int packet) throws Exception {

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/Monitor?"
                    + "user=" + username + "&password=" + password);

            // String id = UUID.randomUUID().toString();
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect.prepareStatement("insert into Monitor.invm_network values (?, ?, ?, ?, ?)");
            // "myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
            // Parameters start with 1

            preparedStatement.setTimestamp(1, time);
            preparedStatement.setString(2, src);
            preparedStatement.setString(3, dec);
            preparedStatement.setString(4, uri);
            preparedStatement.setInt(5, packet);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
        } finally {
            close();
        }

    }

    public List<NetworkProperties> readNetworkTable(Timestamp begin, Timestamp end, String source) throws Exception {

        List<NetworkProperties> dalist = new ArrayList<NetworkProperties>();

        try {

            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/Monitor?" + "user=" + username + "&password=" + password);

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query

            String sql = "select * from Monitor.invm_network where time between '" + begin + "' and '" + end + "' and src = '" + source + "' and uri LIKE '%/NetworkSample/NetworkExample?term=%' order by time ASC";
            // System.out.println(sql);
            resultSet = statement.executeQuery(sql);

            dalist = NetworkProperties.convertNetworkTable(resultSet);

        } catch (Exception e) {
            throw e;
        } finally {
            close();
            return dalist;
        }

    }

    public void writeDatabaseTable(Timestamp start_time, String user_host, String query_time, int rows_sent, String sql_text) throws Exception {

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/Monitor?"
                    + "user=" + username + "&password=" + password);

            String id = UUID.randomUUID().toString();
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect.prepareStatement("insert into Monitor.invm_database values (?, ?, ?, ? , ?, ?)");
            // "myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
            // Parameters start with 1
            preparedStatement.setString(1, id);
            preparedStatement.setTimestamp(2, start_time);
            preparedStatement.setString(3, user_host);
            preparedStatement.setString(4, query_time);
            preparedStatement.setInt(5, rows_sent);
            preparedStatement.setString(6, sql_text);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    public List<DatabaseProperties> readDatabaseTable(Timestamp begin, Timestamp end, String dbuser) throws Exception {

        List<DatabaseProperties> dalist = new ArrayList<DatabaseProperties>();

        try {

            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/Monitor?" + "user=" + username + "&password=" + password);

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query

            String sql = "select * from Monitor.invm_database where execution_time between '" + begin + "' and '" + end + "' and username LIKE '%" + dbuser + "%' order by execution_time ASC";
            // System.out.println(sql);
            resultSet = statement.executeQuery(sql);

            dalist = DatabaseProperties.convertDatabaseTable(resultSet);

        } catch (Exception e) {
            throw e;
        } finally {
            close();
            return dalist;
        }

    }

    public List<ResourceProperties> readResourceTable(Timestamp begin, Timestamp end) throws Exception {

        List<ResourceProperties> dalist = new ArrayList<ResourceProperties>();


        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/Monitor?"
                    + "user=" + username + "&password=" + password);

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query


            resultSet = statement.executeQuery("select * from Monitor.invm_resource where execution_time between '" + begin + "' and '" + end + "' order by execution_time ASC");

            dalist = ResourceProperties.convertResourceTable(resultSet);



        } catch (Exception e) {
            throw e;
        } finally {
            close();
            return dalist;
        }

    }

    public void writeResourceTable(double cpu, long used_memory, long total_memory, double mysql_cpu, long mysql_mem) throws Exception {

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/Monitor?"
                    + "user=" + username + "&password=" + password);

            String id = UUID.randomUUID().toString();
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect.prepareStatement("insert into Monitor.invm_resource values (?, ?, ?, ? , ?, ?, ?)");
            // "myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
            // Parameters start with 1
            preparedStatement.setString(1, id);
            preparedStatement.setDouble(2, cpu);
            preparedStatement.setLong(3, used_memory/1024);
            preparedStatement.setLong(4, total_memory/1024);
            preparedStatement.setDouble(5, mysql_cpu);
            preparedStatement.setLong(6, mysql_mem/1024);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            preparedStatement.setTimestamp(7, timestamp);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

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
        System.out.println(ad.getLattestTime().toString());

//
//        }
    }

    public List<CacheProperties> readCacheTable(Timestamp begin, Timestamp end) {
        List<CacheProperties> dalist = new ArrayList<CacheProperties>();

        try {

            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/Monitor?" + "user=" + username + "&password=" + password);

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query

            String sql = "select * from Monitor.invm_cache where time between '" + begin + "' and '" + end + "' order by time ASC";
            // System.out.println(sql);
            resultSet = statement.executeQuery(sql);

            dalist = CacheProperties.convertNetworkTable(resultSet);

        } catch (Exception e) {
            throw e;
        } finally {
            close();
            return dalist;
        }
    }

    public Timestamp getLattestTime() {
        Timestamp time = Timestamp.valueOf("2013-04-17 15:19:03");

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/Monitor?" + "user=" + username + "&password=" + password);

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query

            String sql = "select max(execution_time) from  `Monitor`.`invm_database`";
            // System.out.println(sql);
            resultSet = statement.executeQuery(sql);

            time = convertToTimeStamp(resultSet);

        } catch (Exception e) {
            throw e;
        } finally {
            close();
            return time;
        }
    }

    private Timestamp convertToTimeStamp(ResultSet resultSet) {


        Timestamp time = null;
        try {

            while (resultSet.next()) {

                String timestring = resultSet.getString("max(execution_time)");

                time = Timestamp.valueOf(timestring);
                time = new Timestamp(time.getTime() + 1000);

            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
        return time;

    }

    public int checkPacket(String letter) {
        int packet = -1;

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/Monitor?" + "user=" + username + "&password=" + password);

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query

            //     System.out.println("select packet from `Monitor`.`network_typing` where  idnetwork_typing = '" + letter + "'");
            String sql = "select packet from `Monitor`.`network_typing` where  idnetwork_typing = '" + letter + "'";
         //       System.out.println(sql);
            resultSet = statement.executeQuery(sql);
            //     System.out.println(packet);
            packet = convertToPacket(resultSet);

        } catch (Exception e) {
//            System.out.println(e.toString());
//            throw e;
        } finally {
            close();
            return packet;
        }
    }

    private int convertToPacket(ResultSet resultSet) {
        int time = -1;
        try {

            while (resultSet.next()) {

                time = resultSet.getInt("packet");
                break;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseProperties.class.getName()).log(Level.SEVERE, null, ex);
        }


        return time;
    }

    public void writeNetworkTyping(String letter, int packet) throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/Monitor?"
                    + "user=" + username + "&password=" + password);

            String id = UUID.randomUUID().toString();
            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect.prepareStatement("insert into Monitor.network_typing values (?, ?)");
            // "myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
            // Parameters start with 1
            preparedStatement.setString(1, letter);
            preparedStatement.setInt(2, packet);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
        } finally {
            close();
        }

    }

    public int getPacket(String letter) {
        int packet = -1;

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/Monitor?" + "user=" + username + "&password=" + password);

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query

            //     System.out.println("select packet from `Monitor`.`network_typing` where  idnetwork_typing = '" + letter + "'");

            String url = "/NetworkSample/NetworkExample?term=" + letter;
            String sql = "select packet from `Monitor`.`invm_network` where  uri = '" + url + "' order by time DESC";
            //      System.out.println(sql);
            resultSet = statement.executeQuery(sql);
            //          System.out.println(packet);
            packet = convertToloadPacket(resultSet);

        } catch (Exception e) {
            //  System.out.println(e.toString());
            // throw e;
        } finally {
            close();
            return packet;
        }
    }

    private int convertToloadPacket(ResultSet resultSet) {

        int packet = -1;

        try {

            while (resultSet.next()) {

                packet = resultSet.getInt("packet");
                break;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseProperties.class.getName()).log(Level.SEVERE, null, ex);
        }

        return packet;
    }

    public String getPacketString(String source) {

        String json = null;

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/Monitor?" + "user=" + username + "&password=" + password);

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query

            //     System.out.println("select packet from `Monitor`.`network_typing` where  idnetwork_typing = '" + letter + "'");

            //       String url = "/NetworkSample/NetworkExample?term=" + letter;
            String sql = "select * from `Monitor`.`invm_network` where  src = '" + source + "' and uri LIKE '%/NetworkSample/NetworkExample?term=%' order by time ASC";
            //    System.out.println(sql);
            resultSet = statement.executeQuery(sql);
            //     System.out.println(packet);
            json = convertTolistPacket(resultSet);

        } catch (Exception e) {
            System.out.println(e.toString());
            throw e;
        } finally {
            close();
            return json;
        }


    }

    private String convertTolistPacket(ResultSet resultSet) {

        String json = "";


//        List<Timestamp> time = new ArrayList<Timestamp>();
//        List<Integer> alist = new ArrayList<Integer>();
//        List<String> iplist = new ArrayList<String>();

        try {

            while (resultSet.next()) {

                json += resultSet.getTimestamp("time");
                json += ", ";
                json += resultSet.getInt("packet");
                json += ", ";
                json += resultSet.getString("src");
                json += "\n";

//                time.add(resultSet.getTimestamp("time"));
//                alist.add(resultSet.getInt("packet"));
//                iplist.add(resultSet.getString("src"));

            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseProperties.class.getName()).log(Level.SEVERE, null, ex);
        }


//        for (int i = 0; i < alist.size(); i++) {
//
//            json += alist.get(i);
//            if (i != alist.size() - 1) {
//                json += ",";
//            }
//
//        }


        json += "";
        return json;
    }

    public List<String> getSourcelist() {
        List<String> dalist = new ArrayList<String>();

        try {

            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://" + ip + "/Monitor?" + "user=" + username + "&password=" + password);

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query

            String sql = "select distinct src from `Monitor`.`invm_network`";
            // System.out.println(sql);
            resultSet = statement.executeQuery(sql);

            dalist = convertSourceList(resultSet);

        } catch (Exception e) {
            throw e;
        } finally {
            close();
            return dalist;
        }
    }

    private List<String> convertSourceList(ResultSet resultSet) {
        List<String> packet = new ArrayList<String>();

        try {

            while (resultSet.next()) {

                packet.add(resultSet.getString("src"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseProperties.class.getName()).log(Level.SEVERE, null, ex);
        }

        return packet;
    }
}
