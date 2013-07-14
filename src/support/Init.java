/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import monitor.CacheMonitor;
import monitor.NetworkMonitor;
import monitor.Monitor;
import monitor.DatabaseMonitor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/**
 *
 * @author Sijin
 */
public class Init {

    public static void main(String[] args) throws Exception {

        Options options = new Options();
        options.addOption("u", true, "options: user1, user2, user3, user4, user5");
        options.addOption("t", true, "options: database, cache, network, all");
        options.addOption("o", true, "options: screen, file, localdb, globaldb");
        options.addOption("i", true, "interval in seconds");

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.getOptionValue("t").equalsIgnoreCase("database")) {
            System.out.println("Database monitor starts...");
            Monitor dm = new DatabaseMonitor();
            dm.start(cmd.getOptionValue("o"), cmd.getOptionValue("i"));

        } else if (cmd.getOptionValue("t").equalsIgnoreCase("cache")) {
            System.out.println("Cache monitor starts...");
            Monitor dm = new CacheMonitor();
            dm.start(cmd.getOptionValue("o"), cmd.getOptionValue("i"));
        } else if (cmd.getOptionValue("t").equalsIgnoreCase("network")) {
            System.out.println("Network monitor starts...");
            Monitor dm = new NetworkMonitor();
            dm.start(cmd.getOptionValue("o"), cmd.getOptionValue("i"));
        } else if (cmd.getOptionValue("t").equalsIgnoreCase("all")) {
            System.out.println("Under development");
        } else {
            System.out.println("Please specify a valid monitor type");

        }



    }
}
