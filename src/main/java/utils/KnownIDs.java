package utils;

import java.util.ArrayList;

import org.opencean.core.address.EnoceanId;

public class KnownIDs {
	static final byte[] therm1 = { (byte) 0x01, (byte) 0x9b, (byte) 0xdc, (byte) 0x47 };
	static final byte[] therm2 = { (byte) 0x01, (byte) 0x82, (byte) 0x3a, (byte) 0xeb };
	
	public static ArrayList<EnoceanId> ids;
	
	static {
		ids.add(new EnoceanId(therm1));
	}
}
