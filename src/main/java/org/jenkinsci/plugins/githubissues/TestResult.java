package org.jenkinsci.plugins.githubissues;

public class TestResult {
	String testSuiteName;
	String fileName;
	int failureCount;
	int errorCount;
	String errorMessage;
	
	TestResult(String testSuiteName, String fileName, int failureCount, int errorCount, String errorMessage) {
		this.testSuiteName = (testSuiteName != null) ? testSuiteName: "";
		this.fileName = (fileName != null) ? fileName: "";
		this.failureCount = failureCount;
		this.errorCount = errorCount;
		this.errorMessage = (errorMessage != null) ? errorMessage: "";
	}
}
