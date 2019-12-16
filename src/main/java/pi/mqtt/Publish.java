package pi.mqtt;

import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class Publish {
	public Publish() throws InterruptedException {
        String topic        = "Home/T1";
        //String content      = "PC: Hello from PC";
        String content      = "Temperature 1: 21.31, Temperature 2: 23.45";
        int qos             = 0;
        String broker       = "tcp://test.mosquitto.org:1883";
        String clientId     = "PCPubdwewg";
        
        System.out.println("..................................TopicPublisher initializing..................................");

        try {
            // Create an Mqtt client
            MqttClient mqttClient = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            
            // Connect the client
            System.out.println("Connecting to broker messaging at " + broker);
            mqttClient.connect(connOpts);
            System.out.println("Connected");

            // Create a Mqtt message
            MqttMessage message = new MqttMessage(content.getBytes());
            // Set the QoS on the Messages - 
            // Here we are using QoS of 0 (equivalent to Direct Messaging in Solace)
            message.setQos(qos);
            
            System.out.println("Publishing message: " + content);
            
            // Publish the message
            
            for (int i = 0; i < 2000; i++) {
            	mqttClient.publish(topic, message);
            	TimeUnit.SECONDS.sleep(5);
			}
            
            // Disconnect the client
            mqttClient.disconnect();
            
            System.out.println("Message published. Exiting");

            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
	
    public static void main(String[] args) throws InterruptedException {
    	Publish mqttPubl = new Publish();
    }
}
