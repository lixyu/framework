package com.vcredit.framework.kafka;

import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public interface IKafkaProcessListener extends ApplicationListener<ContextRefreshedEvent>,ApplicationContextAware {

}
