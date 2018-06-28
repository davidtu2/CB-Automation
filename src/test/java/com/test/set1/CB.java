package com.test.set1;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

public class CB {
	private WebDriver driver;
	private String[] breadcrumb = {"Novo", "Highback Mesh Black Frame"};
	private String[] options = {"BK1", "Novo.FC12", "AR2", "Vnt.B", "B18", "CS6", "UC"};
	
	//Colors
	private String[] mesh = {"MC24"};//These have no cost
	private String[] lumbar = {"AL2", "LA5"};//These require the additional click of APPLY within it's child element
	private String[] color = {"Sugar", "Electric Blue"};//Pattern & Colorway
	boolean Cal133 = true;
	boolean COM = false;
	
	private String user = "kpamittan";
	private String pw = "exemplis";
	private String expected = "ChairBuilder";
	private String base = "513.00";
	private LinkedList<Link> titles = new LinkedList<Link>();//Using one of Java's FIFO implementations
	boolean QA = false;
	
	@Before
	public void navigate() throws Exception{
		try {
			setup();
		}catch(Exception error){
			fail("Error in setup function");
		}
		
		StartURL start;
		try {
			if(QA) {
				//Go to QA site
				start = new StartURL(driver, "https://qachairbuilder.sitonit.net/");
			}else {
				//Go to Prod site
				start = new StartURL(driver, "https://chairbuilder.sitonit.net/");
			}
			
			//Popup workaround
			Link link = start.getLinkByClass("close-popup alert-popup-button");
			
			//Navigate to the page
			for (int i = 0; i < breadcrumb.length; i++) {
				titles.add(start.getLinkByAltText(breadcrumb[i]));
				
				//Let the page load prior to going to the next page
				Thread.sleep(3000);
			}
		}catch (Exception error) {
			fail("Error in navigating towards the page: " + error.getMessage());
		}
	}

	@Test
	public void test() throws Exception{
		//First, let's make sure I'm in the right page
		String actual = driver.getTitle();
		assertEquals(expected, actual);
		
		try {
			customize();
			
			//Let the chair graphics load
			Thread.sleep(3000);
		}catch(Exception error) {
			fail("Error in chair customization: " + error.getMessage());
		}
		
		try {
			login();
			
			//Let the login go through by waiting a bit
			Thread.sleep(2000);
		}catch(Exception error) {
			fail("Login error. Please check your credentials: " + error.getMessage());
		}
		
		try {
			generateNewProject();
		}catch(Exception error) {
			fail("Error in project creation: " + error.getMessage());
		}
		
		try {
			downloadXML();
			
			Thread.sleep(2000);
		}catch(Exception error) {
			fail("Error in downloading the XML file: " + error.getMessage());
		}
		
	}
	
	@After
	public void clean() throws Exception{
		debug();
		driver.quit();
	}
	
	public void setup() {
		//In order to avoid the "This type of file can harm your computer" message, we need to enable safe browsing
		ChromeOptions options = new ChromeOptions();
		Map<String, Object> preferences = new HashMap<String, Object>();
		preferences.put("safebrowsing.enabled", true);
		options.setExperimentalOption("prefs", preferences);
		driver = new ChromeDriver(options);
	}
	
	public BigDecimal price(String priceText, BigDecimal expected, String code) {
		//Depending on the price text, either add or subtract the balance
		if (priceText.charAt(1) == '+') {
			priceText = priceText.replace("[+$", "");
			priceText = priceText.replace("]", "");
			BigDecimal price = new BigDecimal(priceText);
			expected = expected.add(price);
		} else if(priceText.charAt(1) == '-') {
			priceText = priceText.replace("[-$", "");
			priceText = priceText.replace("]", "");
			BigDecimal price = new BigDecimal(priceText);
			expected = expected.subtract(price);
		}else {
			System.out.println("For the code, " + code + ", the price is: " + priceText + ", so there is no change in price.");
		}
		
		return expected;
	}
	
	public void customize() throws Exception{
		//Convert the base price to a BigDecimal for monetary calculations
		BigDecimal expected = new BigDecimal(base);
		
		/*for(int i = 0; i < options.length; i++) {
			//First let's make the element visible in the screen
			//WebElement element = driver.findElement(By.cssSelector("label[for='"+ options[i] +"'"));
			//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			//((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -100)");
			
			//Find the element, then look at it's descendent's text value to get the price
			WebElement element = driver.findElement(By.cssSelector("label[for='"+ options[i] +"'"));
			String priceText = element.findElement(By.cssSelector("p > span")).getText();
			expected = price(priceText, expected, options[i]);//Updates the price for each code that's going to be processed
			
			//Finally, click on the filter to apply it
			element.click();
			Thread.sleep(3000);//Let it finish updating the filters, as a chosen filter may open up more filters
		}*/
		
		expected = pickColor(expected);//TODO: A mess...Refactor this!
		
		//Check if the calculated price is what is displayed on the site
		String actual = driver.findElement(By.cssSelector("#subheader > nav > div > div.chair-info > ul > li:nth-child(2) > h4")).getText();
		actual = actual.replace("$", "");
		assertEquals(expected.toString(), actual);
		
		//Save and Review
		driver.findElement(By.id("finalize")).click();
	}
	
	public BigDecimal pickColor(BigDecimal expected) throws Exception{
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");//Scroll to the top
		//Thread.sleep(3000);
		
		/*//Mesh
		for (int i = 0; i < mesh.length; i++) {
			driver.findElement(By.cssSelector("label[for='"+ mesh[0] +"'")).click();//TODO: Somehow figure out if the image had changed
			Thread.sleep(3000);
		}
		
		//Lumbar
		for (int i = 0; i < lumbar.length; i++) {
			WebElement child = driver.findElement(By.cssSelector("label[for='" + lumbar[i] + "'"));
			WebElement parent = child.findElement(By.xpath(".."));//Currently, XPATH is the best way to find the parent
			
			child.click();//Make the thumb nail pop up
			Thread.sleep(3000);
			parent.findElement(By.cssSelector("div > button")).click();//Clicks APPLY within the thumb nail
			Thread.sleep(3000);//TODO: Somehow figure out if the image had changed
		}
		
		//Material color
		if (Cal133) {
			WebElement element = driver.findElement(By.cssSelector("label[class='cal133 active'"));
			String priceText = element.findElement(By.cssSelector("span > span")).getText();//Get the price text
			
			expected = price(priceText, expected, "Cal133");//Though it may not look like it, expected is a local variable within this function's scope
			element.click();
			Thread.sleep(3000);
		}*/
		
		if (COM) {
			WebElement element = driver.findElement(By.cssSelector("label[class='comFabric active'"));
			String priceText = element.findElement(By.cssSelector("span > span")).getText();//Get the price text
			
			expected = price(priceText, expected, "COM");
			element.click();
			Thread.sleep(3000);
		}else {
			//Select Pattern
			Select dropdown = new Select(driver.findElement(By.id("drop-patterns")));
			dropdown.selectByVisibleText(color[0]);
			Thread.sleep(3000);
			
			//Select Color:
			//Used pattern and colorway to perform wild card searches. Otherwise, just searching for the colorway will yield multiple results
			WebElement child = driver.findElement(By.cssSelector("img[title*='" + color[0] + "'][title*='" + color[1] + "']"));
			WebElement parent = child.findElement(By.xpath(".."));
			WebElement grandparent = parent.findElement(By.xpath(".."));
			
			child.click();//Clicks on the colored thumb nail
			Thread.sleep(3000);
			String priceText = grandparent.findElement(By.cssSelector("div > table > tbody > tr:nth-child(4) > td > span:nth-child(2)")).getText();
			
			expected = price(priceText, expected, color[1]);
			grandparent.findElement(By.cssSelector("div > button")).click();//Clicks APPLY within the thumb nail
			Thread.sleep(3000);
		}
		
		return expected;
	}
	
	public void login(){
		driver.findElement(By.className("login-link")).click();
		driver.findElement(By.name("username")).sendKeys(user);
		driver.findElement(By.name("password")).sendKeys(pw);
		driver.findElement(By.cssSelector("button[class='button secondary submit-login-btn '")).click();
	}
	
	public void generateNewProject() throws Exception {
		//Add to Project > New Project
		driver.findElement(By.cssSelector("button[class='right-off-canvas-toggle add-to-project'")).click();
		
		Thread.sleep(3000);
		driver.findElement(By.className("new-quote")).click();
		
		//Rename the test and Save
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		driver.findElement(By.id("quote-name-input")).sendKeys("Automated Test " + timestamp);
		Thread.sleep(3000);//Need to wait for save button availability
		driver.findElement(By.cssSelector("button[class='button small editable-save'")).click();
	}
	
	public void downloadXML(){
		// Click on Downloads > 2020 Download (XML)
		driver.findElement(By.cssSelector("button[class='btn btn--primary js-downloads-menu'")).click();
		driver.findElement(By.cssSelector("a[class='quote export2020'")).click();
	}
	
	public void debug() {
		System.out.print("Breadcrumb: ");
		while (!titles.isEmpty()) {
			System.out.print(titles.pop().title() + " > ");
		}
	}
}
