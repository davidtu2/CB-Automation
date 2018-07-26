package com.exemplis.pageobjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class ChairBuilder extends Link {
	private String expectedPageTitle = "ChairBuilder";
	private String[] description;
	
	//BigDecimal is used for monetary calculations. This starts with base price and will accumulate over time
	private BigDecimal expectedPrice;
	
	//Fabrics
	//TODO: For future enhancement, consider extracting all the fabrics:
	private String[] patternAndColorway = {"Sugar", "Electric Blue"};
		
	//The extracted data:
	private List<String> options = new ArrayList<String>();
	private List<String> colors = new ArrayList<String>();
	private List<String> lumbar = new ArrayList<String>();
	
	public ChairBuilder(WebDriver driver, String[] crumb, String basePrice) {
		super(driver);
		description = crumb;
		expectedPrice = new BigDecimal(basePrice);
		
		//Check if I am in the right page:
		String actualPageTitle = driver.getTitle();
		System.out.println(" > " + actualPageTitle);
		assertEquals(expectedPageTitle, actualPageTitle);
		
		wait = new WebDriverWait(driver, 20);
		
		//Read from the data file and sort all the codes
		try {
			Reader reader = Files.newBufferedReader(Paths.get(file));
			CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
			CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).withCSVParser(parser).build();//removes header
			
			String[] record;
			while((record = csvReader.readNext()) != null) {
				System.out.println("Extracting: " + record[0]);
				
				//Global exclusions (To be maintained over time)
				if(record[0].contains("133")) {
					continue;
				}
				
				//Chair families specific:
				//Movi
				if(description[0].contains("Movi")) {
					//If we are dealing with Movi in general, we can exclude the following:
					if(record[0].startsWith("Movi.") || record[0].startsWith("GRP1")) {
						continue;
					//Now let's deal with specific chairs:
					//Nester and Light Task:
					}else if(description[0].equals("Movi") && record[1].contains(description[0] + "-")) {
						//Nester's exclusions
						if(description[1].contains("Nester") && (record[0].startsWith("B") || record[0].equals("KD") || record[0].equals("AC"))) {
							continue;
						//Light Task's exclusions
						}else if(description[1].equals("Light Task") && (record[0].contains("NoCharge") || record[0].startsWith("FC"))){
							continue;
						//Now let's sort the rest of the data:
						}else if(description[1].contains("Nester") && record[0].startsWith("FC")) {
							colors.add(record[0]);//For Nesters, colors will have frame and mesh colors
						}else if(record[0].startsWith("MC")) {
							colors.add(record[0]);
						}else {
							options.add(record[0]);
						}
					//Light Task Stools only needs to sort the data:
					}else if(description[0].equals("Movi Light Task Stool") && record[1].contains("MoviStool-")) {
						if(record[0].contains("MC")) {
							colors.add(record[0]);
						}else {
							options.add(record[0]);
						}
					}
				}
				
				//Novo
				if(description[0].equals("Novo") && record[1].contains(description[0] + "-")) {
					if(record[0].startsWith("GRP3") || record[0].startsWith("BK") || record[0].startsWith("Novo.FC")) {
						continue;
					}else if(record[0].startsWith("MC")) {
						colors.add(record[0]);//Mesh colors
					}else if(record[0].startsWith("AL") || record[0].startsWith("LA")) {
						lumbar.add(record[0]);//These options require the additional click of APPLY within it's child element (A thumbnail)	
					}else if(record[0].startsWith("B") && !record[0].equals("Black.CH1")){
						if(description[description.length - 1].contains("Black") && !record[0].equals("B20") && !record[0].equals("B21")) {
							options.add(record[0]);
						}else if(description[description.length - 1].contains("White") && !record[0].equals("B17") && !record[0].equals("B21")) {
							options.add(record[0]);
						}else if(description[description.length - 1].contains("Fog") && !record[0].equals("B17") && !record[0].equals("B20")) {
							options.add(record[0]);
						}
					}else if(record[0].contains("CH")) {
						if(description[description.length - 1].contains("Black") && !record[0].equals("CH1")) {
							options.add(record[0]);
						}else if((description[description.length - 1].contains("White") || description[description.length - 1].contains("Fog")) && record[0].equals("CH1")) {
							options.add(record[0]);//White/Fog Frames can only use this option
						}
					}else {
						options.add(record[0]);
					}
				}
			}
			
			csvReader.close();
			reader.close();
			
		} catch(IOException error){
			fail("Failure to read from the .csv");//Immediately fail the test if the data can't be read
		}
	}

	public void customize() throws Exception{
		if(optionsTest) {
			selectOptions();
		}
		
		if(colorsTest) {
			pickColor();
			pickLumbarColor();
		}
		
		if(fabricsTest) {
			pickColorway();
		}
		
		//After config, check if the calculated price is what is actually displayed on the site
		String actualPrice = driver.findElement(By.cssSelector("#subheader > nav > div > div.chair-info > ul > li:nth-child(2) > h4")).getText();
		actualPrice = actualPrice.replace("$", "");
		assertEquals(expectedPrice.toString(), actualPrice);
	}
	
	public void selectOptions() throws Exception{
		boolean dependency = false;
		
		for(int i = 0; i < options.size(); i++) {
			System.out.println("Processing: " + options.get(i));
			
			//Dependencies://TODO: Figure out a way to deal with dependencies abstractly
			//Novo:
			if(description[0].equals("Novo")) {
				if(options.get(i).equals("CH3") || options.get(i).equals("CH4")) {
					//If CH3 or CH4 is chosen, B18 isn't available:
					dependency = true;
				}
				
				//B18 is only available for Black.CH1, so it must be re-enabled:
				if(options.get(i).equals("B18") && dependency) {
					System.out.println("Re-Processing: Black.CH1\nRe-Processing: B18");
					
					//Find the element, then look at it's descendent's text to get the price
					WebElement element = driver.findElement(By.cssSelector("label[for='Black.CH1'"));
					String price = element.findElement(By.cssSelector("p > span")).getText();
					element.click();
					
					//Apply the price for the selected code:
					expectedPrice = price(price, expectedPrice);
				}
			}
			
			//Wait for the filters to refresh, otherwise the script will pick up the prices before the refresh:
			Thread.sleep(3000);
			
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='"+ options.get(i) +"'")));
			String price = element.findElement(By.cssSelector("p > span")).getText();
			element.click();
			expectedPrice = price(price, expectedPrice);
		}
	}
	
	public void pickColor(){
		for (int i = 0; i < colors.size(); i++) {
			System.out.println("Processing: " + colors.get(i));
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");//Scroll to the top to refresh the page
			
			driver.findElement(By.cssSelector("label[for='"+ colors.get(i) +"'")).click();
		}
	}
	
	public void pickLumbarColor(){
		for (int i = 0; i < lumbar.size(); i++) {
			System.out.println("Processing: " + lumbar.get(i));
			
			WebElement child = driver.findElement(By.cssSelector("label[for='" + lumbar.get(i) + "'"));
			child.click();//Make the thumb nail pop up
			
			WebElement parent = child.findElement(By.xpath(".."));//Currently, XPATH is the best way to find the parent
			parent.findElement(By.cssSelector("div > button")).click();//Clicks APPLY within the thumb nail
		}
	}
	
	public void pickColorway(){
		//Determines if there is a Cal133
		if (Cal133) {
			System.out.println("Processing: Cal133");
			
			WebElement element = driver.findElement(By.cssSelector("label[class='cal133 active'"));
			String price = element.findElement(By.cssSelector("span > span")).getText();
			element.click();
			
			expectedPrice = price(price, expectedPrice);
		}
		
		//Determines COM. If it's checked, Colorway selection is skipped
		if (COM) {
			System.out.println("Processing: COM");
			
			WebElement element = driver.findElement(By.cssSelector("label[class='comFabric active'"));
			String priceText = element.findElement(By.cssSelector("span > span")).getText();
			element.click();
			
			expectedPrice = price(priceText, expectedPrice);
			
		}else {
			//Fabric Type dropdown
			System.out.println("Processing: " + patternAndColorway[0] + ", " + patternAndColorway[1]);
			
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
			
			//Select Colorway: Used pattern and colorway strings to perform wild card searches, 
			//Otherwise, just searching for the colorway will yield multiple results...
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[title*='" + patternAndColorway[0] + "'][title*='" + patternAndColorway[1] + "']")));
			driver.findElement(By.cssSelector("img[title*='" + patternAndColorway[0] + "'][title*='" + patternAndColorway[1] + "']")).click();
			
			//Get the price of the colorway
			WebElement child = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[title*='" + patternAndColorway[0] + "'][title*='" + patternAndColorway[1] + "']")));
			WebElement grandparent = child.findElement(By.xpath("ancestor::div[1]"));
			String price = grandparent.findElement(By.cssSelector("div > table > tbody > tr:nth-child(4) > td > span:nth-child(2)")).getText();
			grandparent.findElement(By.cssSelector("div > button")).click();//Clicks APPLY within the thumb nail
			expectedPrice = price(price, expectedPrice);
		}
	}
	
	public void downloadPDF() throws Exception{
		//Downloads the customized product sheet as well as the COL/COM instructions
		//Customized product sheet
		driver.findElement(By.cssSelector("button[class='downloads']")).click();
		Thread.sleep(1000);//Let the form load
		driver.findElement(By.cssSelector("a[class='download-pdf loading-modal']")).click();
		driver.findElement(By.id("downloadButton")).click();
		Thread.sleep(7000);//sleep() lets the downloads go through
		
		//COL/COM
		driver.findElement(By.cssSelector("a[class='download-colcom-pdf']")).click();
		Thread.sleep(1000);
		
		//Collapses the downloads dropdown list
		driver.findElement(By.cssSelector("button[class='downloads active']")).click();
	}
	
	public void downloadImage() throws Exception{
		//Downloads large, medium, and small images
		//Large
		driver.findElement(By.cssSelector("button[class='downloads']")).click();
		driver.findElement(By.cssSelector("a[class='download-image cylindo']")).click();
		driver.findElement(By.cssSelector("a[data-size='large'")).click();
		Thread.sleep(10000);
		
		//Medium
		driver.findElement(By.cssSelector("a[class='download-image cylindo']")).click();
		driver.findElement(By.cssSelector("a[data-size='medium'")).click();
		Thread.sleep(2000);
		
		//Small
		driver.findElement(By.cssSelector("a[class='download-image cylindo']")).click();
		driver.findElement(By.cssSelector("a[data-size='small'")).click();
		Thread.sleep(2000);
		
		driver.findElement(By.cssSelector("button[class='downloads active']")).click();
	}
	
	public void copyLink() {
		//Clicks on the "Copy Link" button and validates whether a link has been copied
		driver.findElement(By.cssSelector("button[class='downloads']")).click();
		driver.findElement(By.id("specCopy")).click();
		
		//Check to see if the link has been copied. We are going to verify this by checking text of the tag
		String actual = driver.findElement(By.id("specCopy")).getText();
		assertEquals("LINK COPIED", actual);
		
		driver.findElement(By.cssSelector("button[class='downloads active']")).click();
	}
	
	public BigDecimal price(String price, BigDecimal expected) {
		//This is for the case of priceText being blank, which occurs when you click on an option that is already selected:
		if(price.isEmpty()) {
			price = "[$0.00]";
		}
		
		System.out.println("Running total: " + expected + ". Applying price: " + price);
		
		//Depending on the price text, either add or subtract the balance
		if (price.charAt(1) == '+') {
			price = price.replace("[+$", "");
			price = price.replace("]", "");
			BigDecimal _price = new BigDecimal(price);
			expected = expected.add(_price);
		} else if(price.charAt(1) == '-') {
			price = price.replace("[-$", "");
			price = price.replace("]", "");
			BigDecimal _price = new BigDecimal(price);
			expected = expected.subtract(_price);
		}
		
		System.out.println("The price is now: " + expected);
		return expected;
	}
	
	public SaveAndReviewPage goToSaveAndReviewPage() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");//Scroll to the bottom to find the button
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finalize")));
		driver.findElement(By.id("finalize")).click();
		
		return new SaveAndReviewPage(driver);
	}
}