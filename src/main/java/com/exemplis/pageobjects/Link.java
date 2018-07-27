package com.exemplis.pageobjects;

import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Link {
	protected WebDriver driver;
	protected WebDriverWait wait;
	
	//Values to modify, depending on how you want to set up the test:
	//Indicates whether or not you want to test in QA or PROD:
	protected boolean QA = false;
	
	//Indicates whether or not you want to test the filters in CB:
	protected boolean optionsTest = true;
	
	//Indicates whether or not you want to test the colors in CB:
	protected boolean colorsTest = true;
	
	//Indicates whether of not you want to test fabrics in CB:
	protected boolean fabricsTest = true;
	
	//Login
	private String username = "kpamittan";
	private String password = "exemplis";
	private String expectedName = "Kevin Pamittan";
	
	//Data file location for extraction. The file is located within the project's folder structure:
	protected String file = "src/main/resources/optionsAltAll.csv";
	
	//These indicate whether the fabric is Cal133 or COM (Customer Owned Material)
	protected boolean Cal133 = true;
	protected boolean COM = false;
	
	//Optional params for fabric search and filtering:
	protected String search = "Electric Blue";
	protected String fabricType = "Fabric";
	protected String color = "Blue";
	protected String patternType = "Solid";
	protected String manufacturer = "SitOnIt";
	protected String lead = "2 days";
	protected int[] gradeRange = {1, 1};

	public Link(WebDriver driver) {
		this.driver = driver;
	}
	
	//All of the pages seem to have the ability to login, so that is why this is here
	public void login() throws Exception{
		//Define the wait time to be 20 seconds to look for web elements:
		wait = new WebDriverWait(driver, 20);
		
		//Wait for the login button element to become available, then click on it:
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("login-link")));
		element.click();
		
		//Pass username and password
		String username = "";
		String password = "";
		//At times, the login form refreshes between the inputs of the username and password, resulting in an empty username field.
		//This is a workaround for that:
		while(!username.equals(this.username) || !password.equals(this.password)) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
			driver.findElement(By.name("username")).sendKeys(this.username);
			username = driver.findElement(By.name("username")).getAttribute("value");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
			driver.findElement(By.name("password")).sendKeys(this.password);
			password = driver.findElement(By.name("password")).getAttribute("value");
		}
		
		//Submit
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[class='button secondary submit-login-btn '")));
		element.click();
		
		//Verify that the user logged in is correct
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("dropdown")));
		String actualName = element.getText().trim();
		assertEquals(expectedName, actualName);
	}
}
