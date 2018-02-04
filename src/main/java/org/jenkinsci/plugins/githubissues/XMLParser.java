package org.jenkinsci.plugins.githubissues;

import java.io.File;
import java.io.PrintStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XMLParser {



	public TestResult parse(String inputFilePath, PrintStream logger) {
		TestResult parsedTestResult = null;
		
		try {	         
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			UserHandler userhandler = new UserHandler();
			File dir = new File(inputFilePath);
			saxParser.parse(dir, userhandler);
			parsedTestResult = userhandler.getTestResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return parsedTestResult;

	} 

	class UserHandler extends DefaultHandler {

		TestResult testResult;

		String testSuiteName = "";
		String fileName = "";
		int failureCount = 0;
		int errorCount = 0;
		String errorMessage = "";

		public TestResult getTestResult() {
			return testResult;
		}
		
		@Override
		public void startElement(
				String uri, String localName, String qName, Attributes attributes)
						throws SAXException {


			if (qName.equalsIgnoreCase("testsuite")) {
				testSuiteName = attributes.getValue("name");
				failureCount = Integer.parseInt(attributes.getValue("failures"));
				errorCount = Integer.parseInt(attributes.getValue("errors"));

			}
			else if(qName.equalsIgnoreCase("property") && attributes.getValue("name").equalsIgnoreCase("file")) {
				fileName = attributes.getValue("value");
			}
			else if (qName.equalsIgnoreCase("error")) {
				errorMessage = attributes.getValue("message");
			}

		}

		@Override
	    public void endElement(String uri, String localName, String qName) throws SAXException {
	        if (qName.equalsIgnoreCase("testsuites")) {
	            testResult = new TestResult(testSuiteName, fileName, failureCount, errorCount, errorMessage);
	        }
	    }

	}

}

