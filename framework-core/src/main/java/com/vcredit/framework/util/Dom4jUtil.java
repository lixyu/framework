package com.vcredit.framework.util;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dom4jUtil {
	private static final Logger logger = LoggerFactory.getLogger(Dom4jUtil.class);

	public static Document getDocument(String xml) {
		Document document = null;
		try {
			document = DocumentHelper.parseText(xml);
			if (document == null) {
				throw new RuntimeException("document is null");
			}
			return document;
		} catch (Exception e) {
			logger.error("parse xml fail", e);
			throw new RuntimeException("get document fail", e);
		}
	}

	/**
	 * 解析XML获取Root节点
	 * 
	 * @param xml
	 * @return Element
	 */
	public static Element getRoot(String xml) {
		Document document = getDocument(xml);
		return document.getRootElement();
	}

	/**
	 * 获取节点值
	 * 
	 * @param root
	 * @param nodeName
	 * @return String
	 */
	public static String getValue(Element root, String nodeName) {
		try {
			if (root == null || StringUtils.isBlank(nodeName)) {
				return null;
			}
			Element nodeElement = root.element(nodeName);
			if (nodeElement == null) {
				return null;
			}
			return nodeElement.getTextTrim();
		} catch (Exception e) {
			logger.error("get node(" + nodeName + ") value fail");
			return null;
		}
	}
}
