package com.vcredit.framework.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 图片工具
 * 
 * @author maoyibiao
 *
 */
public class ImageUtil {
	private static final int NUM = 4;
	// private static final int LINE = 10;
	private static final int WIDTH = 110;
	private static final int HEIGHT = 30;

	/**
	 * 画图
	 * 
	 * @return
	 */
	public static Map<String, BufferedImage> dynaImage() {
		Map<String, BufferedImage> m = new HashMap<String, BufferedImage>();
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics grap = image.getGraphics();
		grap.setColor(new Color(150, 150, 150));
		grap.fillRect(0, 0, WIDTH, HEIGHT);
		String ranString = RandomUtil.randomCandidate(NUM);
		for (int i = 0; i < NUM; i++) {
			grap.setColor(getRandomColor());
			grap.setFont(new Font(null, Font.BOLD, 20));
			grap.drawString(String.valueOf(ranString.toCharArray()[i]), 10 + i * WIDTH / NUM, HEIGHT / 2 + 5);
		}
		for (int i = 0; i < NUM; i++) {
			Random ran = new Random();
			grap.setColor(getRandomColor());
			grap.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT), ran.nextInt(WIDTH), ran.nextInt(HEIGHT));

		}
		m.put(ranString, image);
		return m;
	}

	private static Color getRandomColor() {
		Random ran = new Random();
		return new Color(ran.nextInt(128), ran.nextInt(128), ran.nextInt(128));
	}

	/**
	 * 返回压缩图片的base64编码
	 * 
	 * @throws Exception
	 */
	public static String compressPic(InputStream is) throws Exception {
		double comBase = 400;
		String ocrimg = "";
		try {
			Image src = ImageIO.read(is);
			src.flush();
			int srcHeight = src.getHeight(null);
			int srcWidth = src.getWidth(null);
			int deskHeight = 0;// 缩略图高
			int deskWidth = 0;// 缩略图宽
			double srcScale = (double) srcHeight / srcWidth;
			/** 缩略图宽高算法 */
			if ((double) srcHeight > comBase || (double) srcWidth > comBase) {
				if (srcScale >= 1) {
					deskHeight = (int) comBase;
					deskWidth = srcWidth * deskHeight / srcHeight;
				} else {
					deskWidth = (int) comBase;
					deskHeight = srcHeight * deskWidth / srcWidth;
				}
			} else {
				deskHeight = srcHeight;
				deskWidth = srcWidth;
			}
			BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_3BYTE_BGR);
			tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); // 绘制缩小后的图
			// 转base64
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(tag, "jpg", baos);
			byte[] datas = baos.toByteArray();
			ocrimg = Base64Util.base64Encode(datas);
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("压缩图片失败");
		}
		return ocrimg;
	}
}
