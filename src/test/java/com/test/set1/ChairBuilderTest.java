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
import com.exemplis.pageobjects.MyAccountPage;
import com.exemplis.pageobjects.SaveAndReviewPage;
import com.exemplis.pageobjects.SelectPage;
import com.exemplis.pageobjects.SeriesPage;
import com.exemplis.pageobjects.StartURL;

public class ChairBuilderTest {
	private WebDriver driver;
	private StartURL PLP_Page;
	private SelectPage selectPage;
	private SeriesPage seriesPage;
	private ChairBuilder chairBuilder;
	private SaveAndReviewPage saveAndReviewPage;
	private MyAccountPage myAccountPage;
	private boolean QA = false;//Indicates whether or not I'm testing in QA OR Prod
	
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
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void test() throws Exception{
		PLP_Page = new StartURL(driver, QA);
		selectPage = PLP_Page.goToSelectPage();
		seriesPage = selectPage.goToSeriesPage();
		chairBuilder = seriesPage.goToChairBuilder();
		chairBuilder.customize();
		saveAndReviewPage = chairBuilder.goToSaveAndReviewPage();
		saveAndReviewPage.login();
		myAccountPage = saveAndReviewPage.generateNewProject();
		myAccountPage.renameProject();
		myAccountPage.downloadXML();
	}
	
	@After
	public void clean() throws Exception{
		driver.quit();
	}
}