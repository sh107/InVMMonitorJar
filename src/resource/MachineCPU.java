/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resource;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;

/**
 *
 * @author Sijin
 */
public class MachineCPU extends Resource{

    CpuPerc cpu = null;

    public MachineCPU() {

        try {
            cpu = sigar.getCpuPerc();
        } catch (SigarException se) {
            se.printStackTrace();
        }

    }

    public double getUsedCPUPerc(){

        return 1- cpu.getIdle();
    }

    public double getFreeCPUPerc(){

        return cpu.getIdle();
    }

    public static void main(String[] args) throws Exception {

        MachineCPU mm = new MachineCPU();
        System.out.println(mm.getUsedCPUPerc());

    }

}
