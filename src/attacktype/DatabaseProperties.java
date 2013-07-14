/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attacktype;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sijin
 */
public class DatabaseProperties extends Properties {

    private Timestamp time;
    private double responseTime;
    private int dataSize;
    private long timeInterval;
    private String username;
    private String sql;

    public static List<DatabaseProperties> convertDatabaseTable(ResultSet resultSet) {

        List<DatabaseProperties> dpl = new ArrayList<DatabaseProperties>();
        try {

            int count = 0;
            Timestamp pre = null;
            while (resultSet.next()) {

                if (count == 0 && !resultSet.getString("username").startsWith("root[root]")) {
                    pre = resultSet.getTimestamp("execution_time");
                    count = 1;
                }

                if (count == 1) {
                    DatabaseProperties dp = new DatabaseProperties();
                    dp.setTime(resultSet.getTimestamp("execution_time"));
                    // System.out.println(resultSet.getTimestamp("execution_time"));
                    dp.setDataSize(resultSet.getInt("rows_sent"));
                    String query_time = resultSet.getString("query_time");
                    String[] timearray = query_time.split(":");
            
                    double rt = 60 * 60 * Integer.parseInt(timearray[0]) + 60 * Integer.parseInt(timearray[1]) + Integer.parseInt(timearray[2]);

                  //  System.out.println(rt);
                    
                    dp.setResponseTime(rt);
                    dp.setUsername(resultSet.getString("username"));
                    Timestamp now = resultSet.getTimestamp("execution_time");
                    dp.setTimeInterval(now.getTime() - pre.getTime());
                    pre = now;
                    dp.setSql(resultSet.getString("sql_text"));
            
                    dpl.add(dp);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseProperties.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dpl;

    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }




    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }
}
