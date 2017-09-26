package com.vcredit.framework.ecache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class EcacheUtil {
	private static CacheManager manager = CacheManager.create(EcacheUtil.class.getResource("/ecache.xml")); 
	public static void cache(String cacheName,String key,Object value){
		 
		Ehcache cache = manager.getEhcache(cacheName);
		cache.put(new Element(key, value));
	}
	
	public static Object get(String cacheName,String key){
		Ehcache cache = manager.getEhcache(cacheName);
		Element e = cache.get(key);
		if(e!=null)
			return cache.get(key).getObjectValue();
		return null;
	}
	
	public static boolean del(String cacheName,String key){
		Ehcache cache = manager.getEhcache(cacheName);
		return cache.remove(key);
	}
}
