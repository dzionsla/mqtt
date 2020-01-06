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
import org.hibernate.Session;
import org.hibernate.Transaction;

//import com.google.firebase.database.DatabaseReference;

import pi.firebase.FirebaseInit;
import pi.firebase.TemperatureModelFB;
import pi.mySQL.HibernateUtil;
import pi.mySQL.TemperatureModel;

public class Subscribe {
	
	public Subscribe() {
		//String topic        = "T/GettingStarted/pubsub";
		String topic        = "Home/T1";
        int qos             = 0;
        String broker       = "tcp://test.mosquitto.org:1883";
        //String broker       = "tcp://192.168.1.232:1883";
        //String broker       = "tcp://localhost:1883";
        String clientId     = "RasPi4";
   
        System.out.println("..................................TopicSubscriber initializing..................................");
        
        // Connection to Firebase
        FirebaseInit firebase = new FirebaseInit("Home/Temperature");
        //firebase.fireBaseInit("Home/Temperature"); nope

        try {
            // Create an Mqtt client
            MqttClient mqttClient = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);
            connOpts.setAutomaticReconnect(true);
            connOpts.setKeepAliveInterval(5);
            connOpts.setConnectionTimeout(0);
            
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
                    
                    String temperature = parseTemperature(msg);           		
            		String sensorNumber = parseSensorNumber(msg);
                    
            		// Connection and send to mySQL
//            		TemperatureModel tm1 = new TemperatureModel(temperature, temperature);
//                    mySQLConnection(tm1); 
                    
                    // Send to Firebase Realtime Database
            		//firebase.getRef().child("T" + sensorNumber).push().setValueAsync(temperature);
            		firebase.getRef().child("T" + sensorNumber).push().setValueAsync(new TemperatureModelFB(temperature));

                }	

                public void connectionLost(Throwable cause) {
                	System.out.println("Connection to broker messaging lost!" + cause.getMessage());
//                	try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//                    try {
//                    	System.out.println("1");
//                    	//Thread.sleep(1000);
//						mqttClient.connect(connOpts);
//						//Thread.sleep(5000);
//						if (mqttClient.isConnected()) {
//							System.out.println("jebne connected");
//							//clientId = mqttClient.generateClientId();
//							//mqttClient.connect(connOpts);
//							System.out.println(mqttClient.isConnected());
//							System.out.println(mqttClient.getClientId());
//							
//						} else {
//							System.out.println("NOT connected");
//						}
//						System.out.println("1.0");
//						//mqttClient.reconnect();
//						System.out.println("1.1");
//						mqttClient.subscribe(topic, qos);
//						System.out.println("1.2");
//					} catch (MqttSecurityException e) {
//						// TODO Auto-generated catch block
//						System.out.println("2");
//						e.printStackTrace();
//					} catch (MqttException e) {
//						// TODO Auto-generated catch block
//						System.out.println("3");
//						e.printStackTrace();
////					} catch (InterruptedException e) {
////						// TODO Auto-generated catch block
////						e.printStackTrace();
					//}
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                	System.out.println("jebneeee");
                }

            });
            
            // Connect the client
            System.out.println("Connecting to broker messaging at " + broker);
            mqttClient.connect(connOpts);
            System.out.println("Connected");
            
            // Subscribe client to the topic filter and a QoS level of 0
            System.out.println("Subscribing client to topic: " + topic);
            mqttClient.subscribe(topic, qos);
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
	
	private void mySQLConnection(TemperatureModel tm1) {
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
	
	private String parseTemperature(String msg) {
		// Get temperature
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
		//System.out.println(sensor1 + " - " + sensor2);
		return sensor1;
	}
	
	private String parseSensorNumber(String msg) {
		// Get child
		String child = null;
		Pattern p2 = Pattern.compile("\\d+");
		Matcher m2 = p2.matcher(msg);
		m2.find();
		//System.out.println(child);
		return m2.group();
	}
	
	
}
