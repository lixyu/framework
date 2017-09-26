package com.vcredit.framework.spring;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.config.AdvisorComponentDefinition;
import org.springframework.aop.config.AdvisorEntry;
import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.aop.config.PointcutComponentDefinition;
import org.springframework.aop.config.PointcutEntry;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanNameReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.ctc.wstx.util.StringUtil;

public class AopBeanDefinitionParser extends AbstractBeanDefinitionParser {

	private static final String EXPRESSION = "expression";
	private static final String POINTCUT = "pointcut";
	private static final String ADVICE_BEAN_NAME = "adviceBeanName";
	private static final String ADVISOR = "advisor";

	private ParseState parseState = new ParseState();
	private AopBeanDefinition[] aopBeanDefinition;
	@SuppressWarnings("rawtypes")
	private Class clazz;

	@SuppressWarnings("rawtypes")
	public AopBeanDefinitionParser(Class clazz, AopBeanDefinition... aopBeanDefinitions) {
		this.clazz = clazz;
		this.aopBeanDefinition = aopBeanDefinitions;
	}

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		String cls = element.getAttribute("class");
		if (!StringUtil.isAllWhitespace(cls)) {
			try {
				clazz = Class.forName(cls);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		setProperty(element, beanDefinition);
		beanDefinition.setBeanClass(clazz);
		beanDefinition.setLazyInit(false);
		parserContext.getRegistry().registerBeanDefinition(clazz.getSimpleName(), beanDefinition);
		CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(),
				parserContext.extractSource(element));
		parserContext.pushContainingComponent(compositeDef);
		parserContext.popAndRegisterContainingComponent();

		configureAutoProxyCreator(parserContext, element);

		for (AopBeanDefinition aopBean : aopBeanDefinition) {
			if (POINTCUT.equals(aopBean.getType())) {
				parsePointcut(aopBean, parserContext);
			} else if (ADVISOR.equals(aopBean.getType())) {
				parseAdvisor(aopBean, parserContext);
			}
		}
		return beanDefinition;
	}

	private void configureAutoProxyCreator(ParserContext parserContext, Element element) {
		AopNamespaceUtils.registerAspectJAutoProxyCreatorIfNecessary(parserContext, element);
	}

	private void parseAdvisor(AopBeanDefinition aopBean, ParserContext parserContext) {
		AbstractBeanDefinition advisorDef = createAdvisorBeanDefinition(aopBean, parserContext);
		String id = aopBean.getId();

		try {
			this.parseState.push(new AdvisorEntry(id));
			String advisorBeanName = id;
			if (StringUtils.hasText(advisorBeanName)) {
				parserContext.getRegistry().registerBeanDefinition(advisorBeanName, advisorDef);
			} else {
				advisorBeanName = parserContext.getReaderContext().registerWithGeneratedName(advisorDef);
			}

			Object pointcut = parsePointcutProperty(aopBean, parserContext);
			if (pointcut instanceof BeanDefinition) {
				advisorDef.getPropertyValues().add(POINTCUT, pointcut);
				parserContext.registerComponent(
						new AdvisorComponentDefinition(advisorBeanName, advisorDef, (BeanDefinition) pointcut));
			} else if (pointcut instanceof String) {
				advisorDef.getPropertyValues().add(POINTCUT, new RuntimeBeanReference((String) pointcut));
				parserContext.registerComponent(new AdvisorComponentDefinition(advisorBeanName, advisorDef));
			}
		} finally {
			this.parseState.pop();
		}
	}

	private AbstractBeanDefinition createAdvisorBeanDefinition(AopBeanDefinition aopBean, ParserContext parserContext) {
		RootBeanDefinition advisorDefinition = new RootBeanDefinition(DefaultBeanFactoryPointcutAdvisor.class);
		// advisorDefinition.setSource(parserContext.extractSource(advisorElement));

		String adviceRef = aopBean.getAdviceRef();
		if (!StringUtils.hasText(adviceRef)) {
			parserContext.getReaderContext().error("'advice-ref' attribute contains empty value.", adviceRef,
					this.parseState.snapshot());
		} else {
			advisorDefinition.getPropertyValues().add(ADVICE_BEAN_NAME, new RuntimeBeanNameReference(adviceRef));
		}

		return advisorDefinition;
	}

	private AbstractBeanDefinition parsePointcut(AopBeanDefinition aopBean, ParserContext parserContext) {
		String id = aopBean.getId();
		String expression = aopBean.getExpression();

		AbstractBeanDefinition pointcutDefinition = null;

		try {
			this.parseState.push(new PointcutEntry(id));
			pointcutDefinition = createPointcutDefinition(expression);
			// pointcutDefinition.setSource(parserContext.extractSource(pointcutElement));

			String pointcutBeanName = id;
			if (StringUtils.hasText(pointcutBeanName)) {
				parserContext.getRegistry().registerBeanDefinition(pointcutBeanName, pointcutDefinition);
			} else {
				pointcutBeanName = parserContext.getReaderContext().registerWithGeneratedName(pointcutDefinition);
			}

			parserContext.registerComponent(
					new PointcutComponentDefinition(pointcutBeanName, pointcutDefinition, expression));
		} finally {
			this.parseState.pop();
		}

		return pointcutDefinition;
	}

	private Object parsePointcutProperty(AopBeanDefinition aopBean, ParserContext parserContext) {
		String pointcutRef = aopBean.getPointcutRef();
		if (!StringUtils.hasText(pointcutRef)) {
			parserContext.getReaderContext().error("'pointcut-ref' attribute contains empty value.", pointcutRef,
					this.parseState.snapshot());
			return null;
		}
		return pointcutRef;
	}

	protected AbstractBeanDefinition createPointcutDefinition(String expression) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition(AspectJExpressionPointcut.class);
		beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		beanDefinition.setSynthetic(true);
		beanDefinition.getPropertyValues().add(EXPRESSION, expression);
		return beanDefinition;
	}
}
