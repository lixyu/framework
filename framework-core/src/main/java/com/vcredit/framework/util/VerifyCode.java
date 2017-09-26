package com.vcredit.framework.util;

import java.awt.image.BufferedImage;

public class VerifyCode {
	private String code;
	private BufferedImage image;
	
	public VerifyCode(String code, BufferedImage image) {
		this.code = code;
		this.image = image;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
}
