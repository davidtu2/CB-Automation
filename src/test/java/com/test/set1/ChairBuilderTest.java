package com.test.set1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.exemplis.pageobjects.ChairBuilder;
import com.exemplis.pageobjects.SelectPage;
import com.exemplis.pageobjects.SeriesPage;
import com.exemplis.pageobjects.StartURL;

public class ChairBuilderTest {
	private WebDriver driver;
	boolean QA = false;
	
	@Before
	public void setup() throws Exception{
		//In order to avoid the "This type of file can harm your computer" message, we need to enable safe browsing
		ChromeOptions options = new ChromeOptions();
		Map<String, Object> preferences = new HashMap<String, Object>();
		preferences.put("safebrowsing.enabled", true);
		options.setExperimentalOption("prefs", preferences);
		driver = new ChromeDriver(options);
		
		//Maximizes the browser
		driver.manage().window().maximize();
		
		//This test will last for 30 secs
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void test() throws Exception{
		StartURL PLP_Page = new StartURL(driver, QA);
		SelectPage selectPage = PLP_Page.goToSelectPage();
		SeriesPage seriesPage = selectPage.goToSeriesPage();
		ChairBuilder chairBuilder = seriesPage.goToChairBuilder();
		
		chairBuilder.customize();
		Thread.sleep(3000);//Let the chair graphics load
		chairBuilder.login();
		Thread.sleep(3000);//Let the login go through by waiting a bit
		chairBuilder.generateNewProject();
		chairBuilder.downloadXML();
		Thread.sleep(3000);
	}
	
	@After
	public void clean() throws Exception{
		driver.quit();
	}
}
