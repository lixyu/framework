package com.vcredit.framework.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
/**
 * Double数字工具
 * @author maoyibiao
 *
 */
public class NumberUtil {
	private static DecimalFormat format = new DecimalFormat("#.00");
	
	public static String format(Double d, String format) {
		return new DecimalFormat(format).format(d);
	}

	public static String format(Double d) {
		return format.format(d);
	}
	
	public static double sum(double ... values) {
		BigDecimal bd1 = null;
		for(double value:values){
			if(bd1==null)
				bd1 = new BigDecimal(value);
			else
				bd1 = bd1.add(new BigDecimal(Double.toString(value)));
		}
		return Double.parseDouble(format(bd1.doubleValue()));
	}

	public static double sub(double ... values) {
		BigDecimal bd1 = null;
		for(double value:values){
			if(bd1==null)
				bd1 = new BigDecimal(value);
			else
				bd1 = bd1.subtract(new BigDecimal(Double.toString(value)));
		}
		return Double.parseDouble(format(bd1.doubleValue()));
	}

	public static double multiply(double ... values) {
		BigDecimal bd1 = new BigDecimal(1);
		for(double value:values){
			bd1 = bd1.multiply(new BigDecimal(Double.toString(value)));
		}
		return Double.parseDouble(format(bd1.doubleValue()));
	}

	public static double divide(int scale,double ... values) {
		BigDecimal bd1 = new BigDecimal(1);
		for(double value:values){
			bd1 = bd1.divide(new BigDecimal(Double.toString(value)), scale, BigDecimal.ROUND_HALF_UP);
		}
		return Double.parseDouble(format(bd1.doubleValue()));
	}

	public static double divide(double ... values) {
		BigDecimal bd1 = new BigDecimal(1);
		for(double value:values){
			bd1 = bd1.divide(new BigDecimal(Double.toString(value)));
		}
		return bd1.doubleValue();
	}
	
	public static double round(double value, int scale) {
		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
	
	public static long toLong(double value) {
		BigDecimal bd1 = null;
		try{
			bd1 = new BigDecimal(Double.toString(value));
		}catch(Exception e){
			return 0L;
		}
		return bd1.multiply(new BigDecimal(100)).longValue();
	}
	
	public static double toDouble(Long value) {
		BigDecimal bd1 = null;
		try{
			bd1 = new BigDecimal(Double.toString(value));
		}catch(Exception e){
			return 0d;
		}
		return bd1.divide(new BigDecimal(100)).doubleValue();
	}
	
}
