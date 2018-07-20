package com.exemplis.pageobjects;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SaveAndReviewPage extends Link {
	private String expectedPageTitle = "ChairBuilder";

	public SaveAndReviewPage(WebDriver driver) {
		super(driver);
		
		//Check if I am in the right page:
		String actualPageTitle = driver.getTitle();
		System.out.print("Continuing breadcrumb: " + actualPageTitle);
		assertEquals(expectedPageTitle, actualPageTitle);
	}

	public MyAccountPage goToMyAccountPage() throws Exception {
		wait = new WebDriverWait(driver, 20);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[class='right-off-canvas-toggle add-to-project'")));
		
		//Add to Project > New Project
		element.click();
		Thread.sleep(2000);
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("new-quote")));
		element.click();
		
		return new MyAccountPage(driver);
	}
}