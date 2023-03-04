package com.video.platform.service.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Http request tool class
 */
public class HttpUtil {

	private static final String CONTENT_TYPE_JSON = "application/json";

	private static final String CONTENT_TYPE_URL_ENCODED = "application/x-www-form-urlencoded";

	private static final String REQUEST_METHOD_POST = "POST";

	private static final String REQUEST_METHOD_GET = "GET";

	private static final Integer CONNECT_TIME_OUT = 120000;

	/**
	 * http get request
	 * @param url
	 * @param params
	 */
	public static HttpResponse get(String url, Map<String, Object> params) throws Exception {
		String getUrl = buildGetRequestParams(url, params);
		URL urlObj = new URL(getUrl);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setDoInput(true);
		con.setRequestMethod(REQUEST_METHOD_GET);
		con.setConnectTimeout(CONNECT_TIME_OUT);
		con.connect();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
		int responseCode = con.getResponseCode();
		String response = writeResponse(responseCode,br);
		br.close();
		String cookie = con.getHeaderField("Set-Cookie");
		con.disconnect();
		return new HttpResponse(responseCode,response, cookie);
	}

	/**
	 * http get request  can configure header
	 * @param url
	 * @param params
	 */
	public static HttpResponse get(String url,
								   Map<String, Object> params,
								   Map<String, Object> headers) throws Exception {
		String getUrl = buildGetRequestParams(url, params);
		URL urlObj = new URL(getUrl);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setDoInput(true);
		con.setRequestMethod(REQUEST_METHOD_GET);
		con.setRequestProperty("Content-Type", CONTENT_TYPE_JSON);
		con.setConnectTimeout(CONNECT_TIME_OUT);
		//set request header
		for(Entry<String, Object> entry : headers.entrySet()) {
			String key = entry.getKey();
			String value = String.valueOf(entry.getValue());
			con.setRequestProperty(key, value);
		}
		con.connect();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
		int responseCode = con.getResponseCode();
		String response = writeResponse(responseCode,br);
		br.close();
		String cookie = con.getHeaderField("Set-Cookie");
		con.disconnect();
		return new HttpResponse(responseCode,response, cookie);
	}

	/**
	 * http get request return output stream
	 * @param url
	 * @param headers
	 * @param response
	 */
	public static OutputStream get(String url,
								   Map<String, Object> headers,
								   HttpServletResponse response) throws Exception {
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setDoInput(true);
		con.setRequestMethod(REQUEST_METHOD_GET);
		con.setConnectTimeout(CONNECT_TIME_OUT);
		for(Entry<String, Object> entry : headers.entrySet()) {
			String key = entry.getKey();
			String value = String.valueOf(entry.getValue());
			con.setRequestProperty(key, value);
		}
		con.connect();
		BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
		OutputStream os = response.getOutputStream();
		int responseCode = con.getResponseCode();
		byte[] buffer = new byte[1024];
		if(responseCode >=200 && responseCode <300) {
			int i = bis.read(buffer);
			while (( i != -1)) {
				os.write(buffer,0,i);
				i = bis.read(buffer);
			}
			bis.close();
		}
		bis.close();
		con.disconnect();
		return os;
	}

	/**
	 * http post，request parameters use map to encapsulation
	 * @param url
	 * @param params
	 */
	public static HttpResponse postJson(String url, Map<String, Object> params) throws Exception {
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", CONTENT_TYPE_JSON);
		con.setRequestMethod(REQUEST_METHOD_POST);
		con.setConnectTimeout(CONNECT_TIME_OUT);
		String json = JSONObject.toJSONString(params);
		con.connect();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
		outputStreamWriter.write(json);
		outputStreamWriter.flush();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
		int responseCode = con.getResponseCode();
		String response = writeResponse(responseCode,br);
		outputStreamWriter.close();
		br.close();
		String cookie = con.getHeaderField("Set-Cookie");
		con.disconnect();
		return new HttpResponse(responseCode,response, cookie);
	}

	/**
	 * http post Request parameters use json string
	 * @param url
	 */
	public static HttpResponse postJson(String url, String json) throws Exception {
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", CONTENT_TYPE_JSON);
		con.setRequestMethod(REQUEST_METHOD_POST);
		con.setConnectTimeout(CONNECT_TIME_OUT);
		con.connect();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
		outputStreamWriter.write(json);
		outputStreamWriter.flush();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
		int responseCode = con.getResponseCode();
		String response = writeResponse(responseCode,br);
		outputStreamWriter.close();
		br.close();
		String cookie = con.getHeaderField("Set-Cookie");
		con.disconnect();
		return new HttpResponse(responseCode,response, cookie);
	}

	/**
	 * http post，Request parameters use json string, can configure the request header
	 * @param url
	 * @param params
	 */
	public static HttpResponse postJson(String url,
										Map<String, Object> params,
										Map<String, Object> headers) throws Exception {
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", CONTENT_TYPE_JSON);
		//set request header
		for(Entry<String, Object> entry : headers.entrySet()) {
			String key = entry.getKey();
			String value = String.valueOf(entry.getValue());
			con.setRequestProperty(key, value);
		}
		con.setRequestMethod(REQUEST_METHOD_POST);
		con.setConnectTimeout(CONNECT_TIME_OUT);
		String json = JSONObject.toJSONString(params);
		con.connect();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
		outputStreamWriter.write(json);
		outputStreamWriter.flush();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
		int responseCode = con.getResponseCode();
		String response = writeResponse(responseCode,br);
		outputStreamWriter.close();
		br.close();
		String cookie = con.getHeaderField("Set-Cookie");
		con.disconnect();
		return new HttpResponse(responseCode,response, cookie);
	}

	/**
	 * http post  form of the request parameter is url-encoded
	 * @param url
	 * @param params
	 */
	public static HttpResponse postUrlEncoded(String url,
											  Map<String, Object> params) throws Exception {
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", CONTENT_TYPE_URL_ENCODED);
		con.setRequestMethod(REQUEST_METHOD_POST);
		con.setConnectTimeout(CONNECT_TIME_OUT);
		con.connect();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
		String postParam = buildPostFormOrUrlEncodedParams(params);
		outputStreamWriter.write(postParam);
		outputStreamWriter.flush();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
		int responseCode = con.getResponseCode();
		String response = writeResponse(responseCode,br);
		outputStreamWriter.close();
		br.close();
		String cookie = con.getHeaderField("Set-Cookie");
		con.disconnect();
		return new HttpResponse(responseCode,response, cookie);
	}

	/**
	 * http post  form of the request parameter is form-data
	 * @param url
	 * @param params
	 */
	public static HttpResponse postFormData(String url,
											Map<String, Object> params) throws Exception {
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestMethod(REQUEST_METHOD_POST);
		con.setConnectTimeout(CONNECT_TIME_OUT);
		con.connect();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
		String postParam = buildPostFormOrUrlEncodedParams(params);
		outputStreamWriter.write(postParam);
		outputStreamWriter.flush();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
		int responseCode = con.getResponseCode();
		String response = writeResponse(responseCode,br);
		outputStreamWriter.close();
		br.close();
		String cookie = con.getHeaderField("Set-Cookie");
		con.disconnect();
		return new HttpResponse(responseCode,response, cookie);
	}

	/**
	 * http post  form of the request parameter is form-data, and the request header can be set
	 * @param url
	 * @param params
	 */
	public static HttpResponse postFormData(String url,
											Map<String, Object> params,
											Map<String, Object> headers) throws Exception {
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestMethod(REQUEST_METHOD_POST);
		con.setConnectTimeout(CONNECT_TIME_OUT);
		con.setRequestProperty("Content-Type", CONTENT_TYPE_URL_ENCODED);
		//set request header
		for(Entry<String, Object> entry : headers.entrySet()) {
			String key = entry.getKey();
			String value = String.valueOf(entry.getValue());
			con.setRequestProperty(key, value);
		}
		con.connect();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
		String postParam = buildPostFormOrUrlEncodedParams(params);
		outputStreamWriter.write(postParam);
		outputStreamWriter.flush();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
		int responseCode = con.getResponseCode();
		String response = writeResponse(responseCode,br);
		outputStreamWriter.close();
		br.close();
		String cookie = con.getHeaderField("Set-Cookie");
		con.disconnect();
		return new HttpResponse(responseCode,response, cookie);
	}

	private static String buildPostFormOrUrlEncodedParams(Map<String, Object> params) throws Exception {
		StringBuilder postParamBuilder = new StringBuilder();
		if(params != null && !params.isEmpty()) {
			for (Entry<String, Object> entry : params.entrySet()) {
				if(entry.getValue() == null) {
					continue;
				}
				String value = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
				postParamBuilder.append(entry.getKey()).append("=").append(value).append("&");
			}
			postParamBuilder.deleteCharAt(postParamBuilder.length() - 1);
		}
		return postParamBuilder.toString();
	}


	private static String buildGetRequestParams(String url, Map<String, Object> params) throws Exception {
		StringBuilder sb = new StringBuilder(url);
		if(params != null && !params.isEmpty()) {
			sb.append("?");
			for (Entry<String, Object> entry : params.entrySet()) {
				if(entry.getValue() == null) {
					continue;
				}
				String value = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
				sb.append(entry.getKey()).append("=").append(value).append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}


	private static String writeResponse(int responseCode, BufferedReader br) throws Exception {
		StringBuilder responseSb = new StringBuilder();
		String response;
		if(responseCode >=200 && responseCode <300) {
			String line;
			while ((line = br.readLine()) != null) {
				responseSb.append(line).append("\n");
			}
			response = responseSb.toString();
			br.close();
		}else {
			response = responseSb.toString();
		}
		return response;
	}

	public static class HttpResponse {

		private int statusCode;

		private String body;

		private String cookie;

		public String getCookie() {
			return cookie;
		}

		public void setCookie(String cookie) {
			this.cookie = cookie;
		}

		public HttpResponse(int statusCode, String body){
			this.statusCode = statusCode;
			this.body = body;
		}

		public HttpResponse(int statusCode, String body, String cookie){
			this.statusCode = statusCode;
			this.body = body;
			this.cookie = cookie;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public boolean isSuccess(){
			return this.statusCode >= 200 && this.statusCode < 300;
		}

		@Override
		public String toString() {
			return "{\n\tstatusCode:" + statusCode + ",\n\tbody:" + body + "}";
		}

	}
}
