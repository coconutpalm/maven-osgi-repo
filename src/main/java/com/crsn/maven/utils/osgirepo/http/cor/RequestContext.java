package com.crsn.maven.utils.osgirepo.http.cor;

import java.io.IOException;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

public interface RequestContext {
	public void process(Request req, Response res) throws IOException;
}
