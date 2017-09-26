package com.vcredit.framework.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.vcredit.framework.MQService;
import com.vcredit.framework.interceptor.ExceptionHandler;
import com.vcredit.framework.interceptor.KafKaSendAdvice;
import com.vcredit.framework.interceptor.LogAdvice;
import com.vcredit.framework.interceptor.RequestParamValidAdvice;
import com.vcredit.framework.kafka.KafKaProcessListener;
import com.vcredit.framework.redis.RedisService;

public class NamespaceHandler extends NamespaceHandlerSupport {
	@Override
	public void init() {
		 registerBeanDefinitionParser("kafka-provider", new BeanDefinitionParser(KafKaSendAdvice.class));
		 registerBeanDefinitionParser("kafka-consumer", new BeanDefinitionParser(KafKaProcessListener.class));
		 registerBeanDefinitionParser("redis", new BeanDefinitionParser(RedisService.class));
		 registerBeanDefinitionParser("mq", new BeanDefinitionParser(MQService.class));
		 registerBeanDefinitionParser("exceptionHandler", new BeanDefinitionParser(ExceptionHandler.class));
		 registerBeanDefinitionParser("paramAdvice", new AopBeanDefinitionParser(RequestParamValidAdvice.class,new AopBeanDefinition("paramAdvice", "@annotation(javax.validation.Valid)", "pointcut",null, null),
				 new AopBeanDefinition("requestParamValidAdviceAdvisor", null, "advisor", "RequestParamValidAdvice","paramAdvice")));
		 registerBeanDefinitionParser("logAdvice", new AopBeanDefinitionParser(LogAdvice.class,new AopBeanDefinition("datalogInsertPointCut", "@annotation(com.vcredit.framework.annotation.Log)", "pointcut",null, null),
				new AopBeanDefinition("logAdvisor", null, "advisor", "LogAdvice","datalogInsertPointCut")));
//		 registerBeanDefinitionParser("tcc", new AopBeanDefinitionParser(OpenTransactionalMethodInvok.class,new AopBeanDefinition("openTransactionalPointCut", "@annotation(com.alibaba.dubbo.tcc.transactional.anno.OpenTran)", "pointcut",null, null),
//					new AopBeanDefinition("openTransactionalAdvisor", null, "advisor", "OpenTransactionalMethodInvok","openTransactionalPointCut")));
		 registerBeanDefinitionParser("quartz", new BeanDefinitionParser(SchedulerFactoryBean.class));
	}
}
