package com.orangehrm.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentReportsManager;

public class DashBoardTest extends BaseClass{
	
	private LoginPage loginPage;
	private DashboardPage dashBoardPage;
	
	@BeforeMethod
	public void setUpPages() {
		loginPage = new LoginPage(getDriver());
		dashBoardPage = new DashboardPage(getDriver());
		loginPage.login("admin","admin123");
		
	}
	@Test
	public void verifyHeaderTest() {
		ExtentReportsManager.logStep("Testing Dashboard page header");
		String headerActual = dashBoardPage.getDashboardHeader();
		Assert.assertEquals(headerActual,"Dashboard","Actual header doesnt match the expected header");
	}
	
	@Test
	public void verifyUserTest() {
		ExtentReportsManager.logStep("Testing logged in suername");
		String userActual = dashBoardPage.getLoggedinUsername();
		Assert.assertEquals(userActual,"Test User","Actual user doesnt match the expected user");
	}
	
	

}
