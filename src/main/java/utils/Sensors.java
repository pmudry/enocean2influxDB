package utils;

import java.util.HashMap;

import org.opencean.core.address.EnoceanId;

import sensors.STM330;

public class Sensors {
	public static final byte[] dummy  = { (byte) 0x01, (byte) 0x2, (byte) 0x3, (byte) 0x4 };
	public static final byte[] therm1 = { (byte) 0x01, (byte) 0x9b, (byte) 0xdc, (byte) 0x47 };
	public static final byte[] therm2 = { (byte) 0x01, (byte) 0x82, (byte) 0x3a, (byte) 0xeb };
	
	public static HashMap<EnoceanId, STM330> sensors = new HashMap<EnoceanId, STM330>();
	
	static {
		sensors.put(new EnoceanId(therm1), new STM330("balcon", therm1));
		sensors.put(new EnoceanId(therm2), new STM330("salon", therm2));
		sensors.put(new EnoceanId(dummy), new STM330("test sensor", dummy));
	}
}
