package com.crsn.maven.utils.osgirepo.http;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.DefaultedHttpContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.crsn.maven.utils.osgirepo.util.HttpResponseWrapper;

public class HttpServerTest {

	private final HttpServer server = new HttpServer();

	@Before
	public void setUp() {
		server.start();
	}

	@After
	public void tearDown() {
		server.stop();
	}
	

	@Test
	public void willAnswerWith404ToNotExistingPage()
			throws ClientProtocolException, IOException {
		String uri = server.getServerUrl() + "test";
		HttpResponseWrapper response = HttpResponseWrapper.getResponse(uri);
		assertTrue(response.isNotFound());
	}

	@Test
	public void willAnswerWithContent() {
		server.registerContent("/", new StringContent("testvalue", "UTF-8",
				"text/plain"));
		String uri = server.getServerUrl();
		HttpResponseWrapper response = HttpResponseWrapper.getResponse(uri);
		assertTrue(response.isContent());
		List<String> result = response.contentAsStringList("UTF-8");
		assertEquals(Arrays.asList("testvalue"), result);
	}

}
