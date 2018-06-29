package com.exemplis.pageobjects;

import static org.junit.Assert.fail;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SeriesPage extends Link {

	public SeriesPage(WebDriver driver) {
		super(driver);
		
		System.out.println("You are in: " + driver.getTitle());
		if(!driver.getTitle().equals("ChairBuilder")) {
			//throw new Exception("You are in the wrong page");//TODO: Implement Exception handling
			fail("You are in the wrong page");
		}
	}
	
	public ChairBuilder goToChairBuilder() throws Exception {
		//getLinkByAltText("Highback Mesh Black Frame");
		driver.findElement(By.cssSelector("img[alt='Highback Mesh Black Frame'")).click();
		title = driver.getTitle();//TODO: Determine if I need this
		Thread.sleep(3000);//Let the page load prior to going to the next page
		
		return new ChairBuilder(driver);
	}
	
	public void login(){
		driver.findElement(By.className("login-link")).click();
		driver.findElement(By.name("username")).sendKeys("kpamittan");
		driver.findElement(By.name("password")).sendKeys("");
		driver.findElement(By.cssSelector("button[class='button secondary submit-login-btn '")).click();
		
		//TODO: Assert that you are logged in
	}
}
