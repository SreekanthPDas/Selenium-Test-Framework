package com.orangehrm.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.orangehrm.base.BaseClass;
import com.orangehrm.driveractions.DriverActions;

public class LoginPage{
	
	private WebDriver driver;
	private DriverActions driverActions;
	public static final Logger logger = BaseClass.logger;
	
    @FindBy(name="username")
    private WebElement userNameField;

    @FindBy(name= "password")
    private WebElement passWordField;

    @FindBy(xpath="//button[contains(@class,'login-button')]")
    private WebElement loginButton;
    
    @FindBy(xpath="//i[contains(@class,'content-icon')]/following-sibling::p")
    private WebElement invalidCredentialMessage;
    
    public LoginPage(WebDriver driver){
		
    	this.driverActions = BaseClass.getDriverActions(driver);
    	logger.info("Browser configured");
        PageFactory.initElements(driver, this);
        
    }
    
    public void login(String user, String pass){
               
    	driverActions.setTextBox(userNameField, user);
    	driverActions.setTextBox(passWordField, pass);
    	driverActions.clickElement(loginButton);
        
    }
    
    public boolean invalidloginMessage(String message) {
    	return driverActions.compareText(invalidCredentialMessage, message);
    }
    
    
}
