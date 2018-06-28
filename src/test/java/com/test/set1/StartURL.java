package com.test.set1;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;

public class StartURL extends Link {
	private String baseURL;
	
	public StartURL(WebDriver driver, String baseURL) {
		super(driver);
		this.baseURL = baseURL;
		
		//Maximizes the browser
		driver.manage().window().maximize();
		
		//This test will last for 30 secs
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		//Now go to the URL
		driver.get(this.baseURL);
	}
}
