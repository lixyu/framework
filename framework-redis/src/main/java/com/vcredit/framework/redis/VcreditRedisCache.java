package com.vcredit.framework.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.StringUtils;

import com.vcredit.framework.redis.RedisService;

public class VcreditRedisCache implements Cache {
	/**
	 * 缓存名称
	 */
	private String name = "default";

	@Autowired
	private RedisService redisService;

	/**
	 * 超时时间
	 */
	private long timeout;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		return this.redisService;
	}

	@Override
	public ValueWrapper get(Object key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		} else {
			final String finalKey;
			if (key instanceof String) {
				finalKey = (String) key;
			} else {
				finalKey = key.toString();
			}
			Object object = redisService.get(finalKey);
			return (object != null ? new SimpleValueWrapper(object) : null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		if (StringUtils.isEmpty(key) || null == type) {
			return null;
		} else {
			final String finalKey;
			final Class<T> finalType = type;
			if (key instanceof String) {
				finalKey = (String) key;
			} else {
				finalKey = key.toString();
			}
			Object object = redisService.get(finalKey);
			if (finalType != null && finalType.isInstance(object) && null != object) {
				return (T) object;
			} else {
				return null;
			}
		}
	}

	@Override
	public void put(Object key, Object value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return;
		} else {
			final String finalKey;
			if (key instanceof String) {
				finalKey = (String) key;
			} else {
				finalKey = key.toString();
			}
			if (!StringUtils.isEmpty(finalKey)) {
				redisService.put(finalKey, value, this.timeout);
			}
		}
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return null;
		} else {
			final String finalKey;
			if (key instanceof String) {
				finalKey = (String) key;
			} else {
				finalKey = key.toString();
			}
			if (!StringUtils.isEmpty(finalKey)) {
				ValueWrapper valueWrapper = this.get(finalKey);
				if (null == valueWrapper) {
					this.put(finalKey, value);
				}
				return new SimpleValueWrapper(value);
			} else {
				return null;
			}
		}
	}

	@Override
	public void evict(Object key) {
		if (null != key) {
			final String finalKey;
			if (key instanceof String) {
				finalKey = (String) key;
			} else {
				finalKey = key.toString();
			}
			if (!StringUtils.isEmpty(finalKey)) {
				redisService.del(finalKey);
			}
		}
	}

	@Override
	public void clear() {

	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
