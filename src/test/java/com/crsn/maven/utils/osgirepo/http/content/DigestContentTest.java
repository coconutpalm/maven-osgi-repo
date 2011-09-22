package com.crsn.maven.utils.osgirepo.http.content;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.crsn.maven.utils.osgirepo.http.Content;
import com.crsn.maven.utils.osgirepo.http.StringContent;
import com.crsn.maven.utils.osgirepo.maven.MavenRepository;

public class DigestContentTest {

	@Test
	public void canCalculateMd5Digest() throws IOException {

		assertEquals("08c5926ca861023c1d2a36653fd88e2\r\n", createDigestOfType("MD5"));
	}

	@Test
	public void canCalculateSha1Mac() throws IOException {

		assertEquals("d869db7fe62fb07c25a0403ecaea5531744b5fb\r\n",
				createDigestOfType("SHA1"));
	}

	private static String createDigestOfType(String digestType)
			throws IOException, UnsupportedEncodingException {
		Content originalContent = createTestContent();
		DigestContent content = new DigestContent(digestType, originalContent);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		content.serializeContent(baos);
		String contentAsString = baos.toString("ASCII");
		return contentAsString;
	}

	private static Content createTestContent() {
		return new StringContent("whatever", "ASCII", "text/plain");
	}

}
