package com.exemplis.pageobjects;

import static org.junit.Assert.assertEquals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeriesPage extends Link {
	private String expectedPageTitle = "ChairBuilder";

	public SeriesPage(WebDriver driver) {
		super(driver);
		
		//Check if I am in the right page:
		String actualPageTitle = driver.getTitle();
		System.out.print(" > " + actualPageTitle);
		assertEquals(expectedPageTitle, actualPageTitle);
	}
	
	public ChairBuilder goToChairBuilder(String[] crumb) {
		wait = new WebDriverWait(driver, 20);
		WebElement child = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt='" + crumb[crumb.length - 1] + "'")));
		
		//Get base price:
		WebElement parent = child.findElement(By.xpath(".."));
		WebElement grandparent = parent.findElement(By.xpath(".."));
		String base = grandparent.findElement(By.cssSelector("li:nth-child(3) > span")).getText();
		base = base.replace("Starting at $", "");
		base += ".00";
		
		//Click on the picture:
		driver.findElement(By.cssSelector("img[alt='" + crumb[crumb.length - 1] + "'")).click();
		
		return new ChairBuilder(driver, crumb, base);
	}
}