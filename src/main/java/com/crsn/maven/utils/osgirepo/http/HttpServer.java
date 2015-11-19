package com.crsn.maven.utils.osgirepo.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import com.crsn.maven.utils.osgirepo.http.cor.FilterChain;
import com.crsn.maven.utils.osgirepo.http.filter.ContentFilter;
import com.crsn.maven.utils.osgirepo.http.filter.ControllerFilter;
import com.crsn.maven.utils.osgirepo.http.filter.NotFoundFilter;
import com.crsn.maven.utils.osgirepo.http.filter.ServerVersionFilter;
import com.crsn.maven.utils.osgirepo.http.handler.RequestHandler;
import com.crsn.maven.utils.osgirepo.http.handler.SearchHandler;
import com.google.code.mjl.Log;
import com.google.code.mjl.LogFactory;

public class HttpServer implements Container {

	private static final Log log = LogFactory.getLog();

	private enum State {
		NOT_RUNNING, RUNNING;
	}

	private ConcurrentHashMap<String, Content> contents = new ConcurrentHashMap<String, Content>();

	private State currentState = State.NOT_RUNNING;

	private final int port;

	private Connection connection;

	private InetSocketAddress listeningOn;

	private final ContentFilter contentFilter = new ContentFilter();
	private final Map<String, RequestHandler> handlers = new HashMap<String, RequestHandler>();
	{
		handlers.put("search", new SearchHandler());
	}

	private final FilterChain chain = new FilterChain(new ServerVersionFilter(), contentFilter, new ControllerFilter(
			handlers), new NotFoundFilter());

	public HttpServer() {
		this.port = 0;
	}

	public HttpServer(int port) {
		this.port = port;

	}

	public String getServerUrl() {
		checkState(State.RUNNING);
		try {
			return String.format("http://%s:%d/", InetAddress.getLocalHost().getHostName(), listeningOn.getPort());
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}

	}

	public void start() {
		try {
			connection = new SocketConnection(this);
			listeningOn = (InetSocketAddress) connection.connect(new InetSocketAddress(port));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		switchToState(State.RUNNING);
	}

	private void switchToState(State newState) {
		this.currentState = newState;

	}

	public void stop() {
		checkState(State.RUNNING);
		try {
			this.connection.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		switchToState(State.NOT_RUNNING);

	}

	private void checkState(State expected) {
		if (currentState != expected) {
			throw new IllegalStateException(String.format("Server in illegal state %s, expected %s.", currentState,
					expected));
		}
	}

	public void registerContent(String path, Content content) {
		contentFilter.registerContent(path, content);
	}

	public static void main(String[] args) throws IOException {
		Container container = new HttpServer();
		Connection connection = new SocketConnection(container);
		SocketAddress address = new InetSocketAddress(8080);
		connection.connect(address);

	}

	@Override
	public void handle(Request req, Response res) {

		try {
			chain.process(req, res);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
