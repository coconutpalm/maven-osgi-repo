package com.crsn.maven.utils.osgirepo.http.content;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.crsn.maven.utils.osgirepo.maven.builder.MavenRepositoryBuilder;

public class VelocityHtmlTemplateContentTest {

	@Test
	public void canCreateTemplateContent() throws IOException {
		MavenRepositoryBuilder builder=new MavenRepositoryBuilder();
		VelocityHtmlTemplateContent content=new VelocityHtmlTemplateContent("/test.vm", builder.build());
		ByteArrayOutputStream output=new ByteArrayOutputStream();
		content.serializeContent(output);
		output.close();
		assertTrue(output.toByteArray().length > 0);
	}

}
