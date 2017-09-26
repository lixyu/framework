package com.vcredit.framework.util;

import java.util.Random;

public class RandomUtil {
	public static final String CANDIDATE = "abcdefghijkmnpstuvwxyz123456789ABCDEFHJKMNRSTXYZ";
	
	/**
	 * 从候选字符串里随机字符串
	 * @param length
	 * @return
	 */
	public static String randomCandidate(int length, String candidate) {
		String result = "";
		Random random = new Random();
		
		for (int i = 0; i < length; i++) {
			result += candidate.charAt(random.nextInt(candidate.length()));
		} 
		
		return result;
	}
	
	public static String randomCandidate(int length) {
		return randomCandidate(length, CANDIDATE);
	}
	
	/**
	 * 随机大小写字母和数组
	 * @param length 长度
	 * @return
	 */
	public static String random(int length){
		StringBuffer val=new StringBuffer();
		while(true){
			if(val.length()==length)
				return val.toString();
			int n = (int)(Math.floor(Math.random()*122));
			if(n<10){
				val.append(n+"");
			}
			if((n>=65&&n<=90)||(n>=97&&n<=122)){
				val.append((char)n);
			}
		}
	}
	/**
	 * 随机数组
	 * @param length 长度
	 * @return
	 */
	public static String randomNumber(int length){
		StringBuffer val=new StringBuffer();
		while(true){
			if(val.length()==length)
				return val.toString();
			int n = (int)(Math.random()*10);
			val.append(n+"");
		}
	}
	

	/**
	 * 生成手机短信验证码
	 * @param numberFlag
	 * @param length
	 * @return
	 */
	public static String createRandom(boolean numberFlag, int length){  
		  String retStr = "";  
		  String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";  
		  int len = strTable.length();  
		  boolean bDone = true;  
		  do {  
		   retStr = "";  
		   int count = 0;  
		   for (int i = 0; i < length; i++) {  
		    double dblR = Math.random() * len;  
		    int intR = (int) Math.floor(dblR);  
		    char c = strTable.charAt(intR);  
		    if (('0' <= c) && (c <= '9')) {  
		     count++;  
		    }  
		    retStr += strTable.charAt(intR);  
		   }  
		   if (count >= 2) {  
		    bDone = false;  
		   }  
		  } while (bDone);  
		  return retStr;  
		 } 
	
	public static void main(String[] args) {
		System.out.println(randomCandidate(12));
	}
}
