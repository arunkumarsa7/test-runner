package com.automate.testrunner.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.automate.testrunner.TestRunnerApplication;
import com.automate.testrunner.config.TestRunnerConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestRunnerApplication.class)
public class TestRunnerUtil {

	@Autowired
	TestRunnerConfiguration getTestIterationCluster;

	@Autowired
	TestSuitesLoader suitesLoader;

	@Test
	public void testEinarbeitungAntrag1() {
		try {
			final File iterationFolder = new File(getTestIterationCluster.getTestRunnerLoggerLocation());
			iterationFolder.mkdirs();
			final Counter counter = new Counter(iterationFolder);
			final Logger lLogger = new Logger(iterationFolder);

			final List<Class> junitTestSuites = suitesLoader.getTestSuitesToRun();

			final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
			LocalDateTime now = null;

			int index = 0;
			while ((index = counter.getNextIndex()) < junitTestSuites.size()) {

				final Class junitTestSuite = junitTestSuites.get(index);

				now = LocalDateTime.now();

				lLogger.write("// " + junitTestSuite.getName() + " suite started.:" + dtf.format(now) + "\n");
				lLogger.flush();
				final Result result = JUnitCore.runClasses(junitTestSuite);

				final List<Failure> failures = result.getFailures();
				for (final Failure lFailure : failures) {
					lLogger.write(lFailure.getDescription().getTestClass().getName() + ".class,\n");
				}

				lLogger.write("// " + junitTestSuite.getName() + " suite completed.");
				lLogger.write(" RunCount " + result.getRunCount());
				lLogger.write(" FailureCount " + result.getFailureCount());
				lLogger.write(" IgnoreCount " + result.getIgnoreCount());
				lLogger.write(" RunTime " + result.getRunTime());
				lLogger.newLine();
				lLogger.flush();
			}
			lLogger.close();

		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

}

class Logger {

	private final List<BufferedWriter> mWriters;

	/**
	 * Konstruktor with only local log file
	 *
	 * @throws IOException
	 */
	public Logger() throws IOException {
		this.mWriters = new ArrayList<>(2);
		final File localLogFile = new File("failedtest.txt");
		localLogFile.createNewFile();
		final FileWriter localFileWriter = new FileWriter(localLogFile.getAbsoluteFile());
		final BufferedWriter localWriter = new BufferedWriter(localFileWriter);
		this.mWriters.add(localWriter);
	}

	/**
	 * Konstruktor with local and remote log file
	 *
	 * @param pIterationFolder
	 * @throws IOException
	 */
	public Logger(final File pIterationFolder) throws IOException {
		this();
		try {
			final String userName = System.getProperty("user.name");
			final File remoteLogFile = new File(pIterationFolder, userName + "_failedtest.txt");
			remoteLogFile.createNewFile();
			final FileWriter remoteFileWriter = new FileWriter(remoteLogFile.getAbsoluteFile());
			final BufferedWriter remoteWriter = new BufferedWriter(remoteFileWriter);
			this.mWriters.add(remoteWriter);
		} catch (final Exception lException) {
			lException.printStackTrace();
		}
	}

	public void write(final String pString) throws IOException {
		for (final BufferedWriter lBufferedWriter : mWriters) {
			lBufferedWriter.write(pString);
		}
	}

	public void close() throws IOException {
		for (final BufferedWriter lBufferedWriter : mWriters) {
			lBufferedWriter.close();
		}
	}

	public void newLine() throws IOException {
		for (final BufferedWriter lBufferedWriter : mWriters) {
			lBufferedWriter.newLine();
		}
	}

	public void flush() throws IOException {
		for (final BufferedWriter lBufferedWriter : mWriters) {
			lBufferedWriter.flush();
		}
	}

}

class Counter {

	private final File counterFile;

	public Counter(final File pIterationFolder) throws IOException {
		this.counterFile = new File(pIterationFolder, "counter");
		if (!this.counterFile.exists()) {
			this.counterFile.createNewFile();
		}
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public int getNextIndex() throws IOException {
		int readIndex = 0;
		int index = 0;
		int writeIndex = 0;
		while (true) {
			index = read();
			writeIndex = ++index;
			write(writeIndex);
			readIndex = read();
			if (writeIndex == readIndex) {
				break;
			}
		}
		return index;
	}

	/**
	 * @param pI
	 * @throws IOException
	 */
	private void write(final int pI) throws IOException {
		try (final FileOutputStream mFileOutputStream = new FileOutputStream(counterFile)) {
			mFileOutputStream.write(pI);
		}
	}

	/**
	 * @return
	 * @throws IOException
	 */
	private int read() throws IOException {
		try (final FileInputStream lFileInputStream = new FileInputStream(counterFile)) {
			return lFileInputStream.read();
		}
	}

}