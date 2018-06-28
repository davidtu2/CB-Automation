package com.test.set1;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

//Starting point of this test case
public class WebDriverLinkTest {
	private WebDriver driver;
	
	//When using JUint, @Before, @Test, and @After is executed in that order
	@Before
	public void init() {
		//Optional, if not specified, WebDriver will search PATH for chromedriver
		//System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
		//System.setProperty("webdriver.gecko.driver", "C:\\geckodriver-v0.20.1-win64\\geckodriver.exe");
		//System.setProperty("webdriver.ie.driver", "C:\\IEDriverServer_x64_3.12.0\\IEDriverServer.exe");
		
		//Creates a new instance of Driver in order to use it's library:
		//driver = new ChromeDriver();
		//driver = new FirefoxDriver();
		driver = new InternetExplorerDriver();
		//Breadcrumb: start > l1 > l2 and so on...
		//TODO: Implement a for loop
		StartURL start = new StartURL(driver, "https://www.seleniumhq.org/");//Navi to the base URL
		Link l1 = start.getLinkByLinkText("Documentation");//Navi to l1
		Link l2 = l1.getLinkByLinkText("Selenium WebDriver");//Navi to l2
	}

	@Test
	public void test() {
		//The validation portion
		String expected = "Selenium WebDriver — Selenium Documentation";
		
		//First, make sure I'm on the right page:
		String actual = driver.getTitle();
		//Then test
		assertEquals(expected, actual);
	}
	
	@After
	public void clean() {
		driver.quit();
	}
}