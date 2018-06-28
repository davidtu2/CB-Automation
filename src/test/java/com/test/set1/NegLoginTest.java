package com.test.set1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class NegLoginTest {
	private WebDriver driver;
	
	@Before
	public void b4() {
		System.out.println("b4");
		// Optional, if not specified, WebDriver will search your path for chromedriver.
		//System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
		
		//Creates a new instance of Driver in order to use it's API:
		driver = new ChromeDriver();
		//Maximize the browser
		driver.manage().window().maximize();
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
		driver.get("https://my.fullerton.edu");
		driver.findElement(By.id("username")).sendKeys("david.tu2");
		driver.findElement(By.id("password")).sendKeys("testpwd");
		driver.findElement(By.name("_eventId_proceed")).click();
		
		//The validation portion
		assertEquals(driver.findElement(By.cssSelector(".form-element.form-error")).getText(),"The password you entered was incorrect.");
		System.out.println("Test is complete");
		
		
	}
	
	@After
	public void after() {
		driver.quit();
	}

}
