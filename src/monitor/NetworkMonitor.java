/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitor;

import attacktype.NetworkProperties;
import communication.AccessDatabase;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sijin
 */
public class NetworkMonitor extends Monitor {

    public static boolean localdb = false;
    public static int event = 0;

    public NetworkMonitor() {
    }

    @Override
    public void start(String format, String interval) {
//        try {
        double time = Double.parseDouble(interval);

//            Runtime r = Runtime.getRuntime();
//            Process p = r.exec("/monitor/newnetwork/tshark.sh");

        while (true) {
            try {
                if (format.equalsIgnoreCase("screen")) {
                    //  System.out.println(dataToJson(time));
                } else if (format.equalsIgnoreCase("file")) {
                    System.out.println("Under development");
                } else if (format.equalsIgnoreCase("localdb")) {
                    localdb = true;
                    try {
                        // System.out.println(dataToJson(time));
                        run1(time);
                    } catch (IOException ex) {
                        Logger.getLogger(NetworkMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (format.equalsIgnoreCase("globaldb")) {
                    System.out.println("Under development");
                } else {
                    System.out.println("invalid output type");
                }
                Thread.sleep((int) time * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(DatabaseMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        } catch (IOException ex) {
//            Logger.getLogger(NetworkMonitor.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void run1(double time) throws IOException, InterruptedException {

        NetworkProperties np = new NetworkProperties();

        Runtime r = Runtime.getRuntime();
        Process p = r.exec("/monitor/newnetwork/csv1.sh");


        if (p.waitFor() == 0) {
            // Directory path here
            String path = "/monitor/newnetwork/csv/";

            String files;
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();

            for (int j = 0; j < listOfFiles.length; j++) {

                if (listOfFiles[j].isFile()) {
                    files = listOfFiles[j].getName();
                    if (files.endsWith(".csv") || files.endsWith(".CSV")) {
                        // System.out.println(files);

                        List<NetworkProperties> listnp = np.convertNetworkTable("/monitor/newnetwork/csv/" + files);

                        AccessDatabase ad = new AccessDatabase();
                        for (int i = 0; i < listnp.size(); i++) {
                            try {
                                //           System.out.println(listnp.get(i).getUri());

                                //    if (listnp.get(i).getDes().equalsIgnoreCase("146.169.35.102") && listnp.get(i).getUri()!=null) {

                                if (listnp.get(i).getUri() != null) {
                                    int index = findPair(i, listnp);
                                    if (index != -1) {

                                        if(listnp.get(i).getUri().length() > 0){
                                            listnp.get(i).setUri(listnp.get(i).getUri().replace("%20", " "));
                                        }

                                        ad.writeNetoworkTable(listnp.get(i).getTime(), listnp.get(i).getSrc(), listnp.get(i).getDes(), listnp.get(i).getUri(), listnp.get(index).getPacket());

                                        if (listnp.get(i).getUri().startsWith("/NetworkSample/NetworkExample?term=")) {
                                            ad.writeNetworkTyping(listnp.get(i).getUri().substring(35).toLowerCase(), listnp.get(index).getPacket());
                                        }

                                    }

                                }


                                //  System.out.println(listnp.get(i).getUri().substring(0, 35));

//                                    if (listnp.get(i).getUri().length() > 35) {
//                                        if (listnp.get(i).getUri().substring(0, 35).equalsIgnoreCase("/NetworkSample/NetworkExample?term=")) {
//                                            ad.writeNetworkTyping(listnp.get(i).getUri().substring(35).toLowerCase(), listnp.get(i).getPacket());
//                                        }
//                                    }
                                //               }


                            } catch (Exception ex) {
                                Logger.getLogger(NetworkMonitor.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }

                }
            }
        }








    }

    private void run(int time) throws IOException, InterruptedException {


        if (event == 0) {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec("/monitor/network/network.sh " + time);
            System.out.println(p.waitFor());

            if (p.waitFor() == 0) {

                NetworkProperties np = new NetworkProperties();

                List<NetworkProperties> listnp = np.convertNetworkTable("/monitor/network/test.csv");
                event = 1;
                AccessDatabase ad = new AccessDatabase();
                for (int i = 0; i < listnp.size(); i++) {
                    try {
                        //  System.out.println(listnp.get(i).getTime());
                        ad.writeNetoworkTable(listnp.get(i).getTime(), listnp.get(i).getSrc(), listnp.get(i).getDes(), listnp.get(i).getUri(), listnp.get(i).getPacket());

                        System.out.println(listnp.get(i).getUri().substring(0, 35));

                        if (listnp.get(i).getUri().substring(0, 35).equalsIgnoreCase("/NetworkSample/NetworkExample?term=")) {
                            ad.writeNetworkTyping(listnp.get(i).getUri().substring(35).toLowerCase(), listnp.get(i).getPacket());
                        }

                    } catch (Exception ex) {
                        Logger.getLogger(NetworkMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        } else {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec("/monitor/network/network1.sh " + time);

            System.out.println(p.waitFor());

            if (p.waitFor() == 0) {

                NetworkProperties np = new NetworkProperties();

                List<NetworkProperties> listnp = np.convertNetworkTable("/monitor/network/test1.csv");
                event = 0;
                AccessDatabase ad = new AccessDatabase();
                for (int i = 0; i < listnp.size(); i++) {
                    try {
                        System.out.println(listnp.get(i).getTime());
                        ad.writeNetoworkTable(listnp.get(i).getTime(), listnp.get(i).getSrc(), listnp.get(i).getDes(), listnp.get(i).getUri(), listnp.get(i).getPacket());

                    } catch (Exception ex) {
                        Logger.getLogger(NetworkMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        }



    }

    private int findPair(int i, List<NetworkProperties> listnp) {

        int index = -1;

        for (int j = i + 1; j < listnp.size(); j++) {

            if (listnp.get(i).getSrc().equalsIgnoreCase(listnp.get(j).getDes())) {
                index = j;
                break;
            }

        }

        return index;
    }
}
