package com.crsn.maven.utils.osgirepo.http.handler;

import java.io.IOException;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

public interface RequestHandler {
	public void handle(Request req, Response res) throws IOException;
}
