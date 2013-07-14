/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.ptql.ProcessFinder;

/**
 *
 * @author Sijin
 */
public class ProcessCPU extends ProcessResource {

    ProcCpu cpu = null;

    public ProcessCPU(String name) {

        long pid = getPid(name);
        System.out.println("pid "+ pid);

        try {
            cpu = sigar.getProcCpu(pid);

        } catch (SigarException se) {
            se.printStackTrace();
        }

    }

    public double getCpuPerc() {

        return cpu.getPercent();
    }

    public static void main(String[] args) throws SigarException {

        ProcessCPU cpu = new ProcessCPU("mysqld");
        System.out.println(cpu.getCpuPerc());

    }
}
