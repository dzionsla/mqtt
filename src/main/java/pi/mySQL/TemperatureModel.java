package pi.mySQL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "temperature")
public class TemperatureModel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "sensor1")
    private String sensor1;

    @Column(name = "sensor2")
    private String sensor2;

	public TemperatureModel(String sensor1, String sensor2) {
		this.sensor1 = sensor1;
		this.sensor2 = sensor2;
	}

	public String getSensor1() {
		return sensor1;
	}

	public void setSensor1(String sensor1) {
		this.sensor1 = sensor1;
	}

	public String getSensor2() {
		return sensor2;
	}

	public void setSensor2(String sensor2) {
		this.sensor2 = sensor2;
	}

	@Override
	public String toString() {
		return "TemperatureModel [id=" + id + ", sensor1=" + sensor1 + ", sensor2=" + sensor2 + "]";
	}
    
}
