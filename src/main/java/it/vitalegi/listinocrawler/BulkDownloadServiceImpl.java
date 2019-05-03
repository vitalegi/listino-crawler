package it.vitalegi.listinocrawler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Service;

@Service
public class BulkDownloadServiceImpl {

	public static final String URL = "PASTE-HERE";
	public static final String SECTOR_INPUT_ID = "_ctl1_cmbSettori";
	public static final String CHAPTER_INPUT_ID = "_ctl1_cmbCapitoli";
	public static final String PARAGRAPH_INPUT_ID = "_ctl1_cmbParagrafi";

	public void scan() {
		WebDriver driver = getDriver();

		driver.get(URL);

		List<String> sectors = getSelectValues(driver, SECTOR_INPUT_ID);
		for (String sector : sectors) {
			if (sector.compareTo(firstSector()) < 0) {
				System.out.println("Skip sector " + sector);
			} else {
				scanSector(driver, sector);
			}
		}

		System.out.println("Selected: " + getSelectedValue(driver, SECTOR_INPUT_ID));

		System.out.println("Page title is: " + driver.getTitle());

		// Close the browser
		driver.quit();
	}

	protected void scanSector(WebDriver driver, String sector) {
		System.out.println("> " + sector);
		sleep();
		selectValue(driver, SECTOR_INPUT_ID, sector);
		List<String> chapters = getSelectValues(driver, CHAPTER_INPUT_ID);
		for (String chapter : chapters) {
			if (sector.compareTo(firstSector()) == 0 //
					&& Integer.parseInt(chapter) < Integer.parseInt(firstChapter())) {

				System.out.println("Skip sector " + sector + " chapter " + chapter);
			} else {
				scanChapter(driver, sector, chapter);
			}
		}
	}

	protected void scanChapter(WebDriver driver, String sector, String chapter) {
		System.out.println(">> " + chapter);
		sleep();
		selectValue(driver, CHAPTER_INPUT_ID, chapter);
		List<String> paragraphs = getSelectValues(driver, PARAGRAPH_INPUT_ID);
		for (String paragraph : paragraphs) {
			if (sector.compareTo(firstSector()) == 0 //
					&& Integer.parseInt(chapter) == Integer.parseInt(firstChapter()) //
					&& Integer.parseInt(paragraph) < Integer.parseInt(firstParagraph())) {
				System.out.println("Skip " + sector + " " + chapter);
			} else {
				scanParagraph(driver, sector, chapter, paragraph);
			}
		}
	}

	protected void scanParagraph(WebDriver driver, String sector, String chapter, String paragraph) {
		System.out.println(">>> " + paragraph);
		sleep();
		selectValue(driver, PARAGRAPH_INPUT_ID, paragraph);
		downloadTxt(driver);
	}

	protected WebDriver getDriver() {
		System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");

		Map<String, Object> chromePrefs = new HashMap<>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", ListinoCrawlerCommandLineApplication.DOWNLOAD_PATH);

		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(ChromeOptions.CAPABILITY, options);

		return new ChromeDriver(options);
	}

	protected void downloadTxt(WebDriver driver) {
		sleep();
		WebElement downloadTxtButton = driver.findElement(By.id("_ctl1_lnkDowTXT"));

		downloadTxtButton.click();
	}

	protected List<String> getSelectValues(WebDriver driver, String id) {
		Select select = new Select(driver.findElement(By.id(id)));
		return select.getOptions()//
				.stream().map(e -> e.getAttribute("value"))//
				.collect(Collectors.toList());
	}

	protected String getSelectedValue(WebDriver driver, String id) {
		Select select = new Select(driver.findElement(By.id(id)));
		return select.getAllSelectedOptions().get(0).getAttribute("value");
	}

	protected void selectValue(WebDriver driver, String id, String value) {
		Select select = new Select(driver.findElement(By.id(id)));
		select.selectByValue(value);
	}

	protected void sleep() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected String firstSector() {
		return "I";
	}

	protected String firstChapter() {
		return "1";
	}

	protected String firstParagraph() {
		return "5";
	}
}
