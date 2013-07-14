/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monitor;

import communication.AccessDatabase;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sijin
 */
public class CacheMonitor extends Monitor{

    public static boolean localdb = false;
    public static int event = 0;
    
    public CacheMonitor() {
    }

    @Override
    public void start(String format, String interval) {
        int time = Integer.parseInt(interval);

        while (true) {
            try {

                if (format.equalsIgnoreCase("screen")) {
                    //  System.out.println(dataToJson(time));
                } else if (format.equalsIgnoreCase("file")) {
                    System.out.println("Under development");
                } else if (format.equalsIgnoreCase("localdb")) {
                    localdb = true;
              
                    run(time);
                   
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

    private static void Program(Object[] a, int temp) {

        for (int i = 0; i < temp; i++) {

            for (int j = 0; j < temp; j++) {

                int[] b = (int[]) a[i];

                b[j] = 1;
            }
        }


    }

    public void run(int time) {
       int temp = time;

        Object[] a = new Object[temp];
        for (int i = 0; i < temp; i++) {

            a[i] = new int[temp];

        }

        for (int i = 0; i < 1000000000; i++) {

            for (int j = 0; j < 100000000; j++) {

                Random randomGenerator = new Random();

                int random = randomGenerator.nextInt(10);

                int run = randomGenerator.nextInt(random+1);

                if (run == 0) {

                    long startTime = System.currentTimeMillis();

                    Program(a, temp);

                    long endTime = System.currentTimeMillis();
                    long totalTime = endTime - startTime;
                    
                    AccessDatabase ab = new AccessDatabase();
                    
                    try {
                        ab.writeCacheTable(new Timestamp(endTime), (int) totalTime);
                    } catch (Exception ex) {
                        Logger.getLogger(CacheMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }

   


                } else {

                    long startTime = System.currentTimeMillis();

                    Program(a, temp);

                    long endTime = System.currentTimeMillis();
                    long totalTime = endTime - startTime;

                    AccessDatabase ab = new AccessDatabase();

                    try {
                        ab.writeCacheTable(new Timestamp(endTime), (int) totalTime);
                    } catch (Exception ex) {
                        Logger.getLogger(CacheMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }



            }

        }

    }

    public static void main(String[] args) {
        CacheMonitor cm = new CacheMonitor();
        cm.run(6000);
    }

}
