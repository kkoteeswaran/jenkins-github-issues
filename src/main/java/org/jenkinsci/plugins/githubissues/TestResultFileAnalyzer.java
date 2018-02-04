package org.jenkinsci.plugins.githubissues;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import hudson.FilePath;

public class TestResultFileAnalyzer {

	public ArrayList<TestResult> getFailedTests(FilePath workspace, String resultsDir, PrintStream logger) {
		
		ArrayList<TestResult> failedTests = new ArrayList<TestResult>();
		String resultDirPath = workspace.getRemote() + "/" + resultsDir;
		FilePath resultsDirectory = new FilePath( new File(resultDirPath));
		
		//Filter in only XML files
		List<FilePath> xmlResultFiles = null;
		try {
			xmlResultFiles = resultsDirectory.list(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().endsWith(".xml");
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(xmlResultFiles == null) {
			logger.format("[Github Issues Plugin]: No XML result files found in %s\n! Please check job configurations.", resultDirPath);
		}
		else if(!xmlResultFiles.isEmpty()) {
			for (FilePath eachXMLFile : xmlResultFiles) {
				XMLParser xmlParser = new XMLParser();
				TestResult testResult = xmlParser.parse(resultDirPath + "/" + eachXMLFile.getName(), logger);
				if(testResult.errorCount > 0 || testResult.failureCount > 0) {
					failedTests.add(testResult);
				}
			}
		}
		
		return failedTests;
		
	}
	
	class XMLFileFilter implements FilenameFilter {
		public boolean accept(File directory, String fileName) {
	        return fileName.endsWith(".xml");
	    }
	}
	
}