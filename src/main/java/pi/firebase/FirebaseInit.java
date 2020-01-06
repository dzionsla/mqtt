package pi.firebase;

import java.io.FileInputStream;

//DatabaseReference usersRef = ref.child("users");
//Map<String, User> users = new HashMap<>();
//users.put("alanisawesome", new User("June 21, 1921", "Alan Turing", "zje1231bek"));
//users.put("gracehop", new User("December 21, 1921", "Grace Hopper", "dziwk31231a"));       
//usersRef.setValueAsync(users); 
//ref.child("T1").push().setValueAsync("TwojaStara");

//usersRef.setValue(users, new DatabaseReference.CompletionListener() {
//@Override
//public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//  if (databaseError != null) {
//    System.out.println("Data could not be saved " + databaseError.getMessage());
//  } else {
//    System.out.println("Data saved successfully.");
//  }
//}
//});

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseInit {
	
	private FirebaseDatabase database = null;
	private DatabaseReference ref = null;
	
	public FirebaseInit(String name) {
		fireBaseInit(name);
	}
	
	public DatabaseReference getRef() {
		return ref;
	}
	
	private DatabaseReference getReference(String name) {
        return database.getReference(name);
	}
	
	public void fireBaseInit(String name) {
		
		try {
			// Working in PC from zmienne środowiskowe or from file in project tree
//			FileInputStream serviceAccount =
//					  new FileInputStream("C:\\projects\\java\\MQTT\\cos tutaj jeszcze\\hometempmqtt-firebase-adminsdk-atjxm-af47ab54ec.json");
			//System.out.println(System.getProperty("user.dir"));
//			FirebaseOptions options = new FirebaseOptions.Builder()
//					  .setCredentials(GoogleCredentials.getApplicationDefault())
//					  .setDatabaseUrl("https://hometempmqtt.firebaseio.com")
//					  .build();
			
			// working in Raspi withoout external file adminsdk.json (hometempmqtt-firebase-adminsdk-atjxm-af47ab54ec.json)
			ClassPathResource resource = new ClassPathResource("hometempmqtt-firebase-adminsdk-atjxm-af47ab54ec.json");
			InputStream inputStream = resource.getInputStream();
			// working in Raspi with external file adminsdk.json (hometempmqtt-firebase-adminsdk-atjxm-af47ab54ec.json)
			//FileInputStream serviceAccount = new FileInputStream("/home/pi/Projects/Java/adminsdk.json");
			FirebaseOptions options = new FirebaseOptions.Builder()
					  .setCredentials(GoogleCredentials.fromStream(inputStream))
					  .setDatabaseUrl("https://hometempmqtt.firebaseio.com")
					  .build();

			FirebaseApp.initializeApp(options);
			
			this.database = FirebaseDatabase.getInstance();
			
			this.ref = getReference(name);
			
//			DatabaseReference ref = FirebaseDatabase.getInstance()
//				    .getReference("restricted_access/secret_document");
//			ref.addListenerForSingleValueEvent(new ValueEventListener() {
//				  @Override
//				  public void onDataChange(DataSnapshot dataSnapshot) {
//				    Object document = dataSnapshot.getValue();
//				    System.out.println(document);
//				  }
//
//				  @Override
//				  public void onCancelled(DatabaseError error) {
//				  }
//			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
