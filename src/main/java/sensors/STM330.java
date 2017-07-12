package sensors;

import utils.Utils;

public class STM330 {
	final static float TMAX = 40f;
	
	private byte[] enoceanID;
	private String location;
	private double temperature;
	
	
	public STM330(String location, byte[] id){
		this.location = location;
		this.enoceanID = id;
	}
	
	public String getID(){
		return Utils.bytesToHex(enoceanID); 
	}
	
	public double getTemperature(){
		return temperature;
	}
	
	public String getLocation(){
		return this.location;
	}
	
	public void assignTemperature(byte temp){
		double factor = TMAX / 255.0f;
		temperature = (255 - (temp & 0xff)) * factor; // Cast required for signed to unsigned conversion
	}
	
	public String toString(){
		return "Room : " + location + ", temperature : " + temperature; 
	}
}
