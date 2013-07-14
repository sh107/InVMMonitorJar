/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resource;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.ptql.ProcessFinder;

/**
 *
 * @author Sijin
 */
public class ProcessResource extends Resource{

    protected long getPid(String string) {

        ProcessFinder find = new ProcessFinder(sigar);
        long pid = 0;
        try {
            pid = find.findSingleProcess("Exe.Name.ct=" + string);
        } catch (SigarException ex) {
            Logger.getLogger(ProcessCPU.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pid;
    }
}
