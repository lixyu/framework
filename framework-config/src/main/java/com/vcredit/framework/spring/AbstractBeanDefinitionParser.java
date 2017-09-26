package com.vcredit.framework.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractBeanDefinitionParser implements org.springframework.beans.factory.xml.BeanDefinitionParser{

	protected List<Node> getChildNode(Node node,String localName){
		List<Node> nodes = new ArrayList<Node>();
		NodeList nodeList = node.getChildNodes();
		for(int i=0;i<nodeList.getLength();i++){
			 Node child = nodeList.item(i);
	        	if(localName.equals(child.getLocalName())){
	        		nodes.add(child);
	        	}
	     }
		return nodes;
	}
	
	protected String getAttributer(Node node,String attrName){
		if(node instanceof Element){
			Element el = (Element)node;
			return el.getAttribute(attrName);
		}
		return null;
	}
	
	protected String getNodeVal(Node node){
		if(node instanceof Element){
			Element el = (Element)node;
			return el.getTextContent();
		}
		return null;
	}
	
	protected void setProperty(Element element,BeanDefinition beanDefinition){
		for(Node node:getChildNode(element, "property")){
			String proCls = getAttributer(node, "ref");
			String property = getAttributer(node, "property");
			if(org.apache.commons.lang.StringUtils.isNotEmpty(proCls)&&org.apache.commons.lang.StringUtils.isNotEmpty(property)){
				beanDefinition.getPropertyValues().addPropertyValue(property, new RuntimeBeanReference(proCls));
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected RootBeanDefinition addSpringBean(RootBeanDefinition beanDefinition, ParserContext parserContext,
			Class cls){
		RootBeanDefinition bean = new RootBeanDefinition();
		bean.setBeanClass(cls);
		beanDefinition.setLazyInit(false);
		parserContext.getRegistry().registerBeanDefinition(cls.getSimpleName(), bean);
		return bean;
	}
}
