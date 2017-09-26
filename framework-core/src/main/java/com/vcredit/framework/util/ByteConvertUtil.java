package com.vcredit.framework.util;

import java.util.List;
/**
 * 基本类型高低字节流之间转换
 * @author sk_mao
 *
 */
public class ByteConvertUtil {
	public static byte[] longToByte(long number){  
		long temp = number;     
		byte[] b =new byte[8];        
		for(int i =0; i < b.length; i++){     
			b[i]=new Long(temp &0xff).byteValue();// 将最低位保存在最低位     
			temp = temp >>8;// 向右移8位  
		}         
		return b;  
	}
	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}
	public static byte[] intToBytes(int i) {
		byte[] result = new byte[4];
		result[3] = (byte) ((i >> 24) & 0xFF);
		result[2] = (byte) ((i >> 16) & 0xFF);
		result[1] = (byte) ((i >> 8) & 0xFF);
		result[0] = (byte) (i & 0xFF);
		return result;
	}
	public static int byteArrayToInt(byte[] b, int offset) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i + offset] & 0x000000FF) << shift;// 往高位游
		}
		return value;
	}

	public static int byteArrayToInt(byte[] b) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			value |= b[i];
			value = value << 8;
		}
		return value;
	}
	public static byte[] shortToByte(short number){ 
		int temp = number;     
		byte[] b =new byte[2];    
		for(int i =0; i < b.length; i++){
			b[i]=new Integer(temp &0xff).byteValue();// 将最低位保存在最低位 
			temp = temp >>8;// 向右移8位     
		}       
		return b;    
	}
	public static byte[] ListToByteArray(List<Byte> bytes){
		if(bytes==null)
			return null;
		byte[] bbs=new byte[bytes.size()];
		for(int i=0;i<bytes.size();i++){
			bbs[i]=bytes.get(i);
		}
		return bbs;
	}
	
	public static String printByteArray(byte[] bs){
		StringBuffer buf = new StringBuffer();
		if(bs.length>0){
			for(byte b:bs){
				buf.append(",").append(b);
			}
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}
	
	public static Byte[] toByteArray(byte[] bs){
		Byte[] b = new Byte[bs.length];
		for(int i=0;i<bs.length;i++){
			b[i] = bs[i];
		}
		return b;
	}
}
