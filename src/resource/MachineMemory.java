/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 *
 * @author Sijin
 */
public class MachineMemory extends Resource{
    
    Mem mem = null;

    public MachineMemory() {

        try {
            mem = sigar.getMem();
        } catch (SigarException se) {
            se.printStackTrace();
        }

    }

    public long getFreeMemory() {

        return mem.getFree();
    }

    public long getTotalMemory(){
        return mem.getTotal();
    }

    public long getUsedMemory(){

        return mem.getUsed();
    }

    public static void main(String[] args) throws Exception {

        MachineMemory mm = new MachineMemory();
        System.out.println(mm.getTotalMemory());

    }
}
