/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitor;

import communication.AccessDatabase;
import database.MySQLAccess;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONObject;
import resource.Resource;

/**
 *
 * @author Sijin
 */
public class DatabaseMonitor extends Monitor {

    public static boolean localdb = false;

    @Override
    public void start(String format, String interval) {
        int time = Integer.parseInt(interval);

        while (true) {
            try {

                if (format.equalsIgnoreCase("screen")) {
                    System.out.println(dataToJson(time));
                } else if (format.equalsIgnoreCase("file")) {
                    System.out.println("Under development");
                } else if (format.equalsIgnoreCase("localdb")) {
                    localdb = true;
                    System.out.println(dataToJson(time));

                } else if (format.equalsIgnoreCase("globaldb")) {
                    System.out.println("Under development");
                } else {
                    System.out.println("invalid output type");
                }

                Thread.sleep(time * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(DatabaseMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }



    }

    private String dataToJson(int time) {

        String json = "";
        JSONObject jobj = new JSONObject();

        MySQLAccess dao = new MySQLAccess();
        try {
            AccessDatabase ad = new AccessDatabase();
            Timestamp pre = ad.getLattestTime();

            jobj.put("database", dao.readDatabase(pre));
        } catch (Exception ex) {
            Logger.getLogger(DatabaseMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }

        Resource rs = new Resource();
        jobj.put("resource", rs.getResource());

        json = jobj.toString();

        return json;

    }



}
