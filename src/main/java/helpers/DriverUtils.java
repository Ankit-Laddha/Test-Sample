package helpers;

import com.sun.javafx.PlatformUtil;
import com.thoughtworks.gauge.AfterScenario;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverUtils {

    private static final String CHROME = "chrome";
    private static final String INTERNET_EXPLORER = "internetexplorer";
    public static String browserName = System.getenv("browser_name") == null ? "internetexplorer" : System.getenv("browser_name");
    public static boolean cleanCache = System.getenv("clearCache") != null && Boolean.parseBoolean(System.getenv("clearCache"));
    private static DriverUtils instance = new DriverUtils();

    public static ThreadLocal<WebDriver> CURRENT_DRIVER = ThreadLocal.withInitial(() ->  // thread local CURRENT_DRIVER object for webdriver
    {
        if (browserName.equalsIgnoreCase(CHROME)) {
            return createChromeDriver(cleanCache);
        } else if (browserName.equalsIgnoreCase(INTERNET_EXPLORER)) {
            return  createInternetExplorerDriver(cleanCache);
        } else {
            throw new RuntimeException("Unknown WebDriver browser: " + browserName);
        }
    });

    public static DriverUtils getInstance() {
        return instance;
    }

    public WebDriver getDriver() // call this method to get the CURRENT_DRIVER object and launch the browser
    {
        Long callingThreadID = new Long(Thread.currentThread().getId());
        System.out.println("Current thread id :'" + callingThreadID + "', thread name is :" + Thread.currentThread().getName());
        WebDriver driver =  CURRENT_DRIVER.get();
        CURRENT_DRIVER.set(driver);
        return driver;
    }

    @AfterScenario
    public void removeDriver() // Quits the CURRENT_DRIVER and closes the browser
    {
        if (getDriver() != null) {
            CURRENT_DRIVER.get().quit();
        }
        CURRENT_DRIVER.remove();
    }

    private static WebDriver createChromeDriver(boolean clearCache) {
        if (PlatformUtil.isMac()) {
            System.setProperty("webdriver.chrome.driver", System.getenv("webdriver_chrome_driver_mac"));
        }
        if (PlatformUtil.isWindows()) {
            System.setProperty("webdriver.chrome.driver", System.getenv("webdriver_chrome_driver_windows"));
        }
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        ChromeOptions opts = new ChromeOptions();
        if (clearCache) {
            opts.addArguments("--incognito");
        }
        opts.addArguments("disable-extensions");
        opts.addArguments("--window-size=32000,32000");
        capabilities.setCapability(ChromeOptions.CAPABILITY, opts);
        WebDriver driver = new ChromeDriver(opts);
        if (clearCache) {
            driver.get("chrome://extensions-frame");
            WebElement checkbox = driver.findElement(By.xpath("//label[@class='incognito-control']/input[@type='checkbox']"));
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }
        return driver;
    }

    private static WebDriver createInternetExplorerDriver(boolean clearCache) {
        System.setProperty("webdriver.ie.driver", System.getenv("webdriver_ie_driver"));
        InternetExplorerOptions opts = new InternetExplorerOptions();
        opts.ignoreZoomSettings();
        opts.introduceFlakinessByIgnoringSecurityDomains();
        opts.destructivelyEnsureCleanSession();
        opts.addCommandSwitches("--start-maximized");
        return new InternetExplorerDriver(opts);

    }


}

