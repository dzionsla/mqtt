package pi.firebase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TemperatureModelFB {
	
	private String time;
	private String sensor1;
	
	public TemperatureModelFB() {
	}
	
	public TemperatureModelFB(String sensor1) {
		this.sensor1 = sensor1;
		this.time = getDateTime();
	}
	
	public TemperatureModelFB(String time, String sensor1) {
		super();
		this.time = time;
		this.sensor1 = sensor1;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSensor1() {
		return sensor1;
	}

	public void setSensor1(String sensor1) {
		this.sensor1 = sensor1;
	}

	@Override
	public String toString() {
		return "TemperatureModel [time=" + time + ", sensor1=" + sensor1 + "]";
	}
	
	
	private String getDateTime() {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}
	
	
}
