package com.test.set1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Link {
	private WebDriver driver;
	private String title;
	private String breadcrumb;

	public Link(WebDriver driver) {
		this.driver = driver;
	}
	
	public Link(WebDriver driver, String title, String breadcrumb) {
		this.driver = driver;
		this.title = title;
		this.breadcrumb = breadcrumb;
	}
	
	//Notice: This method can be brittle from internationalization
	public Link getLinkByLinkText(String next) {
		driver.findElement(By.linkText(next)).click();
		
		return new Link(driver);
	}
	
	public Link getLinkByClass(String name) {
		driver.findElement(By.cssSelector("a[class='" + name + "'")).click();
		
		return new Link(driver);
	}
	
	public Link getLinkByAltText(String name) {
		driver.findElement(By.cssSelector("img[alt='" + name + "'")).click();
		title = driver.getTitle();
		
		return new Link(driver, title, name);
	}
	
	public String title() {
		return title;
	}
	
	public String breadcrumb() {
		return breadcrumb;
	}

}
