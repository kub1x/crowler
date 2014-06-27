package cz.sio2.crowler.selenium;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebDriverConfiguration {

	public static void doIt(String[] args) {
		WebDriver wd = new FirefoxDriver();
		wd.get("url");

		wd.manage().window().maximize();

		List<WebElement> list = new ArrayList<>();

		String id = "";
		list = wd.findElements(By.id(id));

		String xpath = "";
		list = wd.findElements(By.xpath(xpath));

		wd.close(); // quit()
	}
}
