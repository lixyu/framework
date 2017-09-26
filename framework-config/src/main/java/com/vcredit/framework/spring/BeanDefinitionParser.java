package com.vcredit.framework.spring;

import java.util.List;
import java.util.Properties;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.vcredit.framework.quartz.AutowiringSpringBeanJobFactory;
import com.vcredit.framework.quartz.QuartJobSchedulingListener;

public class BeanDefinitionParser extends AbstractBeanDefinitionParser {
	@SuppressWarnings("rawtypes")
	private Class clazz;

	@SuppressWarnings("rawtypes")
	public BeanDefinitionParser(Class clazz) {
		this.clazz = clazz;
	}

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String id = getAttributer(element, "id");
		String clazzStr = getAttributer(element, "class");
		if (!StringUtils.isEmpty(clazzStr)) {
			try {
				clazz = Class.forName(clazzStr);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		if (!org.apache.commons.lang.StringUtils.isNotEmpty(id)) {
			id = clazz.getSimpleName();
		}
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		if ("kafka-provider".equals(element.getLocalName())) {
			providerInit(element, beanDefinition);
		}
		if ("kafka-consumer".equals(element.getLocalName())) {
			consumerInit(element, beanDefinition);
		}
		if ("redis".equals(element.getLocalName())) {
			redisInit(element, beanDefinition);
		}
		if ("quartz".equals(element.getLocalName())) {
			quartzInit(element, beanDefinition, parserContext);
		}
		beanDefinition.setLazyInit(false);
		beanDefinition.setBeanClass(clazz);
		parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
		return beanDefinition;
	}

	private RootBeanDefinition quartzInit(Node node, RootBeanDefinition beanDefinition, ParserContext parserContext) {
		addSpringBean(beanDefinition, parserContext, QuartJobSchedulingListener.class);
		RootBeanDefinition autowiringSpringBeanJobFactory = addSpringBean(beanDefinition, parserContext,
				AutowiringSpringBeanJobFactory.class);
		String location = getAttributer(node, "configLocation");
		beanDefinition.getPropertyValues().add("configLocation", location);
		beanDefinition.getPropertyValues().add("jobFactory", autowiringSpringBeanJobFactory);
		return beanDefinition;
	}

	private RootBeanDefinition redisInit(Node node, RootBeanDefinition beanDefinition) {
		setProperty((Element) node, beanDefinition);
		return beanDefinition;
	}

	private RootBeanDefinition providerInit(Node node, RootBeanDefinition beanDefinition) {
		Properties prop = new Properties();
		List<Node> nodeList = getChildNode(node, "props");
		for (Node nd : nodeList) {
			for (Node pop : getChildNode(nd, "prop")) {
				prop.setProperty(getAttributer(pop, "key"), getNodeVal(pop));
			}
		}
		beanDefinition.setInitMethodName("init");
		beanDefinition.getPropertyValues().addPropertyValue("props", prop);
		return beanDefinition;
	}

	private RootBeanDefinition consumerInit(Node node, RootBeanDefinition beanDefinition) {
		Properties prop = new Properties();
		List<Node> nodeList = getChildNode(node, "props");
		for (Node nd : nodeList) {
			for (Node pop : getChildNode(nd, "prop")) {
				prop.setProperty(getAttributer(pop, "key"), getNodeVal(pop));
			}
		}
		beanDefinition.getPropertyValues().addPropertyValue("props", prop);
		return beanDefinition;
	}

}
