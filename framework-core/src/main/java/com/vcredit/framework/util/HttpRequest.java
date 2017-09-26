package com.vcredit.framework.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.util.ReflectionUtils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressWarnings({ "deprecation", "unused" })
public class HttpRequest {

	private static String defaultCharSet = "UTF-8";

	private static int TIMEOUT = 20;

	@SuppressWarnings("resource")
	public static String doJson(String url, String json, String charset, Header... header) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		StringEntity s = new StringEntity(json.toString(), charset);
		s.setContentEncoding(charset);
		s.setContentType("application/json;charset=UTF-8");// 发送json数据需要设置contentType
		for (Header h : header) {
			post.addHeader(h);
		}
		post.setEntity(s);
		HttpResponse res = client.execute(post);
		if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return EntityUtils.toString(res.getEntity(), charset);
		}
		throw new RuntimeException("http request result code:" + res.getStatusLine().getStatusCode());
	}

	@SuppressWarnings("resource")
	public static String doText(String url, String text, String charset) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		StringEntity s = new StringEntity(text, charset);
		s.setContentEncoding(charset);
		post.setEntity(s);
		HttpResponse res = client.execute(post);
		if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return EntityUtils.toString(res.getEntity(), charset);
		}
		throw new RuntimeException("http request result code:" + res.getStatusLine().getStatusCode());
	}

	public static String doText(String url, String text) throws Exception {
		return doText(url, text, defaultCharSet);
	}

	@SuppressWarnings("resource")
	public static String doPost(String url, String charset, String... param) throws Exception {
		if (param != null && param.length % 2 != 0)
			throw new Exception("param count excepiton");
		CloseableHttpResponse response = null;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		if (param != null)
			for (int i = 0; i < param.length; i = i + 2) {
				formparams.add(new BasicNameValuePair(param[i], param[i + 1]));
			}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);
		post.setEntity(new UrlEncodedFormEntity(formparams, charset));
		response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return EntityUtils.toString(response.getEntity());
		}
		throw new RuntimeException("http request result code:" + response.getStatusLine().getStatusCode());
	}

	public static String doPost(String url, String... param) throws Exception {
		return doPost(url, defaultCharSet, param);
	}

	@SuppressWarnings("resource")
	public static String doGet(String url, String charset, String... param) throws Exception {
		if (param != null && param.length % 2 != 0)
			throw new Exception("param count excepiton");
		CloseableHttpResponse response = null;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		if (param != null)
			for (int i = 0; i < param.length; i = i + 2) {
				BasicHttpParams httpParam = new BasicHttpParams();
				httpParam.setParameter(param[i], param[i + 1]);
				get.setParams(httpParam);
			}
		response = client.execute(get);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return EntityUtils.toString(response.getEntity());
		}
		throw new RuntimeException("http request result code:" + response.getStatusLine().getStatusCode());
	}

	@SuppressWarnings("resource")
	public static String doPost(String url, String charset, Object obj) throws Exception {
		CloseableHttpResponse response = null;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> formparams = postParam(obj);
		post.setEntity(new UrlEncodedFormEntity(formparams, charset));
		response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return EntityUtils.toString(response.getEntity());
		}
		throw new RuntimeException("http request result code:" + response.getStatusLine().getStatusCode());
	}

	public static String doPost(String url, Object obj) throws Exception {
		return doPost(url, defaultCharSet, obj);
	}

	@SuppressWarnings("resource")
	public static String doGet(String url, Object obj) throws Exception {
		CloseableHttpResponse response = null;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		if (obj != null)
			get.setParams(getParam(obj));
		response = client.execute(get);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return EntityUtils.toString(response.getEntity());
		}
		throw new RuntimeException("http request result code:" + response.getStatusLine().getStatusCode());
	}

	private static BasicHttpParams getParam(Object obj) {
		Field[] fields = obj.getClass().getFields();
		BasicHttpParams httpParam = new BasicHttpParams();
		for (Field f : fields) {
			Object result = ReflectionUtils.getField(f, obj);
			if (result != null)
				httpParam.setParameter(f.getName(), result);
		}
		return httpParam;
	}

	private static List<NameValuePair> postParam(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (Field f : fields) {
			ReflectionUtils.makeAccessible(f);
			Object result = ReflectionUtils.getField(f, obj);
			if (result != null)
				formparams.add(new BasicNameValuePair(f.getName(), String.valueOf(result)));
		}
		return formparams;
	}

	private static Builder okHttpClientBuilder = new OkHttpClient().newBuilder();

	public static String doJson(String url, Object obj) throws Exception {
		String json = JacksonUtil.Obj2Json(obj);
		return doJson(url, json);
	}

	public static String doJson(String url, String json) throws Exception {
		return doJson(url, json, TIMEOUT);
	}

	public static String doJson(String url, String json, Integer timeout) throws Exception {
		OkHttpClient okHttpClient = okHttpClientBuilder.readTimeout(timeout, TimeUnit.SECONDS).build();
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = okHttpClient.newCall(request).execute();
		if (response.isSuccessful()) {
			return response.body().string();
		} else {
			throw new IOException("Unexpected code " + response);
		}
	}

	public static String doGet(String url) throws Exception {
		OkHttpClient okHttpClient = okHttpClientBuilder.readTimeout(TIMEOUT, TimeUnit.SECONDS).build();
		Request request = new Request.Builder().url(url).build();
		Response response = okHttpClient.newCall(request).execute();
		if (response.isSuccessful()) {
			return response.body().string();
		} else {
			throw new IOException("Unexpected code " + response);
		}
	}

}
