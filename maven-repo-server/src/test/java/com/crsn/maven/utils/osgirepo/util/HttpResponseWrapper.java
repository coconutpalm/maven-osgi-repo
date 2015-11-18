package com.crsn.maven.utils.osgirepo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpResponseWrapper {

	private final HttpResponse response;

	private HttpResponseWrapper(HttpResponse response) {
		this.response = response;

	}

	public static HttpResponseWrapper getResponse(String url) {
		DefaultHttpClient client = new DefaultHttpClient();

		HttpGet get = new HttpGet(url);

		try {
			return new HttpResponseWrapper(client.execute(get));
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public boolean isNotFound() {
		return getStatusCode() == 404;
	}

	public boolean isContent() {
		return getStatusCode() == 200;
	}

	public List<String> contentAsStringList(String encoding) {
		HttpEntity entity = response.getEntity();
		InputStream content = null;
		try {

			content = entity.getContent();
			return IOUtils.readLines(content, encoding);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (content != null) {
				try {
					content.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public int getStatusCode() {
		return response.getStatusLine().getStatusCode();
	}

}
