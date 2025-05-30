package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.orangehrm.driveractions.DriverActions;
import com.orangehrm.utilities.ExtentReportsManager;
import com.orangehrm.utilities.LoggerManager;
import com.orangehrm.utilities.Utilities;

public class BaseClass {
	protected static Properties prop;
	//public WebDriver driver;
	//public static DriverActions driverActions;
	
	public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	public static ThreadLocal<DriverActions> driverActions = new ThreadLocal<>();
	public static final Logger logger = LoggerManager.getLoggers(BaseClass.class);
	
	@BeforeSuite
	public void loadConfigFile() {
		// Load properties file
		try {
			FileInputStream fis = new FileInputStream("src//main//resources//config.properties");
			prop = new Properties();
			prop.load(fis);
			logger.info("config.properties file loaded");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		//Start Extent Reporter - This has been implemented in TestListener
		//ExtentReportsManager.getReporter();
	}

	public synchronized void launchBrowser() {
		
		String browser = prop.getProperty("browser");
		
		if (browser.equalsIgnoreCase("Chrome")) {
			//driver = new ChromeDriver();
			driver.set(new ChromeDriver());
			logger.info("Chrome driver initialized");
			ExtentReportsManager.registerDriver(getDriver());
		} else if (browser.equalsIgnoreCase("FireFox")) {
			//driver = new FirefoxDriver();
			driver.set(new FirefoxDriver());
			logger.info("Firefox driver initialized");
			ExtentReportsManager.registerDriver(getDriver());
		} else if (browser.equalsIgnoreCase("Edge")) {
			//driver = new EdgeDriver();
			driver.set(new EdgeDriver());
			logger.info("Edge driver initialized");
			ExtentReportsManager.registerDriver(getDriver());
		} else {
			throw new IllegalArgumentException("Browser is not supported: " + browser);
		}
	}

	public void configureBrowser() {
		//Get Browser Name
		String url = prop.getProperty("URL");
		// Maximize browser window
		getDriver().manage().window().maximize();
		// ImplicitWait
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(prop.getProperty("ImplicitWaitTime"))));
		
		// Delete all cookies
		getDriver().manage().deleteAllCookies();
		logger.info("Browser configured");
		// Navigate to URL
		try {
			getDriver().get(url);
			//driverActions.get().waitForPageLoadComplete(30);
			// Page Load timeout defined
			getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Integer.parseInt(prop.getProperty("TimeOutTime"))));
			
			logger.info("Navigated to URL :" + url);
		}catch(Exception e) {
			logger.info("Unable to navigate to URL: " + e.getMessage());
		}
	}
	
	public synchronized static DriverActions getDriverActions(WebDriver driver) {
		if(driverActions.get() == null) {
			System.out.println("Action driver is not initialized");
			throw new IllegalStateException();
		}
		return driverActions.get();
	}
	
	public synchronized static WebDriver getDriver() {
		if(driver.get() == null) {
			logger.error("Web driver is not initialized");
			throw new IllegalStateException();
		}
		return driver.get();
	}

	@BeforeMethod
	public synchronized void setUp() {
		logger.info("Setting up WebDriver for: " + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(2);
		
		if(driverActions.get()==null) {
			driverActions.set(new DriverActions(getDriver()));
			logger.info("Driver Actions initialized - " + Thread.currentThread().getId());
		}
		
	}

	@AfterMethod(alwaysRun=true)
	public synchronized void tearDown() {
		if (driver != null) {
			try {
				getDriver().quit();
				
			}catch(Exception e) {
				logger.error("Unable to quit the browser " + e.getMessage());
			}
		}
		//ExtentReportsManager.endTest(); - This is implemented in TestListener
		//driver = null;
		//driverActions = null;
		driver.remove();
		driverActions.remove();
		
	}

	public void staticWait(int seconds) {
		LockSupport.parkNanos(seconds);
	}
	

}
