package com.orangehrm.pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.orangehrm.base.BaseClass;
import com.orangehrm.driveractions.DriverActions;

public class DashboardPage{

	private WebDriver driver;
	private DriverActions driverActions;
	
	public DashboardPage(WebDriver driver){
		this.driverActions = BaseClass.getDriverActions(driver);
        PageFactory.initElements(driver, this);
        
    }
    @FindBy(xpath="//span[contains(@class,'oxd-topbar-header-breadcrumb')]//h6")
    private WebElement dashBoardHeader;

    @FindBy(xpath= "//input[contains(@class,'oxd-input oxd-input--focus')]")
    private WebElement searchField;

    @FindBy(xpath= "//a[contains(@href,'viewAdminModule')]")
    private WebElement adminTab;

    @FindBy(xpath= "//a[contains(@href,'pimModule')]")
    private WebElement pimTab;

    @FindBy(xpath= "//a[contains(@href,'leaveModule')]")
    private WebElement leaveTab;

    @FindBy(xpath= "//a[contains(@href,'timeModule')]")
    private WebElement timeTab;

    @FindBy(xpath= "//a[contains(@href,'recruitmentModule')]")
    private WebElement recruitmentTab;

    @FindBy(xpath= "//a[contains(@href,'mydetails')]")
    private WebElement myInfoTab;

    @FindBy(xpath= "//a[contains(@href,'dashboard')]")
    private WebElement dashboardTab;

    @FindBy(xpath= "//a[contains(@href,'directory')]")
    private WebElement directoryTab;

    @FindBy(xpath= "//a[contains(@href,'maintenance')]")
    private WebElement maintenanceTab;

    @FindBy(xpath= "//a[contains(@href,'claim')]")
    private WebElement claimTab;

    @FindBy(xpath= "//a[contains(@href,'buzz')]")
    private WebElement buzzTab;

    @FindBy(xpath= "//p[contains(@class,'drop')]")
    private WebElement loggedinUserName;

    public void clickAdminTab(){
    	driverActions.clickElement(adminTab);
    	        
    }
    
    public boolean getPageHeader(String expextedText){
        
    	return driverActions.compareText(dashBoardHeader, expextedText);
        
    }

    public String getLoggedinUsername(){
        return loggedinUserName.getText();
    }

    public String getDashboardHeader(){
        return driverActions.getTestOnElement(dashBoardHeader);
    }





}
