import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CallbackMqtt implements MqttCallback {
    private InfluxDB influxDB;
    private MqttClient mqttClient;

    public CallbackMqtt(InfluxDB influxDB) {
        super();
        this.influxDB = influxDB;
        try {
            mqttClient = new MqttClient("tcp://192.168.20.110:8123", MqttClient.generateClientId());
            mqttClient.setCallback(this);
            mqttClient.connect();
            mqttClient.subscribe("MQTTNQNTMQMQMB");
            mqttClient.setTimeToWait(10000);

        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) {
        System.out.println("Message received:\n\t"+ new String(mqttMessage.getPayload()) );

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> map;

        try {
            map = objectMapper.readValue(mqttMessage.getPayload(), new TypeReference<Object>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Data result = Data.builder()
                .INDEX_C1((Integer) map.get("INDEX_C1"))
                .INDEX_C2((Integer) map.get("INDEX_C2"))
                .product((String) map.get("product"))
                .T1_BASE((Integer) map.get("T1_BASE"))
                .T1_PAPP((Integer) map.get("T1_PAPP"))
                .T1_PTEC((String) map.get("T1_PTEC"))
                .T2_BASE((Integer) map.get("T2_BASE"))
                .T2_PAPP((Integer) map.get("T2_PAPP"))
                .T2_PTEC((String) map.get("T2_PTEC"))
                .build();

        Point currentPoint = Point.measurement("info")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("product", result.getProduct())
                .addField("T1_PTEC", result.getT1_PTEC())
                .addField("T1_PAPP", result.getT1_PAPP())
                .addField("T1_BASE", result.getT1_BASE())
                .addField("T2_PTEC", result.getT2_PTEC())
                .addField("T2_PAPP", result.getT2_PAPP())
                .addField("T2_BASE", result.getT2_BASE())
                .addField("INDEX_C1", result.getINDEX_C1())
                .addField("INDEX_C2", result.getINDEX_C2())
                .build();
        try {
            influxDB.write(currentPoint);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // not used in this example
    }
}
