import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class FetcherSender extends TimerTask {
    @Override
    public void run() {
        String str = fetch();

        if (!str.isEmpty()) {
            send(str);
        }
    }

    private String fetch() {
        HttpClient client = HttpClientBuilder.create().setConnectionTimeToLive(2, TimeUnit.MINUTES).build();
        HttpGet request = new HttpGet("http://192.168.20.104/api/xdevices.json?cmd=10");

        try {
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity);
        } catch (IOException e) {
            System.out.println("Unable to read data from site: " + e.getMessage());
            return "";
        }
    }


    private void send(final String message) {
        try {
            publish(message);
        } catch (final MqttException e) {
            e.printStackTrace();
        }
    }

    private void publish(final String messageStr) throws MqttException {
        MqttClient client = new MqttClient("tcp://localhost:8123", MqttClient.generateClientId());
        client.connect();
        MqttMessage message = new MqttMessage();
        message.setPayload(messageStr.getBytes());
        client.publish("MQTTNQNTMQMQMB", message);
        client.disconnect();
        System.out.println(new Date().toString() + " | Envoyé");
    }
}
