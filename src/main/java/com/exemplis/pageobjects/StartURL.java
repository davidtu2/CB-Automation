package com.exemplis.pageobjects;

import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StartURL extends Link {
	private String baseURL;
	private String expectedPageTitle = "ChairBuilder";
	
	public StartURL(WebDriver driver, boolean QA) {
		super(driver);
		
		if(QA) {
			baseURL = "";
		}else {
			baseURL = "";
		}
		
		//Now go to the URL
		driver.get(this.baseURL);
		
		String actualPageTitle = driver.getTitle();
		System.out.println("You are in: " + actualPageTitle);
		
		assertEquals(expectedPageTitle, actualPageTitle);
	}
	
	//Popup workaround
	public SelectPage goToPLP() throws Exception {
		driver.findElement(By.cssSelector("a[class='close-popup alert-popup-button'")).click();
		
		return new SelectPage(driver);
	}
	
	//Use this if you get rid of the 2020 popup from the beginning
	public SeriesPage goToSeriesPage(String selectedChair) throws Exception {
		//Let the page load prior to going to the next page
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt='" + selectedChair + "'")));
		driver.findElement(By.cssSelector("img[alt='" + selectedChair + "'")).click();
		
		return new SeriesPage(driver);
	}
}