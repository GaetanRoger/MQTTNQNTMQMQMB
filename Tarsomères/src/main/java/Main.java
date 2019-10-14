import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;

import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086", "user", "password");
        Pong response = influxDB.ping();
        if (response.getVersion().equalsIgnoreCase("unknown")) {
            System.out.println("Error pinging server.");
            return;
        }
        final String dbName = "data";
        influxDB.query(new Query("CREATE DATABASE " + dbName));
        Timer timer = new Timer();
        timer.schedule(new TarsomèresHandler(influxDB), 0, 5000);
    }
}
