package com.crsn.maven.utils.osgirepo.http.content;

import java.io.IOException;
import java.io.OutputStream;

import com.crsn.maven.utils.osgirepo.http.Content;

public class EmptyContent implements Content {

	@Override
	public String contentType() {
		return "text/plain";
	}

	@Override
	public long contentLength() {
		return 0;
	}

	@Override
	public void serializeContent(OutputStream stream) throws IOException {

	}

}
