package com.crsn.maven.utils.osgirepo.http.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import com.crsn.maven.utils.osgirepo.http.cor.RequestContext;
import com.crsn.maven.utils.osgirepo.http.cor.RequestFilter;
import com.crsn.maven.utils.osgirepo.http.handler.RequestHandler;
import com.google.code.mjl.Log;
import com.google.code.mjl.LogFactory;

public class ControllerFilter implements RequestFilter {

	private static final Log log = LogFactory.getLog();

	private static final String COMMAND_EXTENSION = "do";
	private final Map<String, RequestHandler> handlers;

	public ControllerFilter(Map<String, RequestHandler> handlers) {
		this.handlers = handlers;
	}

	@Override
	public void filter(Request req, Response res, RequestContext context) throws IOException {
		Path path = req.getPath();
		String extension = path.getExtension();
		if (extension != null && extension.equals(COMMAND_EXTENSION)) {
			String name = path.getName();
			name=name.substring(0,name.length()-COMMAND_EXTENSION.length()-1);
			RequestHandler requestHandler = handlers.get(name);
			if (requestHandler != null) {
				requestHandler.handle(req, res);
			} else {
				log.warn("could not find handler for command '%s'", name);
				context.process(req, res);
			}
		} else {
			context.process(req, res);
		}
	}

}
