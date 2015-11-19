package com.crsn.maven.utils.osgirepo.http.cor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

public class FilterChain {
	
	private final RequestContext firstContext;

	public FilterChain(RequestFilter... filters) {
		this(Arrays.asList(filters));
	}
	
	public FilterChain(List<RequestFilter> filters) {
		
		if (filters == null) {
			throw new NullPointerException("Null filters.");
		}
		
		if (filters.isEmpty()) {
			throw new IllegalArgumentException("Filters empty.");
		}
		
		ArrayList<RequestFilter> reverse=new ArrayList<RequestFilter>(filters);
		Collections.reverse(reverse);
		
		RequestContext prev=new LastContext();
		
		for (RequestFilter requestFilter : reverse) {
			prev=new RequestContextImpl(requestFilter, prev);
		}
		this.firstContext=prev;
	}
	
	
	public void process(Request req, Response res) throws IOException {
		this.firstContext.process(req, res);
	}
	
	
	private class RequestContextImpl implements RequestContext {
		
		private final RequestFilter filter;
		private final RequestContext nextContext;

		public RequestContextImpl(RequestFilter filter, RequestContext nextContext) {
			if (filter == null) {
				throw new NullPointerException("Null filter.");
			}
			this.filter = filter;
			this.nextContext = nextContext;			
		}

		@Override
		public void process(Request req, Response res) throws IOException {
			this.filter.filter(req,res,nextContext);			
		}
		
	}
	
	private static class LastContext implements RequestContext {

		@Override
		public void process(Request req, Response res) {			
		}
		
	}

}
