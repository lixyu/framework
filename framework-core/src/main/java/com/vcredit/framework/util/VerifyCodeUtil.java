package com.vcredit.framework.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码工具
 * 
 * @author maoyibiao
 *
 */
public class VerifyCodeUtil {
	private static final int CHAR_NUM = 4;
	private static final int LINE_NUM = 4;
	private static final int WIDTH = 150;
	private static final int HEIGHT = 50;
	private static String[] allChars = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "H",
			"K", "L", "M", "N", "R", "S", "T", "W", "X" };

	private static String[] numberChars = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };

	public static VerifyCode getVerifyCode(boolean onlyNumber) {
		// 构造内存中的图片
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics grap = image.getGraphics();
		grap.setColor(new Color(150, 150, 150));
		grap.fillRect(0, 0, WIDTH, HEIGHT);
		Random random = new Random();
		// 保存随机生成的验证码字符串
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < CHAR_NUM; i++) {
			String c = getRandomChar(random, onlyNumber);
			sb.append(c);
			grap.setColor(getRandomColor(random));
			grap.setFont(new Font(null, Font.BOLD, 50));
			grap.drawString(c, 10 + i * WIDTH / CHAR_NUM, HEIGHT / 2 + 20);
		}

		// 添加干扰线
		for (int i = 0; i < LINE_NUM; i++) {
			grap.setColor(getRandomColor(random));
			grap.drawLine(random.nextInt(WIDTH), random.nextInt(HEIGHT), random.nextInt(WIDTH), random.nextInt(HEIGHT));
		}

		return new VerifyCode(sb.toString(), image);
	}

	private static String getRandomChar(Random random, boolean onlyNumber) {
		if (onlyNumber)
			return numberChars[random.nextInt(numberChars.length)];
		else
			return allChars[random.nextInt(allChars.length)];
	}

	private static Color getRandomColor(Random random) {
		return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}
}
