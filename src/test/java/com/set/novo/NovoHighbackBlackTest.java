package com.set.novo;

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
import com.exemplis.pageobjects.SeriesPage;
import com.exemplis.pageobjects.StartURL;

public class NovoHighbackBlackTest {
	private WebDriver driver;
	private StartURL PLP;
	private SeriesPage seriesPage;
	private ChairBuilder chairBuilder;
	private SaveAndReviewPage saveAndReviewPage;
	private MyAccountPage myAccountPage;
	
	//Value to modify, depending on what you want to test
	private String[] crumb = {"Novo", "Highback Mesh Black Frame"};
	
	@Before
	public void setup() throws Exception{
		//In order to avoid the "This type of file can harm your computer" message, we need to enable safe browsing
		ChromeOptions chromeOptions = new ChromeOptions();
		Map<String, Object> preferences = new HashMap<String, Object>();
		preferences.put("safebrowsing.enabled", true);
		chromeOptions.setExperimentalOption("prefs", preferences);
		driver = new ChromeDriver(chromeOptions);
		
		//Maximizes the browser
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void test() throws Exception{
		PLP = new StartURL(driver);
		PLP.removePopup();//In the future, if you get rid of the 2020 popup, comment this line
		seriesPage = PLP.goToSeriesPage(crumb[0]);//"PLP page, go to the Series page of the Novo"
		chairBuilder = seriesPage.goToChairBuilder(crumb);
		chairBuilder.customize();
		saveAndReviewPage = chairBuilder.goToSaveAndReviewPage();
		saveAndReviewPage.login();
		myAccountPage = saveAndReviewPage.goToMyAccountPage();
		myAccountPage.renameProject();
		myAccountPage.downloadXML();
	}
	
	@After
	public void clean() throws Exception{
		driver.quit();
	}
}