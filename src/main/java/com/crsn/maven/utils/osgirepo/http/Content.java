package com.crsn.maven.utils.osgirepo.http;

import java.io.IOException;
import java.io.OutputStream;

public interface Content {

	String contentType();

	long contentLength();

	void serializeContent(OutputStream stream) throws IOException;

}
