package com.orangehrm.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentReportsManager;

public class DemoTest extends BaseClass{
	
	@Test
	public void testDemoTest() {
		ExtentReportsManager.logStep("Testing DemoPage Title");
		staticWait(25);
		String title = getDriver().getTitle();
		ExtentReportsManager.logStep("Page title is: " + title);
		Assert.assertEquals("OrangeHRM",title);
		ExtentReportsManager.logStepWithScreenshot(BaseClass.getDriver(), "Title Test", "Title match the expected - "+title);
	
	}

}
