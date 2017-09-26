package com.vcredit.framework.interceptor;

import static com.vcredit.framework.ecache.EcacheUtil.cache;
import static com.vcredit.framework.ecache.EcacheUtil.del;
import static com.vcredit.framework.ecache.EcacheUtil.get;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.vcredit.framework.bean.MsgBean;
import com.vcredit.framework.util.JacksonUtil;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafKaSendAdvice implements SendAdvice {
	private Properties props;
	private Producer<String, String> producer;
	private final static String CACHE_KEY = "kafkalog";
	private final static String CACHE_NAME = "kafkaCache";
	private ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	private Map<Integer, Integer> count = new ConcurrentHashMap<>();

	private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	private static Logger logger = LogManager.getLogger(KafKaSendAdvice.class);

	public void setProps(Properties props) {
		this.props = props;
	}

	public void init() {
		if (producer == null) {
			producer = new Producer<String, String>(new ProducerConfig(props));
		}
		if (pool.getPoolSize() == 0) {
			pool.execute(new kafkaRunable());
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public synchronized void addMessage(MsgBean kafka) {
		KeyedMessage msg = null;
		try {
			msg = new KeyedMessage<String, String>(kafka.getTopic(), JacksonUtil.Obj2Json(kafka));
			cachedThreadPool.execute(new kafkaSendRunable(msg));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized void send() {
		Object obj = get(CACHE_NAME, CACHE_KEY);
		if (obj != null) {
			List<KeyedMessage<String, String>> list = (List<KeyedMessage<String, String>>) obj;
			producer.send(list);
			del(CACHE_NAME, CACHE_KEY);
		}
	}

	class kafkaRunable extends Thread {

		@SuppressWarnings("static-access")
		@Override
		public void run() {
			long heartMs = Long.parseLong((String) props.get("heart.ms"));
			while (true) {
				try {
					send();
					this.sleep(heartMs);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	class kafkaSendRunable extends Thread {
		@SuppressWarnings("rawtypes")
		private KeyedMessage msg;

		@SuppressWarnings("rawtypes")
		public kafkaSendRunable(KeyedMessage msg) {
			super();
			this.msg = msg;
		}

		@Override
		public void run() {
			try {
				producer.send(msg);
			} catch (Exception e) {
				logger.error("=============================message not operated" + msg);
				e.printStackTrace();
				List<KeyedMessage<String, String>> list = null;
				Object obj = get(CACHE_NAME, CACHE_KEY);
				if (obj == null)
					list = new CopyOnWriteArrayList<KeyedMessage<String, String>>();
				else
					list = (List<KeyedMessage<String, String>>) obj;

				if (count.containsKey(msg.hashCode())) {
					Integer maxCount = count.get(msg.hashCode());
					if (maxCount >= 3) {
						count.remove(msg.hashCode());
						list.remove(msg);
						cache(CACHE_NAME, CACHE_KEY, list);
					} else {
						count.put(msg.hashCode(), maxCount + 1);
					}
				} else {
					count.put(msg.hashCode(), 1);
					list.add(msg);
					cache(CACHE_NAME, CACHE_KEY, list);
				}
			}
		}
	}
}
