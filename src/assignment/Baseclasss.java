package assignment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.bidi.module.Browser;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Baseclasss {

	List<String> urls = new ArrayList<>();
	List<String> resolutions = new ArrayList<>();
	List<WebDriver> browsers = new ArrayList<>();
	static final String baseUrl = "https://www.getcalley.com/page-sitemap.xml";

	public Baseclasss() {
		this.resolutions.add("1920x1080");
		this.resolutions.add("1366x768");
		this.resolutions.add("1536x864");
	}

	void runScript(WebDriver browser) {
		resolutions.forEach((resolution -> {
			int width = Integer.parseInt(resolution.split("x")[0]);
			int height = Integer.parseInt(resolution.split("x")[1]);
			browser.manage().window().setSize(new Dimension(width, height));
			browser.get(baseUrl);

			// finding top 5 urls
			for (int i = 0; i <= 4; i++) {
				List<WebElement> anchors = browser.findElements(By.xpath("//table/tbody/tr/td/a"));
				System.out.println(i);
				String urlString = anchors.get(i).getText();
				browser.get(urlString);
				TakesScreenshot ts = (TakesScreenshot) browser;
				File src = ts.getScreenshotAs(OutputType.FILE);

				Capabilities caps = ((RemoteWebDriver) browser).getCapabilities();
				File fileDestination = new File(String.join("-", "./screenshots/ss-", caps.getBrowserName(),
						resolution.toString(), String.valueOf(i), ".png"));
				try {
					FileUtils.copyFile(src, fileDestination);
				} catch (IOException e) {
					e.printStackTrace();
				}
				browser.navigate().back();
			}
		
		}));
		browser.close();
	}

	public static void main(String[] args) {
		
		Baseclasss baseclasss = new Baseclasss();
//		baseclasss.runScript(new FirefoxDriver());
		baseclasss.runScript(new ChromeDriver());
	}

}
