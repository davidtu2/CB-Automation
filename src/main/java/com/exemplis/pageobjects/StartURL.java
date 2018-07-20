package com.exemplis.pageobjects;

import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StartURL extends Link {
	private String baseURL;
	private String expectedPageTitle = "ChairBuilder";
	
	public StartURL(WebDriver driver) {
		super(driver);
		
		//Define the wait time to be 20 seconds to look for web elements:
		wait = new WebDriverWait(driver, 20);
		
		//If I'm testing in QA, use the 1st URL, otherwise, use the PROD one
		if(QA) {
			baseURL = "https://qachairbuilder.sitonit.net/";
		}else {
			baseURL = "https://chairbuilder.sitonit.net/";
		}
		
		//Goes to the URL
		driver.get(baseURL);
		
		//Check if I am in the right page:
		String actualPageTitle = driver.getTitle();
		System.out.print("Breadcrumb (Of page titles): " + actualPageTitle);
		assertEquals(expectedPageTitle, actualPageTitle);
	}
	
	//2020 Popup workaround:
	public void removePopup() throws Exception {
		driver.findElement(By.cssSelector("a[class='close-popup alert-popup-button'")).click();
	}
	
	public SelectPage goToSelectPage(String chair) {
		//Look for the element:
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt='" + chair + "'")));
		driver.findElement(By.cssSelector("img[alt='" + chair + "'")).click();
		
		return new SelectPage(driver);
	}
	
	public SeriesPage goToSeriesPage(String chair) throws Exception {
		//Look for the element:
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt='" + chair + "'")));
		driver.findElement(By.cssSelector("img[alt='" + chair + "'")).click();
		
		return new SeriesPage(driver);
	}
}