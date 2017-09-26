package com.vcredit.framework.enumtype;
/**
 * 
 * @author sk_mao
 * 	返回json数据key
 * 	result 集合数据
 * 	rows 记录数
 *	total 总数
 */
public enum ResultEnum {
	rows(0),total(1),result(2);
	private int code;
	private ResultEnum(int code){
		this.code=code;
	}
	public int getCode(){
		return this.code;
	}
}
