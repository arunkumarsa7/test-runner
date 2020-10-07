package com.automate.testrunner.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import com.automate.testrunner.config.TestRunnerConfiguration;

@Configuration
public class TestSuitesLoader {

	@Autowired
	TestRunnerConfiguration getTestIterationCluster;

	@SuppressWarnings("rawtypes")
	private static List<Class> testSuitesToRun;

	@SuppressWarnings("rawtypes")
	private void loadClassFiles() {
		testSuitesToRun = new ArrayList<>();
		final File file = new File(getTestIterationCluster.getTestSuitesLocation());
		try (final URLClassLoader cl = new URLClassLoader(new URL[] { file.toURI().toURL() })) {
			final Class class1 = cl.loadClass(getTestIterationCluster.getTestIterationProjectSuite1());
			final Class class2 = cl.loadClass(getTestIterationCluster.getTestIterationProjectSuite1());
			testSuitesToRun.add(class1);
			testSuitesToRun.add(class2);
		} catch (final IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public List<Class> getTestSuitesToRun() {
		if (CollectionUtils.isEmpty(testSuitesToRun)) {
			loadClassFiles();
		}
		return testSuitesToRun;
	}

}
