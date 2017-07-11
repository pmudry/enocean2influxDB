package sensors;

public class STM330 {
	final static float TMAX = 40f;
	
	byte[] enoceanID;
	String location;
	float temperature;
	
	public STM330(String location){
		this.location = location;
	}
	
	public void assignTemperature(byte temp){
		float factor = TMAX / 255.0f;
		temperature = (255 - (temp & 0xff)) * factor; // Cast required for signed to unsigned conversion
	}
	
	public String toString(){
		return "Room : " + location + ", temperature : " + temperature; 
	}
}
