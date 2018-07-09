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
import com.exemplis.pageobjects.ChairBuilder2;
import com.exemplis.pageobjects.MyAccountPage;
import com.exemplis.pageobjects.SaveAndReviewPage;
import com.exemplis.pageobjects.SelectPage;
import com.exemplis.pageobjects.SeriesPage;
import com.exemplis.pageobjects.StartURL;

public class ChairBuilderTest {
	private WebDriver driver;
	private StartURL PLP;
	private SelectPage PLP2;
	private SeriesPage seriesPage;
	private ChairBuilder2 chairBuilder2;
	private SaveAndReviewPage saveAndReviewPage;
	private MyAccountPage myAccountPage;
	
	//Values to modify, depending on what you want to test:
	private boolean QA = false;//Indicates whether or not you want to test in QA or Prod
	private String[] crumb = {"Novo", "Highback Mesh Black Frame"};
	
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
		PLP = new StartURL(driver, QA);
		
		//In the future, if you happen to get rid of the 2020 popup, comment this block:
		PLP2 = PLP.goToPLP();
		seriesPage = PLP2.goToSeriesPage(crumb[0]);
		
		//And uncomment this:
		//seriesPage = PLP.goToSeriesPage(crumb[0]);
		
		chairBuilder2 = seriesPage.goToChairBuilder2(crumb[1]);
		chairBuilder2.customize();
		saveAndReviewPage = chairBuilder2.goToSaveAndReviewPage();
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