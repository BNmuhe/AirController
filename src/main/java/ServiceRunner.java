import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.BufferedReader;
import java.util.Scanner;

public class ServiceRunner {
    public static void main(String[] args) {
        MqttService clientMqtt=new MqttService();
        clientMqtt.initMqttClient();
        new Thread(()->{
            Scanner scanner = new Scanner(System.in);
            if (scanner.nextLine().equals("exit")) {
                try {
                    clientMqtt.getClient().disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
