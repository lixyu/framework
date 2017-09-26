package com.vcredit.framework.text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.vcredit.framework.text.DataFormatter;
import com.vcredit.framework.text.TextProcess;
import com.vcredit.framework.text.TextTransformationFilter;
import com.vcredit.framework.text.config.Analyze;
import com.vcredit.framework.text.config.Config;
import com.vcredit.framework.text.config.Data;
import com.vcredit.framework.text.config.Package;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TextContextLoaderListener implements ServletContextListener {

	private final static String TEXT_CONFIG_PARAM_NAME = "TextConfigLocation";
	private final static String DEFAULT_LOCATION = "/text-config.xml";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			String location = sce.getServletContext().getInitParameter(TEXT_CONFIG_PARAM_NAME);
			if (location != null)
				TextProcess.config = init(location);
			else
				TextProcess.config = init(DEFAULT_LOCATION);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	public Config init(String configPath) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		builder.setEntityResolver(new EntityResolver() {

			@Override
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
			}
		});
		Document doc = builder.build(TextProcess.class.getResourceAsStream(configPath));
		try {
			return parseJDOM(doc);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 解析XML文档
	private Config parseJDOM(Document doc)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, DataConversionException {
		Config cfg = new Config();
		Element config = doc.getRootElement();
		cfg.setCodeType(config.getAttribute("code-type").getValue());
		Element analyzesElement = config.getChild("analyzes");
		Element packagesElement = config.getChild("packages");
		if (analyzesElement != null) {
			List analyzes = analyzesElement.getChildren("analyze");
			cfg.setAnalyzes(new ArrayList<Analyze>());
			for (int i = 0; i < analyzes.size(); i++) {
				Element analyzeElement = (Element) analyzes.get(i);
				Analyze analyze = new Analyze();
				analyze.setClassType(Class.forName(analyzeElement.getAttribute("class").getValue()));
				analyze.setSplit(analyzeElement.getAttribute("split").getValue());
				cfg.getAnalyzes().add(analyze);
				List<Element> lineElements = analyzeElement.getChildren();
				for (Element lineElement : lineElements) {
					if (lineElement.getName().equals("filter")) {
						analyze.setFilter((TextTransformationFilter) Class
								.forName(lineElement.getAttribute("class").getValue()).newInstance());
					}
					if (lineElement.getName().equals("datas")) {
						analyze.setDatas(new ArrayList<Data>());
						List datas = lineElement.getChildren("data");
						for (int j = 0; j < datas.size(); j++) {
							Element dataElement = (Element) datas.get(j);
							Data data = new Data();
							data.setField(dataElement.getAttribute("field").getValue());
							data.setIndex(dataElement.getAttribute("index").getIntValue());
							data.setType(Class.forName(dataElement.getAttribute("type").getValue()));
							Attribute attr = dataElement.getAttribute("formatter");
							if (attr != null)
								data.setFormatter((DataFormatter) Class.forName(attr.getValue()).newInstance());
							analyze.getDatas().add(data);
						}
					}
				}
			}
		}
		if (packagesElement != null) {
			List pakcages = packagesElement.getChildren("package");
			cfg.setPackages(new ArrayList<Package>());
			for (int i = 0; i < pakcages.size(); i++) {
				Element pakcageElement = (Element) pakcages.get(i);
				Package pa = new Package();
				pa.setClassType(Class.forName(pakcageElement.getAttribute("class").getValue()));
				pa.setSplit(pakcageElement.getAttribute("split").getValue());
				cfg.getPackages().add(pa);
				List<Element> lineElements = pakcageElement.getChildren();
				for (Element lineElement : lineElements) {
					if (lineElement.getName().equals("filter")) {
						pa.setFilter((TextTransformationFilter) Class
								.forName(lineElement.getAttribute("class").getValue()).newInstance());
					}
					if (lineElement.getName().equals("datas")) {
						pa.setDatas(new ArrayList<Data>());
						List datas = lineElement.getChildren("data");
						for (int j = 0; j < datas.size(); j++) {
							Element dataElement = (Element) datas.get(j);
							Data data = new Data();
							data.setField(dataElement.getAttribute("field").getValue());
							data.setIndex(dataElement.getAttribute("index").getIntValue());
							Attribute type = dataElement.getAttribute("type");
							if (type != null)
								data.setType(Class.forName(type.getValue()));
							Attribute attr = dataElement.getAttribute("formatter");
							if (attr != null)
								data.setFormatter((DataFormatter) Class.forName(attr.getValue()).newInstance());
							pa.getDatas().add(data);
						}
					}
				}
			}
		}
		return cfg;
	}
}
