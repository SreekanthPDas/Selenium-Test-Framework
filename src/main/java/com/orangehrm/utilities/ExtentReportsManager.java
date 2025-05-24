package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.orangehrm.base.BaseClass;

public class ExtentReportsManager {

	private static ExtentReports extentReports;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> driverMap = new HashMap<>();
	public static final Logger logger = BaseClass.logger;
	// Initialize Extent Report
	public static ExtentReports getReporter() {

		if (extentReports == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReports/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("OrangeHRM Report");
			spark.config().setTheme(Theme.DARK);
			spark.config().setDocumentTitle("ExtentReports");
			extentReports = new ExtentReports();
			// Set up Env variables
			extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
			extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
			extentReports.setSystemInfo("Username", System.getProperty("user.name"));
			extentReports.attachReporter(spark);
			logger.info("Spark report attached to extent reports");
		}
		return extentReports;
	}
	
	//Register driver
	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
		logger.info("Driver registered as: Thread ID: " +Thread.currentThread().getId()+" driver:"+driver.toString());
	}

	// Start Test
	public synchronized static ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}

	// Start Test
	public static void endTest() {
		getReporter().flush();
		logger.info("Extent reports flushed");
	}

	// Get current thread's Test
	public static ExtentTest getTest() {
		return test.get();
	}

	/// Method to get the name of the current Test
	public static String getTestName() {
		ExtentTest currentTest = getTest();
		if (currentTest != null) {
			return currentTest.getModel().getName();
		} else {
			return "No test is currently active for the Thread";
		}
	}

	// Log a Step
	public static void logStep(String logMessage) {
		getTest().info(logMessage);
	}
	// Log a Passed Step
		public static void logPassedStep(String logMessage) {
			getTest().pass(logMessage);
		}

	// Log step validation with Screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenshotMessage) {
		getTest().pass(logMessage);
		// Screenshot Method to be called
		attachScreenshot(driver,screenshotMessage );
	}

	// Log step validation with Screenshot
	public static void logFailure(WebDriver driver, String logMessage, String screenshotMessage) {
		getTest().fail(logMessage);
		// Screenshot Method to be called
		attachScreenshot(driver,screenshotMessage );
	}
	
	//Log Skip method
	public static void logSkip(String logMessage) {
		getTest().skip(logMessage);
	}
	
	//Take Screenshot with Date and time in fileName
	public static String takeScreenshot(WebDriver driver, String fileName) {
		TakesScreenshot ts = (TakesScreenshot)driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		//Format Date time
		String timeStamp = new SimpleDateFormat("yyyy-mm-dd_HHmmss").format(new Date());
		//Save in path
		String dest = System.getProperty("user.dir") + "/src/test/resources/screenshots/" + fileName.trim().replace(" ", "_") + timeStamp + ".png";
		File destFile = new File(dest);
		try {
			FileUtils.copyFile(src, destFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Convert Screenshot into Base64 for embedding in Extent Report
		String base64Format = convertToBase64(destFile);
		
		return base64Format;
	}
	
	//Convert Screenshot to Base64 format
	public static String convertToBase64(File screenshotFile) {
		String base64Format = "";
		//Read the file content into byte Array
		try {
			byte[] fileContent = FileUtils.readFileToByteArray(screenshotFile);
			//Convert byte array tp a Base64 String
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base64Format;
		
	}
	
	//Attach Screenshot to Report
	public synchronized static void attachScreenshot(WebDriver driver, String message) {
		try {
		String screenshotBase64 = takeScreenshot(driver,getTestName());
		getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
		}catch(Exception e){
			getTest().fail("Failed to attach report");
			e.printStackTrace();
			
		}
		
	}

}
