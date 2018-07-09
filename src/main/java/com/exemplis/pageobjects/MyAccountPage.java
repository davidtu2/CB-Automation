package com.exemplis.pageobjects;

import java.sql.Timestamp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyAccountPage extends Link{

	public MyAccountPage(WebDriver driver) {
		super(driver);
	}
	
	public void renameProject() throws Exception{
		//Let the page load prior to going to the next page (Let the chair graphics load)
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("quote-name-input")));
				
		//Rename the test and Save
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		driver.findElement(By.id("quote-name-input")).sendKeys("Automated Test " + timestamp);
		
		Thread.sleep(3000);//Need to wait for save button availability
		driver.findElement(By.cssSelector("button[class='button small editable-save'")).click();
	}
	
	public void downloadXML() throws Exception{
		// Click on Downloads > 2020 Download (XML)
		driver.findElement(By.cssSelector("button[class='btn btn--primary js-downloads-menu'")).click();
		driver.findElement(By.cssSelector("a[class='quote export2020'")).click();
		
		Thread.sleep(1000);//Let the XML download
	}
}