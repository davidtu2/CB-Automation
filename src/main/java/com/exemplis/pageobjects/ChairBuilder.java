package com.exemplis.pageobjects;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ChairBuilder extends Link {
	private String expectedPageTitle = "ChairBuilder";
	//Convert the base price to a BigDecimal for monetary calculations
	private BigDecimal expectedPrice = new BigDecimal("513.00");//Starts with base price and will accumulate over time
	private String[] options = {"BK1", "Novo.FC12", "AR2", "Vnt.B", "B18", "CS6", "UC"};
	
	//Colors
	private String[] mesh = {"MC24"};//These act similar to filters where you just click on them
	private String[] lumbar = {"AL2", "LA5"};//These however, require the additional click of APPLY within it's child element
	
	//Optional parameters
	private String search = "Electric Blue";
	private String fabricType = "Fabric";
	private String color = "Blue";
	private String patternType = "Solid";
	private String manufacturer = "SitOnIt";
	private String lead = "2 days";
	private int[] gradeRange = {1, 1};
	boolean Cal133 = true;
	boolean COM = false;
	
	//Required parameters
	private String[] patternAndColorway = {"Sugar", "Electric Blue"};
	
	WebDriverWait wait;
	
	public ChairBuilder(WebDriver driver) {
		super(driver);
		
		String actualPageTitle = driver.getTitle();
		System.out.println("You are in: " + actualPageTitle);
		
		assertEquals(expectedPageTitle, actualPageTitle);
		
		//Let the page load prior to going to the next page
		wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finalize")));
		
		//TODO: Figure out a way to extract the data
	}
	
	public void customize() throws Exception{
		selectFilters();
		
		//Colors
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
		for(int i = 0; i < options.length; i++) {
			//Find the element, then look at it's descendent's text value to get the price
			WebElement element = driver.findElement(By.cssSelector("label[for='"+ options[i] +"'"));
			String priceText = element.findElement(By.cssSelector("p > span")).getText();
			expectedPrice = price(priceText, expectedPrice, options[i]);//Updates the price for each code that's going to be processed
			
			//Finally, click on the filter to apply it
			element.click();
			
			//TODO: Somehow figure out if the image had changed:
			Thread.sleep(1000);//Let it finish updating the filters, as a chosen filter may open up more filters. Also, we need to let the garphic load
		}
	}
	
	public void pickMeshColor() throws Exception{
		for (int i = 0; i < mesh.length; i++) {
			driver.findElement(By.cssSelector("label[for='"+ mesh[0] +"'")).click();
			
			//TODO: Somehow figure out if the image had changed
		}
	}
	
	public void pickLumbarColor() throws Exception{
		for (int i = 0; i < lumbar.length; i++) {
			WebElement child = driver.findElement(By.cssSelector("label[for='" + lumbar[i] + "'"));
			WebElement parent = child.findElement(By.xpath(".."));//Currently, XPATH is the best way to find the parent
					
			child.click();//Make the thumb nail pop up
			parent.findElement(By.cssSelector("div > button")).click();//Clicks APPLY within the thumb nail
			
			//TODO: Somehow figure out if the image had changed
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
			
			//TODO: Somehow figure out if the image had changed
		}
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
	
	public SaveAndReviewPage goToSaveAndReviewPage() throws Exception {
		driver.findElement(By.id("finalize")).click();
		
		return new SaveAndReviewPage(driver);
	}
}