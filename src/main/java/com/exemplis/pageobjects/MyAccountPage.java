package com.exemplis.pageobjects;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyAccountPage extends Link{
	private String expectedPageTitle = "My Project Details";

	public MyAccountPage(WebDriver driver) throws Exception{
		super(driver);
		
		//Wait for the page to load:
		wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("quote-name-input")));
		
		//Check if I am in the right page:
		String actualPageTitle = driver.getTitle();
		actualPageTitle.trim();
		System.out.println(" > " + actualPageTitle);
		assertEquals(expectedPageTitle, actualPageTitle);
	}
	
	public void renameProject() throws Exception{
		//wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("quote-name-input")));
				
		//Rename the test:
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		driver.findElement(By.id("quote-name-input")).sendKeys("Automated Test " + timestamp);
		
		//Wait for save button availability, then save the test:
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("button[class='button small editable-save'")).click();
	}
	
	public void downloadXML() throws Exception{
		// Downloads > 2020 Download (XML)
		driver.findElement(By.cssSelector("button[class='btn btn--primary js-downloads-menu'")).click();
		driver.findElement(By.cssSelector("a[class='quote export2020'")).click();
		
		//Let the XML download:
		Thread.sleep(1000);
	}
}