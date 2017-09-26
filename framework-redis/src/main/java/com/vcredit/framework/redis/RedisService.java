package com.vcredit.framework.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public class RedisService {

	protected RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private VcreditRedisSerializer redisSerializer;

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@SuppressWarnings("rawtypes")
	protected RedisSerializer getRedisSerializer() {
		return redisSerializer;

	}

	public boolean put(final String key, final Object val, final long time, final long token)
			throws RedisTokenException {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				if (validToken(key, token)) {
					return put(key, val, time);
				}
				throw new RedisTokenException("token validate exception");
			}
		});
	}

	public boolean put(final String key, final Object val, final long time) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@SuppressWarnings("unchecked")
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] keyb = getRedisSerializer().serialize(key);
				byte[] valb = getRedisSerializer().serialize(val);
				boolean flag = connection.setNX(keyb, valb);
				if (time > 0 && flag) {
					connection.expire(keyb, time);
				}
				return flag;
			}
		});
	}

	public Object get(final String key, final long token) throws RedisTokenException {
		return redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				if (validToken(key, token)) {
					return get(key);
				}
				throw new RedisTokenException("token validate exception");
			}
		});
	}

	public Object get(final String key) {
		return redisTemplate.execute(new RedisCallback<Object>() {
			@SuppressWarnings("unchecked")
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] keyb = getRedisSerializer().serialize(key);
				byte[] valb = connection.get(keyb);
				if (valb != null && valb.length > 0)
					return getRedisSerializer().deserialize(valb);
				return null;
			}
		});
	}

	public boolean del(final String key, final long token) throws RedisTokenException {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				if (validToken(key, token)) {
					return del(key);
				}
				throw new RedisTokenException("token validate exception");
			}
		});
	}

	public boolean del(final String key) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@SuppressWarnings("unchecked")
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] keyb = getRedisSerializer().serialize(key);
				if (connection.del(keyb) > 0)
					return true;
				return false;
			}
		});
	}

	private boolean validToken(final String key, final long token) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@SuppressWarnings("unchecked")
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] keyb = getRedisSerializer().serialize(getLockName(key));
				byte[] valb = connection.get(keyb);
				if (valb == null || valb.length == 0)
					return false;
				System.out.println(token + "---" + (Long) getRedisSerializer().deserialize(valb));
				return ((Long) getRedisSerializer().deserialize(valb)).equals(token);
			}
		});
	}

	public boolean unToken(final String key, final long token) throws RedisTokenException {
		if (validToken(key, token))
			return del(getLockName(key));
		throw new RedisTokenException("token validate exception");
	}

	@SuppressWarnings("static-access")
	public long getToken(final String key) {
		while (true) {
			Long rediToken = null;
			Object obj = get(getLockName(key));
			if (obj == null) {
				rediToken = System.currentTimeMillis();
				if (put(getLockName(key), rediToken, 60)) {
					return rediToken;
				} else {
					continue;
				}
			}
			try {
				new Thread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private String getLockName(String key) {
		return key + ".lock";
	}

}
