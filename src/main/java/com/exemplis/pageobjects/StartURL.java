package com.exemplis.pageobjects;

import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class StartURL extends Link {
	private String baseURL;
	private String expectedPageTitle = "ChairBuilder";
	
	public StartURL(WebDriver driver, boolean QA) {
		super(driver);
		
		if(QA) {
			baseURL = "https://qachairbuilder.sitonit.net/";
		}else {
			baseURL = "https://chairbuilder.sitonit.net/";
		}
		
		//Now go to the URL
		driver.get(this.baseURL);
		
		String actualPageTitle = driver.getTitle();
		System.out.println("You are in: " + actualPageTitle);
		
		assertEquals(expectedPageTitle, actualPageTitle);
	}
	
	//Popup workaround
	public SelectPage goToSelectPage() throws Exception {
		driver.findElement(By.cssSelector("a[class='close-popup alert-popup-button'")).click();
		
		return new SelectPage(driver);
	}
}
