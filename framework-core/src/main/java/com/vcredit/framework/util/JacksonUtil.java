package com.vcredit.framework.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {
	public static <T> T Json2Obj(String json, TypeReference<T> tr)
			throws JsonParseException, JsonMappingException, IOException {
		T obj = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		mapper.setSerializationInclusion(Include.NON_NULL);  
		obj = mapper.readValue(json, tr);
		return obj;
	}

	@SuppressWarnings({ "unchecked" })
	public static Map<String, Object> Json2Map(String json)
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = null;
		// write your code here
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		result = mapper.readValue(json, Map.class);
		return result;
	}

	/**
	 * @param obj
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String Obj2Json(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(obj);

		return json;
	}

	/**
	 * @param obj
	 * @return
	 * @throws JsonProcessingException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String keyVal2Json(Object... objs)
			throws JsonProcessingException {
		if (objs.length % 2 == 0) {
			ObjectMapper objectMapper = new ObjectMapper();
			Map map = new HashMap();
			for (int i = 0; i < objs.length; i = i + 2) {
				map.put(objs[i], objs[i + 1]);
			}
			String json = objectMapper.writeValueAsString(map);
			return json;
		}
		return "";
	}

	/**
	 * 
	 * @param param
	 * @return
	 * @throws JsonProcessingException 
	 */
	public static String array2Json(String... params) throws JsonProcessingException {
		if (params.length % 2 == 0) {
			Map<String, Object> map = new HashMap<String, Object>(
					params.length / 2);
			for (int i = 0; i < params.length; i++) {
				if (i % 2 == 0)
					map.put(params[i], params[i + 1]);
			}
			return JacksonUtil.Obj2Json(map);
		}
		return null;
	}
}
