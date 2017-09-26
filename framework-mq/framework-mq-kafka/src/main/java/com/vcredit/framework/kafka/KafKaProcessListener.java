package com.vcredit.framework.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.vcredit.framework.bean.MsgBean;
import com.vcredit.framework.util.JacksonUtil;

public class KafKaProcessListener implements IKafkaProcessListener {

	private ConsumerConnector consumer;
	private Properties props;
	private KafKaProcess kafKaProcess;
	private ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	public void setProps(Properties props) {
		this.props = props;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
			if (pool.getPoolSize() == 0) {
				long heartMs = Long.parseLong((String) props.get("heart.ms"));
				String topic = props.getProperty("topic");
				pool.execute(new processThread(topic.split(","), heartMs));
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		kafKaProcess = applicationContext.getBean(KafKaProcess.class);
	}

	class processThread implements Runnable {
		private String[] topic;
		private long heartMs;

		public processThread(String[] topic, long heartMs) {
			this.topic = topic;
			this.heartMs = heartMs;
		}

		@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
		public void run() {
			Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
			for (String tic : topic) {
				topicCountMap.put(tic, new Integer(1));
			}
			while (true) {
				try {
					Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer
							.createMessageStreams(topicCountMap);
					for (String key : consumerMap.keySet()) {
						List<KafkaStream<byte[], byte[]>> kafkas = consumerMap.get(key);
						for (KafkaStream stream : kafkas) {
							ConsumerIterator<byte[], byte[]> it = stream.iterator();
							while (it.hasNext()) {
								String msg = "";
								try {
									msg = new String(it.next().message(), "UTF-8");
									MsgBean kafka = JacksonUtil.Json2Obj(msg, new TypeReference<MsgBean>() {
									});
									kafKaProcess.process(kafka);
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						}
					}
					new Thread().sleep(heartMs);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
