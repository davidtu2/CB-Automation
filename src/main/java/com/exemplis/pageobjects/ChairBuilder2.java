package com.exemplis.pageobjects;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ChairBuilder2 extends Link {
	WebDriverWait wait;
	private String expectedPageTitle = "ChairBuilder";
	private String chairSeries;
	
	//Extracted Data//TODO: Figure out a way to extract all of this data:
	//Convert the base price to a BigDecimal for monetary calculations. This starts with base price and will accumulate over time
	private BigDecimal expectedPrice = new BigDecimal("513.00");
	
	//Options
	private String[] arms = {"AR0", "AR2", "AR4", "AR6"};
	private String[] mechanism = {"Vnt.B", "Novo.T", "Vnt.F", "Vnt.Fe3"};
	private String[] bases = {"B17", "B18", "B20", "B21"};
	private String[] cylinderHeights = {"CH1", "Black.CH1", "CH3", "CH4"};
	private String[] casters = {"CS5", "CS6", "CS3"};
	private String[] packagings = {"KD", "UC", "AC"};
	private List<String> options = new ArrayList<String>();
	
	//Colors
	private String[] meshColors = {"MC20", "MC21", "MC22", "MC23", "MC24", "MC25", "MC26", "MC27", "MC28", "MC29", "MC30", "MC31"};
	private String[] backLumbarColors = {"AL1", "AL2", "AL3", "LA1", "LA2", "LA3", "LA4", "LA5", "LA6", "LA7", "LA8", "LA9", "LA10", "LA11", "LA12", "LA13"};
	private List<String> mesh = new ArrayList<String>();
	private List<String> lumbar = new ArrayList<String>();
	
	//Fabrics
	private String[] patternAndColorway = {"Sugar", "Electric Blue"};
	//Optional params
	private String search = "Electric Blue";
	private String fabricType = "Fabric";
	private String color = "Blue";
	private String patternType = "Solid";
	private String manufacturer = "SitOnIt";
	private String lead = "2 days";
	private int[] gradeRange = {1, 1};
	boolean Cal133 = true;
	boolean COM = false;
	
	public ChairBuilder2(WebDriver driver, String chairSeries) {
		super(driver);
		this.chairSeries = chairSeries;
		
		String actualPageTitle = driver.getTitle();
		System.out.println("You are in: " + actualPageTitle);
		
		assertEquals(expectedPageTitle, actualPageTitle);
		
		wait = new WebDriverWait(driver, 20);
		
		//Populate all the options you can choose in CB
		for(int i = 0; i < arms.length; i++) {
			options.add(arms[i]);
		}
		
		for(int i = 0; i < mechanism.length; i++) {
			options.add(mechanism[i]);
		}
		
		//TODO: Determine how to deal with these dependencies
		for(int i = 0; i < cylinderHeights.length; i++) {
			if(this.chairSeries.contains("Black") && !cylinderHeights[i].equals("CH1")) {
				options.add(cylinderHeights[i]);
			}else if((this.chairSeries.contains("White") || this.chairSeries.contains("Fog")) && cylinderHeights[i].equals("CH1")) {
				//White/Fog Frames can only use this option
				options.add(cylinderHeights[i]);
			}
		}
		
		for(int i = 0; i < casters.length; i++) {
			options.add(casters[i]);
		}
		
		for(int i = 0; i < bases.length; i++) {
			if(this.chairSeries.contains("Black") && !bases[i].equals("B20") && !bases[i].equals("B21")) {
				options.add(bases[i]);
			}else if(this.chairSeries.contains("White") && !bases[i].equals("B17") && !bases[i].equals("B21")) {
				options.add(bases[i]);
			}else if(this.chairSeries.contains("Fog") && !bases[i].equals("B17") && !bases[i].equals("B20")) {
				options.add(bases[i]);
			}
		}
		
		for(int i = 0; i < packagings.length; i++) {
			options.add(packagings[i]);
		}
		
		//Populate all of the mesh colors you can choose in CB
		for(int i = 0; i < meshColors.length; i++) {
			mesh.add(meshColors[i]);//These act similar to filters where you just click on them
		}
		
		//Populate all of the lumbar colors you can choose in CB (lumbar accent colors included)
		for(int i = 0; i < backLumbarColors.length; i++) {
			lumbar.add(backLumbarColors[i]);//These however, require the additional click of APPLY within it's child element
		}
	}

	public void customize() throws Exception{
		selectFilters();
		
		//Colors & Fabrics
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");//Scroll to the top
		pickMeshColor();
		pickLumbarColor();
		pickColorway();
		
		//Check if the calculated price is what is displayed on the site
		String actualPrice = driver.findElement(By.cssSelector("#subheader > nav > div > div.chair-info > ul > li:nth-child(2) > h4")).getText();
		actualPrice = actualPrice.replace("$", "");
		assertEquals(expectedPrice.toString(), actualPrice);
	}
	
	public void selectFilters() throws Exception{
		//TODO: Figure out a way to deal with this dependency: if CH3 or CH4 is chosen, B18 isn't available. B18 is available for Black.CH1
		boolean dependency = false;
		
		for(int i = 0; i < options.size(); i++) {
			System.out.println("Processing Code: " + options.get(i));
			
			if(options.get(i).equals("CH3") || options.get(i).equals("CH4")) {
				dependency = true;
			}
			
			if(options.get(i).equals("B18") && dependency) {
				//Make Black.CH1 available by clicking on it again
				WebElement element = driver.findElement(By.cssSelector("label[for='Black.CH1'"));
				String priceText = element.findElement(By.cssSelector("p > span")).getText();
				expectedPrice = price(priceText, expectedPrice, options.get(i));
				element.click();
			}
			
			//Find the element, then look at it's descendent's text value to get the price
			WebElement element = driver.findElement(By.cssSelector("label[for='"+ options.get(i) +"'"));
			String priceText = element.findElement(By.cssSelector("p > span")).getText();
			expectedPrice = price(priceText, expectedPrice, options.get(i));//Updates the price for each code that's going to be processed
			
			//Finally, click on the filter to apply it
			element.click();
			
			Thread.sleep(1000);//Let it finish updating the filters, as a chosen filter may open up more filters. Also, we need to let the garphic load
		}
	}
	
	public void pickMeshColor() throws Exception{
		for (int i = 0; i < mesh.size(); i++) {
			driver.findElement(By.cssSelector("label[for='"+ mesh.get(i) +"'")).click();
		}
	}
	
	public void pickLumbarColor() throws Exception{
		for (int i = 0; i < lumbar.size(); i++) {
			WebElement child = driver.findElement(By.cssSelector("label[for='" + lumbar.get(i) + "'"));
			WebElement parent = child.findElement(By.xpath(".."));//Currently, XPATH is the best way to find the parent
					
			child.click();//Make the thumb nail pop up
			parent.findElement(By.cssSelector("div > button")).click();//Clicks APPLY within the thumb nail
		}
	}
	
	public void pickColorway() throws Exception{
		//Determines if there is a Cal133
		if (Cal133) {
			WebElement element = driver.findElement(By.cssSelector("label[class='cal133 active'"));
			String priceText = element.findElement(By.cssSelector("span > span")).getText();//Get the price text
			
			expectedPrice = price(priceText, expectedPrice, "Cal133");
			element.click();
		}
		
		//Determines COM. If it's checked, Colorway selection is skipped
		if (COM) {
			WebElement element = driver.findElement(By.cssSelector("label[class='comFabric active'"));
			String priceText = element.findElement(By.cssSelector("span > span")).getText();//Get the price text
			
			expectedPrice = price(priceText, expectedPrice, "COM");
			element.click();
		}else {
			//Fabric Type dropdown
			if(!fabricType.isEmpty()) {
				WebElement element = driver.findElement(By.className("fabric-type-toggle"));
				element = element.findElement(By.cssSelector("select"));
				Select dropdown = new Select(element);
				dropdown.selectByVisibleText(fabricType);
			}
			
			//Search box
			if(!search.isEmpty()) {
				driver.findElement(By.id("cb-search-fabric-text")).sendKeys(search);
				driver.findElement(By.id("cb-search-fabric-submit")).click();
			}
			
			//Color dropdown
			if(!color.isEmpty()) {
				Select dropdown = new Select(driver.findElement(By.id("drop-colors")));
				dropdown.selectByVisibleText(color);
			}
			
			//Pattern Type dropdown
			if(!patternType.isEmpty()) {
				Select dropdown = new Select(driver.findElement(By.id("drop-patternType")));
				dropdown.selectByVisibleText(patternType);
			}
			
			//Manufacturer dropdown
			if(!manufacturer.isEmpty()) {
				Select dropdown = new Select(driver.findElement(By.id("drop-manufacturers")));
				dropdown.selectByVisibleText(manufacturer);
			}
			
			//Lead Time dropdown
			if(!lead.isEmpty()) {
				WebElement element = driver.findElement(By.className("lead-time-filter"));
				element = element.findElement(By.cssSelector("select"));
				Select dropdown = new Select(element);
				dropdown.selectByVisibleText(lead);
			}
			
			//Grade Range dropdowns
			if(gradeRange.length == 2 && gradeRange[0] > 0 & gradeRange[1] >= gradeRange[0]) {
				Select dropdown = new Select(driver.findElement(By.id("grade-min")));
				dropdown.selectByVisibleText(Integer.toString(gradeRange[0]));
				dropdown = new Select(driver.findElement(By.id("grade-max")));
				dropdown.selectByVisibleText(Integer.toString(gradeRange[1]));
			}
			
			//Pattern dropdown
			Select dropdown = new Select(driver.findElement(By.id("drop-patterns")));
			dropdown.selectByVisibleText(patternAndColorway[0]);
			
			//Select Colorway: Used pattern and colorway strings to perform wild card searches. Otherwise, just searching for the colorway will yield multiple results
			//Also, we need to wait a bit for element to be located
			WebElement child = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[title*='" + patternAndColorway[0] + "'][title*='" + patternAndColorway[1] + "']")));
			WebElement parent = child.findElement(By.xpath(".."));
			WebElement grandparent = parent.findElement(By.xpath(".."));
			
			child.click();//Clicks on the colored thumb nail
			String priceText = grandparent.findElement(By.cssSelector("div > table > tbody > tr:nth-child(4) > td > span:nth-child(2)")).getText();
			
			expectedPrice = price(priceText, expectedPrice, patternAndColorway[1]);
			grandparent.findElement(By.cssSelector("div > button")).click();//Clicks APPLY within the thumb nail
		}
	}
	
	public BigDecimal price(String priceText, BigDecimal expected, String code) {
		//This is for the case of priceText being blank, which occurs when you click on an option that is already selected
		if(priceText.isEmpty()) {
			priceText = "[$0.00]";
		}
		
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
	
	public SaveAndReviewPage goToSaveAndReviewPage() throws Exception {
		//Let the page load prior to going to the next page
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finalize")));
		driver.findElement(By.id("finalize")).click();
		
		return new SaveAndReviewPage(driver);
	}
}