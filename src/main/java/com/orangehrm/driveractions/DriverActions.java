package com.orangehrm.driveractions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentReportsManager;

public class DriverActions extends BaseClass {
	public static WebDriver driver;
	public static WebDriverWait wait;
	private static int EXPLICIT_WAIT_TIME;
	public static final Logger logger = BaseClass.logger;

	public DriverActions(WebDriver driver) {
		this.driver = driver;
		EXPLICIT_WAIT_TIME = Integer.parseInt(prop.getProperty("ExplicitWaitTime"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_TIME));
		logger.info("Driver initialized");
	}

	// Return Page Title
	public String getPageTitle() {
		return driver.getTitle();
	}

	// Wait until an element is displayed
	public void waitUntilDisplayed(WebElement webElement) {
		logger.info("Waiting for the element to be displayed: " + getElementDescription(webElement));
		wait.until(ExpectedConditions.visibilityOf(webElement));
	}

	// Wait until an element is clickable
	public void waitUntilClickable(WebElement webElement) {
		logger.info("Waiting for the element to be clickable: " + getElementDescription(webElement));
		wait = new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT_TIME));
		wait.until(ExpectedConditions.elementToBeClickable(webElement));
	}

	// Enter Text in a text box
	public void setTextBox(WebElement webElement, String value) {
		try {
			waitUntilDisplayed(webElement);
			webElement.clear();
			webElement.sendKeys(value);
			logger.info("'" + value + "'" + " - entered in the textBox - " + getElementDescription(webElement));
			ExtentReportsManager.logStep("'" + value + "'" + " - entered in the textBox - " + getElementDescription(webElement));
		} catch (Exception e) {
			logger.error("Unable to set Textbox " + e.getMessage());
			applyBoarder(webElement, "red");
			logger.info("Unable to set Textbox " + getElementDescription(webElement));
			ExtentReportsManager.logStep("Unable to set Textbox " + getElementDescription(webElement));
		}
	}

	// Click a webelement
	public void clickElement(WebElement webElement) {
		try {
			waitUntilClickable(webElement);
			applyBoarder(webElement, "green");
			webElement.click();

			logger.info("Clicked the button - " + getElementDescription(webElement));
			ExtentReportsManager.logStep("Clicked the button - " + getElementDescription(webElement));
		} catch (Exception e) {
			logger.error("Unable to click element " + e.getMessage());
			applyBoarder(webElement, "red");
			logger.info("Unable to click the button " + getElementDescription(webElement));
			ExtentReportsManager.logStep("Unable to click the button " + getElementDescription(webElement));
		}
	}

	// Compare text values
	public boolean compareText(WebElement webElement, String expectedText) {
		try {
			waitUntilDisplayed(webElement);
			String actualText = webElement.getText();
			if (actualText.equalsIgnoreCase(expectedText)) {
				logger.info("Expected Text - " + expectedText + " - matches the actual " + actualText);
				ExtentReportsManager.logStep("Expected Text - " + expectedText + " - matches the actual " + actualText);
				return true;
			} else {
				logger.info("Expected Text - " + expectedText + " - doesnot match the actual " + actualText);
				ExtentReportsManager
						.logStep("Expected Text - " + expectedText + " - doesnot match the actual " + actualText);
				return false;
			}
		} catch (Exception e) {
			logger.info("Unable to compare texts " + e.getMessage());
			return false;
		}
	}

	// Check if an element is displayed
	public boolean elementIsDisplayed(WebElement element) {
		try {
			waitUntilDisplayed(element);
			ExtentReportsManager.logStep("Element is displayed");
			return element.isDisplayed();
		} catch (Exception e) {
			logger.error("The element is not displayed " + e.getMessage());
			ExtentReportsManager.logFailure(driver, "elementIsDisplayed", "Element is not displayed");
			return false;
		}
	}

	// Scroll to view an element
	public void scrollToElement(WebElement element) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true)", element);
			ExtentReportsManager.logStep("Scrolled into the element");
		} catch (Exception e) {
			logger.error("Unable to scroll into the element " + e.getMessage());
		}
	}

	// waiting for page to load with timeout set up
	public void waitForPageTimeout(int timeOutInSec) {
		try {
			wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSec));
			wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState")
					.equals("complete"));
		} catch (Exception e) {
			logger.error("Page timed out before loading " + e.getMessage());
			ExtentReportsManager.logFailure(driver, "Page timedOut before loading", "Page timedOut before loading");
		}
	}

	// Get Text from an element
	public String getTestOnElement(WebElement element) {
		try {
			waitUntilDisplayed(element);
			return element.getText();
		} catch (Exception e) {
			logger.error("Unable to get Text on element " + e.getMessage());
			return null;
		}
	}

	// Utility method to boarder an element
	public void applyBoarder(WebElement element, String color) {
		// Apply the boarder
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].apply.boarder='3px solid " + color + "'", element);
			logger.info("Applied the boarder colour " + color + "to the element " + getElementDescription(element));
		} catch (Exception e) {
			logger.warn("Failed to apply boarder to element " + getElementDescription(element));
		}
	}

	// Method to get element description
	public String getElementDescription(WebElement element) {

		if (driver == null) {
			return "Driver is not initialized";
		} else if (element == null) {
			return "Element is null";
		}

		try {
			String name = element.getDomProperty("name");
			String placeholder = element.getDomProperty("placeholder");
			String id = element.getDomProperty("id");
			String text = element.getDomProperty("text");
			String className = element.getDomProperty("class");

			if (isNotEmpty(name)) {
				return "Element with name :" + name;
			} else if (isNotEmpty(id)) {
				return "Element with id :" + id;
			} else if (isNotEmpty(text)) {
				return "Element with text :" + text;
			} else if (isNotEmpty(className)) {
				return "Element with className :" + className;
			} else if (isNotEmpty(placeholder)) {
				return "Element with placeholder :" + placeholder;
			} else {
				return "Element located using " + element.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Unable to describe the element due to error";
		}

	}

	// Method to check if a text is not null and not empty
	public boolean isNotEmpty(String text) {
		if (text != null && !text.trim().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	// Get all options from a dropdown
	public List<String> getDropDownOptions(WebElement dropDown) {
		List<String> dropDownOptions = new ArrayList<>();
		try {

			Select select = new Select(dropDown);
			for (WebElement element : select.getOptions()) {
				dropDownOptions.add(element.getText());
			}

			logger.info("Fetched dropdown options for: " + getElementDescription(dropDown));

		} catch (Exception e) {

			logger.info("Unable to fetch dropdown options for: " + getElementDescription(dropDown));
		}
		return dropDownOptions;
	}

	// Select dropdwon value by Visible Text
	public void selectDropDownByVisibleText(WebElement element, String value) {
		try {
			new Select(element).selectByVisibleText(value);
			applyBoarder(element, "green");
			logger.info("Selected dropdown value: " + value);
		} catch (Exception e) {
			applyBoarder(element, "red");
			logger.info("Unable to select dropdown value: " + value);
		}
	}

	// Select dropdwon value by Index
	public void selectDropDownByIndex(WebElement element, int index) {
		try {
			new Select(element).selectByIndex(index);
			applyBoarder(element, "green");
			logger.info("Selected dropdown value by Index: " + index);
		} catch (Exception e) {
			applyBoarder(element, "red");
			logger.info("Unable to select dropdown value by Index: " + index);
		}
	}

	// Switch to frame
	public void switchToFrame(WebElement frameElement) {
		try {
			driver.switchTo().frame(frameElement);
			logger.info("Switched to frame:" + getElementDescription(frameElement));
		} catch (Exception e) {
			logger.error("Unable to switch to frame:" + getElementDescription(frameElement));
		}
	}

	// Accept alert
	public void acceptAlert() {
		try {
			driver.switchTo().alert().accept();
			logger.info("Alert accepted");
		} catch (Exception e) {
			logger.error("Unable to accept to alert");
		}
	}

	// Accept alert
	public void dismissAlert() {
		try {
			driver.switchTo().alert().dismiss();
			logger.info("Alert dismissed");
		} catch (Exception e) {
			logger.error("Unable to dismiss alert");
		}
	}

	// Accept alert
	public String getAlertText() {
		try {
			return driver.switchTo().alert().getText();

		} catch (Exception e) {
			logger.error("Unable to get alert text");
			return "";
		}
	}

	// Refresh page
	public void refreshPage() {
		try {
			driver.navigate().refresh();
			ExtentReportsManager.logStep("Page refreshed");
			logger.error("Page refreshed");

		} catch (Exception e) {
			logger.error("Unable to refresh page");
			ExtentReportsManager.logStep("Unable to refresh page");
		}
	}

	// Get current URL
	public String getCurrentPageURL() {
		try {
			String currentURL = driver.getCurrentUrl();
			logger.error("Current URL fetched as: " + currentURL);
			ExtentReportsManager.logStep("Current URL fetched as: " + currentURL);
			return currentURL;

		} catch (Exception e) {
			logger.error("Unable to fetch the current URL");
			ExtentReportsManager.logStep("Unable to fetch the current URL");
			return "";
		}
	}
	
	// Wait for page load to complete
		public void waitForPageLoadComplete(int waitTimeSeconds) {
			try {
				wait.withTimeout(Duration.ofSeconds(waitTimeSeconds)).until(
					      webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
				logger.info("Waited until the page is loaded completely");
				ExtentReportsManager.logStep("Waited until the page is loaded completely");
			} catch (Exception e) {
				logger.error("Unable to Wait until the page is loaded completely");
				ExtentReportsManager.logStep("Unable to Wait until the page is loaded completely");
			}
		}

//***********************Advanced WebElenet actions*********************************	
	// Move to element with Actions class
	public void moveToElement(WebElement element) {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).perform();
			logger.error("Moved to element: " + getElementDescription(element));
			ExtentReportsManager.logStep("Moved to element: " + getElementDescription(element));

		} catch (Exception e) {
			logger.error("Unable to move to element: " + getElementDescription(element));
			ExtentReportsManager.logStep("Unable to move to element: " + getElementDescription(element));
		}
	}

	// Drag and Drop an element
	public void dragAndDrop(WebElement source, WebElement destination) {
		try {
			Actions action = new Actions(driver);
			action.dragAndDrop(source, destination).perform();
			logger.error("Dragged element " + getElementDescription(source) + " to the element "
					+ getElementDescription(destination));
			ExtentReportsManager.logStep("Dragged element " + getElementDescription(source) + " to the element "
					+ getElementDescription(destination));

		} catch (Exception e) {
			logger.error("Unable to Drag element " + getElementDescription(source) + " to the element "
					+ getElementDescription(destination));
			ExtentReportsManager.logStep("Unable to Drag element " + getElementDescription(source) + " to the element "
					+ getElementDescription(destination));
		}
	}

	// Clear text
	public void clearText(WebElement element) {
		try {
			waitUntilDisplayed(element);
			element.clear();
			logger.error("Cleared the element: " + getElementDescription(element));
			ExtentReportsManager.logStep("Clared the element: " + getElementDescription(element));

		} catch (Exception e) {
			logger.error("Unable to clear the element: " + getElementDescription(element));
			ExtentReportsManager.logStep("Unable to clear the element: " + getElementDescription(element));
		}
	}

}
