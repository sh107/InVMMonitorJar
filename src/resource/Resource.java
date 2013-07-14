/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import communication.AccessDatabase;
import java.util.logging.Level;
import java.util.logging.Logger;
import monitor.DatabaseMonitor;
import net.sf.json.JSONObject;
import org.hyperic.sigar.Sigar;

/**
 *
 * @author Sijin
 */
public class Resource {

    protected static Sigar sigar = new Sigar();

    public String getResource() {

        String json = "";

        JSONObject jobj = new JSONObject();

        MachineCPU mm = new MachineCPU();
        jobj.put("cpu", mm.getUsedCPUPerc());

        MachineMemory mk = new MachineMemory();
        jobj.put("used_memory", mk.getUsedMemory());
        jobj.put("total_memory", mk.getTotalMemory());

        ProcessCPU cpu = new ProcessCPU("mysqld");

        jobj.put("mysql_cpu", cpu.getCpuPerc());

        ProcessMemory mem = new ProcessMemory("mysqld");

        jobj.put("mysql_mem", mem.getMem());

        if (DatabaseMonitor.localdb) {
            AccessDatabase dao = new AccessDatabase();
            try {
                dao.writeResourceTable(mm.getUsedCPUPerc(), mk.getUsedMemory(), mk.getTotalMemory(), cpu.getCpuPerc(), mem.getMem());
            } catch (Exception ex) {
                Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }

        json = jobj.toString();

        return json;

    }
}
