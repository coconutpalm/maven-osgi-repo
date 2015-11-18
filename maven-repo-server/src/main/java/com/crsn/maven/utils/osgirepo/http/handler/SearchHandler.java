package com.crsn.maven.utils.osgirepo.http.handler;

import java.io.IOException;
import java.io.PrintStream;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

public class SearchHandler implements RequestHandler {

	@Override
	public void handle(Request req, Response res) throws IOException {
		res.set("Content-Type", "text/plain");
		PrintStream stream = res.getPrintStream();
		stream.println("this is a test");
		stream.close();
	}
}
