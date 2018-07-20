package com.exemplis.pageobjects;

import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SelectPage extends Link {
	private String expectedPageTitle = "ChairBuilder";

	public SelectPage(WebDriver driver) {
		super(driver);
		
		//Check if I am in the right page:
		String actualPageTitle = driver.getTitle();
		System.out.print(" > " + actualPageTitle);
		assertEquals(expectedPageTitle, actualPageTitle);
	}
	
	public SeriesPage goToSeriesPage(String chair) throws Exception {
		//Define the wait time to be 20 seconds to look for web elements:
		wait = new WebDriverWait(driver, 20);
		
		//Look for the element:
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt='" + chair + "'")));
		driver.findElement(By.cssSelector("img[alt='" + chair + "'")).click();
		
		return new SeriesPage(driver);
	}
}