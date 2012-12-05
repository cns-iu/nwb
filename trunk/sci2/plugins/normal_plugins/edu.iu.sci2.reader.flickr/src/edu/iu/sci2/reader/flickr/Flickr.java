package edu.iu.sci2.reader.flickr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Flickr {

	private String apiKey;
	private String method;
	private Map<String, String> parameters = new HashMap<String, String>();
	private String baseURL;

	public Flickr(String baseURL, String apiKey, String method,
			Map<String, String> parameters) {
		this.baseURL = baseURL;
		this.apiKey = apiKey;
		this.method = method;
		this.parameters = parameters;
	}

	public Map<String, String> getParameters() {
		if (parameters.keySet().isEmpty())
			throw new FlickrRuntimeException("Parameters not set");
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public void setApiKey(String newApiKey) {
		apiKey = newApiKey;
	}

	public String getApiKey() {
		if (apiKey == null)
			throw new FlickrRuntimeException("API Key not set");
		return apiKey;
	}

	public String getMethod() {
		if (method == null)
			throw new FlickrRuntimeException("Method not set");
		return method;
	}

	public void setMethod(String newMethod) {
		method = newMethod;
	}

	public void setBaseURL(String newBaseURL) {
		baseURL = newBaseURL;
	}

	public String getBaseURL() {
		if (baseURL == null)
			throw new FlickrRuntimeException("BaseURL not set");
		return baseURL;
	}

	public InputStream callMethod(int requestType) throws IOException {
		String urlString = getURL(requestType);
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		InputStream in = null;
		switch (requestType) {
		case 0:
			con.setRequestMethod("GET");
			con.setDoOutput(false);
			con.setDoInput(true);
			con.connect();
			in = con.getInputStream();
			break;
		case 1:
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.connect();
			OutputStream out = con.getOutputStream();
			String param = getParams();
			byte[] buff = param.getBytes("UTF8");
			out.write(buff);
			out.flush();
			out.close();
			in = con.getInputStream();
		}

		return in;

	}

	private String getURL(int requestType) {
		String requestURL = this.baseURL;
		if (requestType == 0) {
			requestURL = requestURL + "?" + getParams();
		}
		return requestURL;
	}

	private String getParams() {
		String requestParam = "";
		try {
			requestParam = URLEncoder.encode("method", "UTF-8") + "="
					+ URLEncoder.encode(getMethod(), "UTF-8") + "&";
			requestParam = requestParam+URLEncoder.encode("api_key", "UTF-8") + "="
					+ URLEncoder.encode(getApiKey(), "UTF-8") + "&";
			
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			throw new Error("Unsupported Encoding Exception", ex);
		}
		boolean first = true;
		for (Entry<String, String> entry : this.parameters.entrySet()) {
			try {
				if (first)
					first = false;
				else {
					requestParam = requestParam + "&";
				}
				requestParam = requestParam
						+ URLEncoder.encode((String) entry.getKey(), "UTF-8")
						+ "="
						+ URLEncoder.encode((String) entry.getValue(), "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
				throw new Error("Unsupported Encoding Exception", ex);
			}
		}
		return requestParam;
	}

}
