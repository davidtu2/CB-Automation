package com.exemplis.pageobjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
	
	//Alternate to the function: goToSeriesPage(String chair), if you'd rather go straight towards the series page from the beginning of the crumb,
	//thereby, bypassing the instantiation and giving up the use of all of the select page objects
	public SeriesPage goToSeriesPage2(String crumb[]) {
		if(crumb.length > 1) {
			for(int i = 0; i < crumb.length - 1; i++) {
				//Look for the element:
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt='" + crumb[i] + "'")));
				driver.findElement(By.cssSelector("img[alt='" + crumb[i] + "'")).click();
			}
		}else {
			fail("Crumb is too short, it needs to be longer than 1");
		}
		
		return new SeriesPage(driver);
	}
}