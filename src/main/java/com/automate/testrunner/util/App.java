package com.automate.testrunner.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class App {

	public static void main(final String[] args) {

		try {

			final File file = new File("C:/Users/Arun Kumar S A/Documents/SpringBootDemo/target/test-classes/");

			// convert the file to URL format
			final URL url = file.toURI().toURL();
			final URL[] urls = new URL[] { url };

			// load this folder into Class loader
			final ClassLoader cl = new URLClassLoader(urls);

			// load the Address class in 'c:\\other_classes\\'
			final Class cls = cl.loadClass("com.spring_boot.suite.CreateAndListTestSuite");

			// print the location from where this class was loaded
			final ProtectionDomain pDomain = cls.getProtectionDomain();
			final CodeSource cSource = pDomain.getCodeSource();
			final URL urlfrom = cSource.getLocation();
			System.out.println(urlfrom.getFile());

		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

}