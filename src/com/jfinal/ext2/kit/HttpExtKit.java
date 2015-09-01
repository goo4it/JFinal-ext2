/**
 * 
 */
package com.jfinal.ext2.kit;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.jfinal.kit.HttpKit;

/**
 * 
 * @author BruceZCQ
 */
public class HttpExtKit {

	/**
	 * Send Hex String request
	 */
	public static String postHexString(String url, Map<String, String> queryParas, byte[] data, Map<String, String> headers) {
		return HttpKit.post(url, queryParas, HexKit.byteToHexString(data), headers);
	}
	
	/**
	 * Send byte data by post request
	 */
	public static String post(String url, Map<String, String> queryParas, byte[] data, Map<String, String> headers) {
		return HttpKit.post(url, queryParas, (new String(data)), headers);
	}
	
	public static byte[] readIncommingRequestHexByteData(HttpServletRequest request) {
		String ret = HttpKit.readIncommingRequestData(request);
		return HexKit.HexStringToBytes(ret);
	}
	
	public static byte[] readIncommingRequestByteData(HttpServletRequest request) {
		String ret = HttpKit.readIncommingRequestData(request);
		byte[] data = null;
		try {
			data = ret.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			new RuntimeException(e);
		}
		return data;
	}
}
