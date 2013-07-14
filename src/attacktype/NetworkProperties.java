/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attacktype;

import communication.AccessDatabase;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Sijin
 */
public class NetworkProperties extends Properties {



    private Timestamp time;
    private String src;
    private String des;
    private String uri;
    private int packet;
    private long timeInterval;

    public int getPacket() {
        return packet;
    }

    public void setPacket(int packet) {
        this.packet = packet;
    }


    public static List<NetworkProperties> convertNetworkTable(ResultSet resultSet) {
        List<NetworkProperties> dpl = new ArrayList<NetworkProperties>();
        
        try {

            int count = 0;
            Timestamp pre = null;

            while (resultSet.next()) {

           

                if (count == 0) {

                    
                    pre = resultSet.getTimestamp("time");
                    count = 1;
                }

                if (count == 1) {
                    NetworkProperties dp = new NetworkProperties();

                    dp.setTime(resultSet.getTimestamp("time"));
                    dp.setSrc(resultSet.getString("src"));
                    dp.setDes(resultSet.getString("des"));
                    dp.setUri(resultSet.getString("uri"));
                    dp.setPacket(resultSet.getInt("packet"));
                                        
                    Timestamp now = resultSet.getTimestamp("time");
                    dp.setTimeInterval(now.getTime() - pre.getTime());
                    pre = now;
                    dpl.add(dp);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseProperties.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dpl;
    }

    private static NetworkProperties parse(String strLine) {

        NetworkProperties np = new NetworkProperties();

        String[] strArr = strLine.split(",");

        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(strArr[0] + strArr[1]);
        while (m.find()) {
          
            String[] newStrArr = m.group(1).split("[ ]+");

            np.setTime(convertTimestamp(newStrArr[2], newStrArr[0], newStrArr[1], newStrArr[3]));


        }

        p = Pattern.compile("\"([^\"]*)\"");
        m = p.matcher(strArr[2]);
        while (m.find()) {

            np.setSrc(m.group(1));
        }


        

        p = Pattern.compile("\"([^\"]*)\"");
        m = p.matcher(strArr[3]);
        while (m.find()) {
          
            np.setDes(m.group(1));
        }



        p = Pattern.compile("\"([^\"]*)\"");
        m = p.matcher(strArr[4]);
        while (m.find()) {
            
            np.setUri(m.group(1));
        }

        p = Pattern.compile("\"([^\"]*)\"");
        m = p.matcher(strArr[5]);
        while (m.find()) {

            np.setPacket(Integer.parseInt(m.group(1)));
            System.out.println("packet: "+ np.getPacket());
        }



        return np;
    }

    private static Timestamp convertTimestamp(String year, String month, String day, String time) {

        time = time.substring(0, 12);
        DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss.SSS");
        Date date = null;
        try {
            date = dateFormat.parse(day + "/" + month + "/" + year + " " + time);
        } catch (ParseException ex) {
            Logger.getLogger(NetworkProperties.class.getName()).log(Level.SEVERE, null, ex);
        }

        long timestamp = date.getTime();

        return new Timestamp(timestamp);
    }

    public static List<NetworkProperties> convertNetworkTable(String path) throws IOException {

        List<NetworkProperties> dpl = new ArrayList<NetworkProperties>();

        try {

            FileInputStream fstream = new FileInputStream(path);

            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null) {

                NetworkProperties np = null;
          
                np = parse(strLine);

                dpl.add(np);

            }

            in.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }



        return dpl;
    }

    public static void main(String[] args) throws Exception {

        String name = "/NetworkSample/NetworkExample?term=A";
        System.out.println(name.substring(0, 35));
        System.out.println(name.substring(35));


    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    


    public String getDes() {
        return des;


    }

    public void setDes(String des) {
        this.des = des;


    }

    public String getSrc() {
        return src;


    }

    public void setSrc(String src) {
        this.src = src;


    }

    public Timestamp getTime() {
        return time;


    }

    public void setTime(Timestamp time) {
        this.time = time;


    }

    public String getUri() {
        return uri;


    }

    public void setUri(String uri) {
        this.uri = uri;

    }
}
