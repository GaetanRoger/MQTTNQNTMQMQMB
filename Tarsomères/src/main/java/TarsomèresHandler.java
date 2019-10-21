import org.influxdb.InfluxDB;

import java.util.TimerTask;

public class TarsomèresHandler {

    private final InfluxDB influxDB;

    public TarsomèresHandler(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }

    public void run() {
        CallbackMqtt callbackMqtt = new CallbackMqtt(influxDB);
    }
}
