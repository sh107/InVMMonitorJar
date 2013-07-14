/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resource;

import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 *
 * @author Sijin
 */
public class ProcessMemory extends ProcessResource{
    ProcMem cpu = null;

    public ProcessMemory(String name) {

        long pid = getPid(name);
        System.out.println("pid "+ pid);

        try {
            cpu = sigar.getProcMem(pid);

        } catch (SigarException se) {
            se.printStackTrace();
        }

    }

    public long getMem() {

        return cpu.getResident();
    }

    public static void main(String[] args) throws SigarException {

        ProcessMemory cpu = new ProcessMemory("mysqld");
        System.out.println(cpu.getMem());

    }
}
