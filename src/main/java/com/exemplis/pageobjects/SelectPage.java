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
	
	public SeriesPage goToSeriesPage(String[] crumb) throws Exception {
		//Define the wait time to be 20 seconds to look for web elements:
		wait = new WebDriverWait(driver, 20);
		
		//At this point, I am in the last selection page and now I want to go to the series page,
		//so I just need to process the second-from-the-last element of the crumb
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt='" + crumb[crumb.length - 2] + "'")));
		driver.findElement(By.cssSelector("img[alt='" + crumb[crumb.length - 2] + "'")).click();
		
		return new SeriesPage(driver);
	}
}