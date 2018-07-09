package com.exemplis.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SaveAndReviewPage extends Link {

	public SaveAndReviewPage(WebDriver driver) {
		super(driver);
	}

	public MyAccountPage generateNewProject() throws Exception {
		//Let the page load prior to going to the next page (Let the chair graphics load)
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[class='right-off-canvas-toggle add-to-project'")));
		
		//Add to Project > New Project
		driver.findElement(By.cssSelector("button[class='right-off-canvas-toggle add-to-project'")).click();
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("new-quote")));
		element.click();
		//driver.findElement(By.className("new-quote")).click();
		
		return new MyAccountPage(driver);
	}
}