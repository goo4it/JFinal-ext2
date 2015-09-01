/**
 * 
 */
package com.jfinal.ext2.kit;

import com.jfinal.kit.StrKit;

/**
 * @author BruceZCQ
 */
public class HexKit {

	public static byte[] HexStringToBytes(String hexString) {
		if (StrKit.isBlank(hexString)) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] data = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			data[i] = (byte) (c2b(hexChars[pos]) << 4 | c2b(hexChars[pos + 1]));
		}
		return data;
	}

	private static byte c2b(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	
	public static String byteToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}
