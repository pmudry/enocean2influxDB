package utils;

public class Utils {

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 * Converts an array of byte to a nice string, with a ":" separator for each byte
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		
		StringBuffer s = new StringBuffer(new String(hexChars));
		String result = "";
		
		for(int i = 0; i < s.length(); i+= 2){
			result += s.charAt(i);
			result += s.charAt(i+1);
			result += ":";
		}
	
		return result.substring(0, result.length()-1);
	}
	
	public static void main(String args[]){
		byte[] test1 = {(byte)0xca, (byte)0xfe};
		System.out.println(bytesToHex(test1));
	}
}
