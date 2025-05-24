package com.orangehrm.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentReportsManager;

public class LoginTest extends BaseClass{
	
	private LoginPage loginPage;
	private DashboardPage dashBoardPage;
	
	@BeforeMethod
	public void setUpPages() {
		loginPage = new LoginPage(getDriver());
		dashBoardPage = new DashboardPage(getDriver());
		
	}
	@Test(dataProvider="validLoginData", dataProviderClass=DataProviders.class)
	public void verifyValidLogin(String username, String password) {
		ExtentReportsManager.logStep("Testing valid login");
		loginPage.login(username, password);
		staticWait(3);
		Assert.assertTrue(dashBoardPage.getPageHeader("Dashboard"));
		ExtentReportsManager.logStep("Logged in");
		ExtentReportsManager.logPassedStep("Testing Dashboard header");
	}
	
	@Test(dataProvider="invalidLoginData", dataProviderClass=DataProviders.class)
	public void verifyInvalidLogin(String username, String password) {
		ExtentReportsManager.logStep("Testing Invalid Login");
		loginPage.login(username, password);
		Assert.assertTrue(loginPage.invalidloginMessage("Invalid credentials"));
		staticWait(3);
		ExtentReportsManager.logStep("Login failed as expected");
		ExtentReportsManager.logPassedStep("Invalid credentials message displayed as expected");
	}

}
