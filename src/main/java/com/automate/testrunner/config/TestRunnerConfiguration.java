package com.automate.testrunner.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class TestRunnerConfiguration {

	@Value("${test.iteration.user}")
	private String testIterationUser;

	@Value("${test.iteration.number}")
	private Integer testIterationNumber;

	@Value("${test.iteration.name}")
	private String testIterationName;

	@Value("${test.iteration.location}")
	private String testIterationLocation;

	@Value("${test.iteration.project.root}")
	private String testIterationProjectRoot;

	@Value("${test.iteration.project.test.suite.location}")
	private String testIterationProjectSuiteLocation;

	@Value("${test.iteration.project.test.suite1}")
	private String testIterationProjectSuite1;

	@Value("${test.iteration.project.test.suite2}")
	private String testIterationProjectSuite2;

	public String getTestIterationCluster() {
		return new StringBuilder(testIterationName).append(" ").append(testIterationNumber).append(File.separator)
				.append(testIterationUser).toString();
	}

	public String getTestSuitesLocation() {
		return new StringBuilder(testIterationProjectRoot).append(File.separator)
				.append(testIterationProjectSuiteLocation).append(File.separator).toString();
	}

	public String getTestRunnerLoggerLocation() {
		return new StringBuilder(testIterationLocation).append(File.separator).append(testIterationName).append(" ")
				.append(testIterationNumber).append(File.separator).append(testIterationUser).toString();
	}

}
