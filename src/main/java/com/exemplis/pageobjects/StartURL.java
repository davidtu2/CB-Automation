package com.exemplis.pageobjects;

import static org.junit.Assert.fail;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class StartURL extends Link {
	private String baseURL;
	
	public StartURL(WebDriver driver, boolean QA) {
		super(driver);
		
		if(QA) {
			baseURL = "https://qachairbuilder.sitonit.net/";
		}else {
			baseURL = "https://chairbuilder.sitonit.net/";
		}
		
		//Now go to the URL
		driver.get(this.baseURL);
		
		System.out.println("You are in: " + driver.getTitle());
		if(!driver.getTitle().equals("ChairBuilder")) {
			//throw new Exception("You are in the wrong page");//TODO: Implement Exception handling
			fail("You are in the wrong page");
		}
	}
	
	//Popup workaround
	public SelectPage goToSelectPage() throws Exception {
		//getLinkByClass("close-popup alert-popup-button");
		driver.findElement(By.cssSelector("a[class='close-popup alert-popup-button'")).click();
		Thread.sleep(3000);//Let the page load prior to going to the next page
		
		return new SelectPage(driver);
	}
}
