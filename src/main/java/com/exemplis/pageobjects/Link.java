package com.exemplis.pageobjects;

import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Link {
	protected WebDriver driver;
	
	//Values to modify, depending on what you want to test://TODO: Modify this so that it's an extract
	private String username = "";
	private String password = "";
	private String expectedName = "";

	public Link(WebDriver driver) {
		this.driver = driver;
	}
	
	//All of the pages seem to have the ability to login, so that is why this is here
	public void login() throws Exception{
		driver.findElement(By.className("login-link")).click();
		driver.findElement(By.name("username")).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button[class='button secondary submit-login-btn '")).click();
		
		//Let the login go through by waiting a bit
		WebDriverWait wait = new WebDriverWait(driver, 20);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("dropdown")));
		String actualName = element.getText().trim();
		assertEquals(expectedName, actualName);
	}
}
