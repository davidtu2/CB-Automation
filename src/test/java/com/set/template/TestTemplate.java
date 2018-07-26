package com.set.template;

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

public class TestTemplate {
	private WebDriver driver;
	private StartURL PLP;
	private SeriesPage seriesPage;
	private ChairBuilder chairBuilder;
	private SaveAndReviewPage saveAndReviewPage;
	private MyAccountPage myAccountPage;
	
	//Value to modify, depending on what you want to test
	//private String[] crumb = {"Aviera Metal", "Multi Seating", "Pre-Configured", "3-Seat", "21\" Seat"};
	//private String[] crumb = {"Composium", "Sharp", "Club", "Three Quarter Valance"};
	private String[] crumb = {"Movi", "Light Task", "Black Frame"};
	//private String[] crumb = {"Novo", "Highback Mesh Black Frame"};
	//private String[] crumb = {"Test"};
	//private String[] crumb = {};
	
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
	/* End-to-end test for the cases:
	 * 1. Open CB
	 * 2. Chair selection
	 * 3. Chair config
	 * 4. Download PDFs, images, and copies link
	 * 5. Logging in
	 * 6. Saving a project
	 * 7. Download XML
	 */
	public void test() throws Exception{
		PLP = new StartURL(driver);
		PLP.removePopup();//In the future, if you get rid of the 2020 popup, comment this line
		seriesPage = PLP.goToSeriesPage2(crumb);
		chairBuilder = seriesPage.goToChairBuilder(crumb);
		chairBuilder.customize();
		chairBuilder.downloadPDF();
		chairBuilder.downloadImage();
		chairBuilder.copyLink();
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