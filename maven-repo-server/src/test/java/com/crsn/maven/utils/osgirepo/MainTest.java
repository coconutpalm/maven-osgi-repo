package com.crsn.maven.utils.osgirepo;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.crsn.maven.utils.osgirepo.util.TestUtil;

public class MainTest {

	@Test
	public void testArgumentParsing() {
		File repoFile = TestUtil.getFileOfResource("mockrepo");
		Main arguments = Main.parseArguments(new String[]{"-p","31337","-r", repoFile.getAbsolutePath()}, new Main());
		assertEquals(31337,arguments.getPort());
		assertEquals(repoFile, arguments.getPluginRepository());
	}

}
