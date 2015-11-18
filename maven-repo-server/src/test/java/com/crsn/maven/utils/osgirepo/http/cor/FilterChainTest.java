package com.crsn.maven.utils.osgirepo.http.cor;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

public class FilterChainTest {

	@Test
	public void canChainOne() throws IOException {
		TestFilter filter = new TestFilter();
		FilterChain chain=new FilterChain(filter);
		chain.process(null, null);
		assertTrue(filter.isCalled());
	}
	
	@Test
	public void canChainTwo() throws IOException {
		TestFilter firstFilter = new TestFilter();
		TestFilter secondFilter = new TestFilter();
		FilterChain chain=new FilterChain(firstFilter,secondFilter);
		chain.process(null, null);
		assertTrue(firstFilter.isCalled());
		assertTrue(secondFilter.isCalled());
	}
	
	
	private static class TestFilter implements RequestFilter {

		private boolean called=false;
		
	
		public boolean isCalled() {
			return called;
		}


		@Override
		public void filter(Request req, Response res, RequestContext context) throws IOException {
			this.called=true;
			context.process(req, res);
		}
		
	}

}
