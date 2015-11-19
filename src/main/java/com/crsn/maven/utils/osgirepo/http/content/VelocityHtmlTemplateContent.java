package com.crsn.maven.utils.osgirepo.http.content;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.crsn.maven.utils.osgirepo.http.Content;
import com.crsn.maven.utils.osgirepo.maven.MavenRepository;

public class VelocityHtmlTemplateContent implements Content {

	
	private final Template template;
	private final MavenRepository mavenRepo;

	public VelocityHtmlTemplateContent(String templatePath, MavenRepository mavenRepo) {
		if (templatePath == null) {
			throw new NullPointerException("Null template path.");
		}
		VelocityEngine engine=new VelocityEngine();
		engine.setProperty(Velocity.RESOURCE_LOADER, "class");
		engine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		engine.init();
		template = engine.getTemplate(templatePath);		
		

		if (mavenRepo == null) {
			throw new NullPointerException("Null Maven repo.");
		}
		this.mavenRepo = mavenRepo;

		
	}
	
	@Override
	public String contentType() {
		return "text/html; charset=utf-8";
	}

	@Override
	public long contentLength() {
		throw new RuntimeException("Not supported!");
	}

	@Override
	public void serializeContent(OutputStream stream) throws IOException {
		VelocityContext context=new VelocityContext();
		context.put("mavenRepo", mavenRepo);
		context.put("version", 0.2);
		OutputStreamWriter writer=new OutputStreamWriter(stream,"UTF-8");
		template.process();
		template.merge(context, writer);
		writer.flush();
	}

}
