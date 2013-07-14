/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attacktype;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sijin
 */
public class CacheProperties extends Properties {

    public static List<CacheProperties> convertNetworkTable(ResultSet resultSet) {
        List<CacheProperties> dpl = new ArrayList<CacheProperties>();

        try {

            while (resultSet.next()) {

                CacheProperties dp = new CacheProperties();

                dp.setTime(resultSet.getTimestamp("time"));
                dp.setDuration(resultSet.getInt("duration"));

                dpl.add(dp);

            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseProperties.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dpl;
    }
    
    private Timestamp time;
    private int duration;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
