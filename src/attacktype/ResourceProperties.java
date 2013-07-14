/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package attacktype;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sijin
 */
public class ResourceProperties extends Properties {

    private Timestamp time;
    private double databaseCPU;
    private long databaseMemory;
    private double machineCPU;
    private long machineUsedMemory;
    private long machineTotalMemory;

    public static List<ResourceProperties> convertResourceTable(ResultSet resultSet) throws SQLException {
        List<ResourceProperties> dpl = new ArrayList<ResourceProperties>();

        while (resultSet.next()) {

            ResourceProperties dp = new ResourceProperties();

            dp.setTime(resultSet.getTimestamp("execution_time"));
            dp.setDatabaseCPU(resultSet.getDouble("mysql_cpu"));
            dp.setDatabaseMemory(resultSet.getLong("mysql_mem"));
            dp.setMachineCPU(resultSet.getDouble("cpu"));
            dp.setMachineUsedMemory(resultSet.getLong("used_memory"));
            dp.setMachineTotalMemory(resultSet.getLong("total_memory"));

            dpl.add(dp);

        }

        return dpl;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public double getDatabaseCPU() {
        return databaseCPU;
    }

    public void setDatabaseCPU(double databaseCPU) {
        this.databaseCPU = databaseCPU;
    }

    public long getDatabaseMemory() {
        return databaseMemory;
    }

    public void setDatabaseMemory(long databaseMemory) {
        this.databaseMemory = databaseMemory;
    }

    public double getMachineCPU() {
        return machineCPU;
    }

    public void setMachineCPU(double machineCPU) {
        this.machineCPU = machineCPU;
    }

    public long getMachineTotalMemory() {
        return machineTotalMemory;
    }

    public void setMachineTotalMemory(long machineTotalMemory) {
        this.machineTotalMemory = machineTotalMemory;
    }

    public long getMachineUsedMemory() {
        return machineUsedMemory;
    }

    public void setMachineUsedMemory(long machineUsedMemory) {
        this.machineUsedMemory = machineUsedMemory;
    }


}
