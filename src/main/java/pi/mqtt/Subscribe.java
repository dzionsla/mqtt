package pi.mqtt;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pi.mySQL.HibernateUtil;
import pi.mySQL.TemperatureModel;

public class Subscribe {
	
	public Subscribe() {
		//String topic        = "T/GettingStarted/pubsub";
		String topic        = "Home/T1";
        int qos             = 0;
        String broker       = "tcp://mqtt.eclipse.org:1883";
        String clientId     = "PCSub-123411343242424226gsg";
              
        System.out.println("..................................TopicSubscriber initializing..................................");

        try {
            // Create an Mqtt client
            MqttClient mqttClient = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            connOpts.setKeepAliveInterval(15);
            connOpts.setConnectionTimeout(30);
            
            mqttClient.setCallback(new MqttCallback() {

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // Called when a message arrives from the server that
                    // matches any subscription made by the client
                    String time = new Timestamp(System.currentTimeMillis()).toString();
                    String msg = new String(message.getPayload());
                    System.out.println("\nReceived a Message!" +
                            "\n\tTime:    " + time + 
                            "\n\tTopic:   " + topic + 
                            "\n\tMessage: " + msg + 
                            "\n\tQoS:     " + message.getQos() + "\n");
                    
            		String sensor1 = null;
                    String sensor2 = null;
                    Pattern p1 = Pattern.compile("\\d+.\\d+");
            		Matcher m1 = p1.matcher(msg);
            		int cnt = 0;
            		while (m1.find()) {
            			if (cnt == 0) {
            				sensor1 = m1.group();
            			} else {
            				sensor2 = m1.group();
            			}
            			cnt++;
            		}
            		System.out.println(sensor1 + " - " + sensor2);
            		
            		TemperatureModel tm1 = new TemperatureModel(sensor1, sensor2);
                    
                    Transaction transaction = null;
            		
            		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                        // start a transaction
                        transaction = session.beginTransaction();
                        
                        session.save(tm1);
                        
                        // commit transaction
                        transaction.commit();
                    } catch (Exception e1) {
                        if (transaction != null) {
                            transaction.rollback();
                        }
                        e1.printStackTrace();
                    } 
                }

                public void connectionLost(Throwable cause) {
                	System.out.println("Connection to broker messaging lost!" + cause.getMessage());
                	
//                    try {
//                    	System.out.println("1");
//						//mqttClient.connect(connOpts);
//						if (mqttClient.isConnected()) {
//							System.out.println("jebne connected");
//						} else {
//							System.out.println("NOT connected");
//						}
//						System.out.println("1.0");
//						//mqttClient.reconnect();
//						System.out.println("1.1");
//						//mqttClient.subscribe(subTopic, qos);
//						System.out.println("1.2");
//					} catch (MqttSecurityException e) {
//						// TODO Auto-generated catch block
//						System.out.println("2");
//						e.printStackTrace();
//					} catch (MqttException e) {
//						// TODO Auto-generated catch block
//						System.out.println("3");
//						e.printStackTrace();
//					}
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                	System.out.println("jebneeee");
                }

            });
            
            // Connect the client
            System.out.println("Connecting to broker messaging at " + broker);
            mqttClient.connect(connOpts);
            if (mqttClient.isConnected()) {
				System.out.println("CONNECTED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}
            System.out.println("Connected");
            
            // Topic filter the client will subscribe to
            final String subTopic = topic;
            
            // Callback - Anonymous inner-class for receiving messages
           
            
            // Subscribe client to the topic filter and a QoS level of 0
            System.out.println("Subscribing client to topic: " + subTopic);
            mqttClient.subscribe(subTopic, qos);
            System.out.println("Subscribed");

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
	}

}
