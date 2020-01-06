package pi.mqtt;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class Publish {
	public Publish() throws InterruptedException {
        String topic        = "Home/T1";
        //String topic        = "testTopic";
        //String content      = "PC: Hello from PC";
        String content      = null;//"Temperature 1: 21.31, Temperature 2: 23.45";
        int qos             = 0;
        String broker       = "tcp://test.mosquitto.org:1883";
        //String broker       = "tcp://192.168.1.232:1883";
        String clientId     = "PCPub";
        
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
//            content = "Temperature 1: " + randomRealNumber() + ", Temperature 2: " + randomRealNumber();
//            MqttMessage message = new MqttMessage(content.getBytes());
            // Set the QoS on the Messages - 
            // Here we are using QoS of 0 (equivalent to Direct Messaging in Solace)
            //message.setQos(qos);
            
            //System.out.println("Publishing message: " + content);
            
            // Publish the message
            
            for (int i = 0; i < 2; i++) {
            	if (i % 2 == 1) {
            		content = "T1: Temperature 1: " + randomRealNumber() + ", Temperature 2: " + randomRealNumber();
				} else {
					content = "T2: Temperature 1: " + randomRealNumber() + ", Temperature 2: " + randomRealNumber();
				}
            	
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(qos);
            	mqttClient.publish(topic, message);
            	//content = "Temperature 1: " + randomRealNumber() + ", Temperature 2: " + randomRealNumber();
            	TimeUnit.SECONDS.sleep(2);
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

	public double randomRealNumber() {
		
		Random r = new Random();
		double randomValue = 10.0 + (35.0 - 10.0) * r.nextDouble();
		return randomValue;
	}
	
	
    public static void main(String[] args) throws InterruptedException {
    	Publish mqttPubl = new Publish();
    }
}
