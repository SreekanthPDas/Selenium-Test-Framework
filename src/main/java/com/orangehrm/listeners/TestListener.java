package com.orangehrm.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentReportsManager;
import com.orangehrm.utilities.RetryAnalyzer;

public class TestListener implements ITestListener, IAnnotationTransformer{
	public static final Logger logger = BaseClass.logger;
	private static String testName;
	private ExtentReportsManager extentManager;
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	
	}
	//Triggered when the TestCase is started
	@Override
	public void onTestStart(ITestResult result) {
		// 
		testName = result.getMethod().getMethodName();
		//Start loggining in Extent Report
		//ExtentReportsManager.startTest(testName);
		ExtentReportsManager.startTest(testName);
		//ExtentReportsManager.logStep("Test started: "+testName);
		ExtentReportsManager.logStep("Test started: "+testName);
		logger.info("Test started: "+testName);
	}
	//Triggered when a Test is passed
	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		//ExtentReportsManager.logStepWithScreenshot(BaseClass.getDriver(),"Test Passed Successfully!","Test End: "+ testName + " - ✔ Test Passed");

		ExtentReportsManager.logStepWithScreenshot(BaseClass.getDriver(),"Test Passed Successfully!","Test End: "+ testName + " - ✔ Test Passed");logger.info("Test End: "+ testName + " - ✔ Test Passed");
	}
	//Triggered when a Test is failed
	@Override
	public void onTestFailure(ITestResult result) {
		// LOg failure including screenshots
		String failureMessage = result.getThrowable().getMessage();
		//ExtentReportsManager.logStep(failureMessage);
		ExtentReportsManager.logStep(failureMessage);
		//ExtentReportsManager.logFailure(BaseClass.getDriver(),"Test failed !","Test End: "+ testName + " - ❌ Test Failed");
		ExtentReportsManager.logFailure(BaseClass.getDriver(),"Test failed !","Test End: "+ testName + " - ❌ Test Failed");
		logger.info("Test End: "+ testName + " - ❌ Test Failed");
	}
	//Triggered when a Test is Skipped
	@Override
	public void onTestSkipped(ITestResult result) {
		// Log Test Skipped 
		//ExtentReportsManager.logSkip("Test Skipped: " + testName);
		ExtentReportsManager.logSkip("Test Skipped: " + testName);
		logger.info("Test Skipped: " + testName);
	}
	
	//Triggered when a Suite is started
	@Override
	public void onStart(ITestContext context) {
		// Initialize the Extent Reporter
		ExtentReportsManager.getReporter();
		extentManager= new ExtentReportsManager();
		logger.info("Test suite run has commenced");
	}
	//Triggered when a Suite is Finished
	@Override
	public void onFinish(ITestContext context) {
		// Flush the Report when the suite is completed 
		//ExtentReportsManager.endTest();
		ExtentReportsManager.endTest();
		logger.info("Test suite run has completed");
	}
	

}
