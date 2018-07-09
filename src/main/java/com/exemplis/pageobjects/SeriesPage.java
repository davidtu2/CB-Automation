package com.exemplis.pageobjects;

import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeriesPage extends Link {
	private String expectedPageTitle = "ChairBuilder";

	public SeriesPage(WebDriver driver) {
		super(driver);
		
		String actualPageTitle = driver.getTitle();
		System.out.println("You are in: " + actualPageTitle);
		
		assertEquals(expectedPageTitle, actualPageTitle);
	}
	
	public ChairBuilder2 goToChairBuilder2(String chairSeries) throws Exception {
		//Let the page load prior to going to the next page
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt='" + chairSeries + "'")));
		driver.findElement(By.cssSelector("img[alt='" + chairSeries + "'")).click();
		
		return new ChairBuilder2(driver, chairSeries);
	}
}