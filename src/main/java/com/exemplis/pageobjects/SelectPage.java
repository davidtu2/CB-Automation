package com.exemplis.pageobjects;

import static org.junit.Assert.fail;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SelectPage extends Link {

	public SelectPage(WebDriver driver) {
		super(driver);
		
		System.out.println("You are in: " + driver.getTitle());
		if(!driver.getTitle().equals("ChairBuilder")) {
			//throw new Exception("You are in the wrong page");//TODO: Implement Exception handling
			fail("You are in the wrong page");
		}
	}
	
	public SeriesPage goToSeriesPage() throws Exception {
		//getLinkByAltText("Novo");
		driver.findElement(By.cssSelector("img[alt='Novo'")).click();
		title = driver.getTitle();//TODO: Determine if I need this
		Thread.sleep(3000);//Let the page load prior to going to the next page
		
		return new SeriesPage(driver);
	}
	
	public void login(){
		driver.findElement(By.className("login-link")).click();
		driver.findElement(By.name("username")).sendKeys("kpamittan");
		driver.findElement(By.name("password")).sendKeys("");
		driver.findElement(By.cssSelector("button[class='button secondary submit-login-btn '")).click();
		
		//TODO: Assert that you are logged in
	}
}
