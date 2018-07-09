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
		
		String actualPageTitle = driver.getTitle();
		System.out.println("You are in: " + actualPageTitle);
		
		assertEquals(expectedPageTitle, actualPageTitle);
	}
	
	public SeriesPage goToSeriesPage(String selectedChair) throws Exception {
		//Let the page load prior to going to the next page
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt='" + selectedChair + "'")));
		driver.findElement(By.cssSelector("img[alt='" + selectedChair + "'")).click();
		
		return new SeriesPage(driver);
	}
}