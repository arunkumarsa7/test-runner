package com.automate.testrunner.config;

import java.io.File;
import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

@Configuration
@PropertySource("classpath:test-runner.properties")
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

	public String getTestIterationCluster() {
		return new StringBuilder(MessageFormat.format(testIterationName, testIterationNumber)).append(File.separator)
				.append(testIterationUser).toString();
	}

}
