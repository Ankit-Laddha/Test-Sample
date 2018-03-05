package helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Page {

    protected WebDriver driver;
    final int TIMEOUT_IN_SECONDS = 30;//This should come from your properties file

    public Page() {
        driver = DriverUtils.getInstance().getDriver();
    }

    protected Page initElements(Object instance) {
        PageFactory.initElements(driver, instance);
        return this;
    }

    public Page(WebDriver driver) {
        this.driver = driver;
    }

    protected Page visit(String url) {
        driver.navigate().to(url);
        waitForPageLoad();
        return this;
    }

    protected Page click(WebElement element) {
        waitUntilElementIsClickable(element);
        element.click();
        return this;
    }

    protected Page doubleClick(WebElement element, String itemName) throws ElementNotVisibleException, InterruptedException {
        waitUntilElementIsClickable(element, itemName);
        Actions builder = new Actions(driver);
        builder.doubleClick(element).perform();
        return this;
    }

    public boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException ex) {
            return false;
        }
    }

    public void sleepFor(int sleepTimeInSecs)
    {
        try {
            Thread.sleep(sleepTimeInSecs*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean areElementsDisplayed(List<WebElement> elements) {
        return elements.size() > 0
                && elements.stream().allMatch(webElement -> isElementDisplayed(webElement));
    }

    public Page waitUntilElementIsDisplayed(WebElement element, Object...information) {
        String eleDesc = getMeElementDescription(information);
        int timeout = getMeTimeout(information);

        try {
            FluentWait<WebElement> wait = new FluentWait<>(element);
            wait.withTimeout(timeout, TimeUnit.SECONDS);
            wait.until(webElement -> isElementDisplayed(webElement));
        }catch (TimeoutException ex)
        {
            throw new WaitException(eleDesc, WaitException.ForElement.IS_STILL_NOT_VISIBILE, timeout);
        }
        return this;
    }

    public Page waitUntilElementsAreDisplayed(List<WebElement> elements, Object...information) {
        String eleDesc = getMeElementDescription(information);
        int timeout = getMeTimeout(information);

        try {
            FluentWait<List<WebElement>> wait = new FluentWait<>(elements);
            wait.withTimeout(timeout, TimeUnit.SECONDS);
            wait.until(webElements -> areElementsDisplayed(webElements));
        }catch (TimeoutException ex) {
            throw new WaitException(eleDesc, WaitException.ForElement.IS_STILL_NOT_VISIBILE, timeout);
        }
        return this;
    }

    public boolean isElementClickable(final WebElement element) {
        return isElementDisplayed(element) && element.isEnabled();
    }

    public Page waitUntilElementIsClickable(WebElement element, Object...information) {
        String eleDesc = getMeElementDescription(information);
        int timeout = getMeTimeout(information);

        try {
            FluentWait<WebElement> wait = new FluentWait<>(element);
            wait.withTimeout(timeout, TimeUnit.SECONDS);
            wait.until(webElement -> isElementClickable(webElement));
        }catch (TimeoutException ex){
            throw new WaitException(eleDesc, WaitException.ForElement.IS_STILL_NOT_CLICKABLE, timeout);
        }
        return this;
    }

    public boolean isElementNotDisplayed(WebElement element) {
        try {
            return !element.isDisplayed();
        } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException ex) {
            // Assuming a stale element isn't displayed.
            return true;
        }
    }

    public Page waitUntilElementIsNotDisplayed(WebElement element, Object...information) {
        String eleDesc = getMeElementDescription(information);
        int timeout = getMeTimeout(information);

        try {
            FluentWait<WebElement> wait = new FluentWait<>(element);
            wait.withTimeout(timeout, TimeUnit.SECONDS);
            wait.until(webElement -> isElementNotDisplayed(webElement));
        }catch (TimeoutException ex){
            throw new WaitException(eleDesc, WaitException.ForElement.IS_STILL_VISIBLE, timeout);
        }
        return this;
    }

    public boolean areElementsNotDisplayed(List<WebElement> elements) {
        return elements.stream().allMatch(ele -> isElementNotDisplayed(ele));
    }

    public Page waitUntilElementsAreNotDisplayed(List<WebElement> elements, Object...information) {
        String eleDesc = getMeElementDescription(information);
        int timeout = getMeTimeout(information);

        try {
            FluentWait<List<WebElement>> wait = new FluentWait<>(elements);
            wait.withTimeout(30, TimeUnit.SECONDS);
            wait.until(webElements -> areElementsNotDisplayed(webElements));
        }catch (TimeoutException ex){
            throw new WaitException(eleDesc, WaitException.ForElement.IS_STILL_VISIBLE, timeout);
        }
        return this;
    }

    public String getMeElementDescription(Object... information) {
        String elementDescription = (String) Arrays.stream(information)
                .filter(info -> info instanceof String)
                .findFirst().orElse(null);
        if (elementDescription == null)
            return "No ForElement Description Found";
        else
            return elementDescription;
    }

    public int getMeTimeout(Object... information) {
        Integer timeoutInSeconds = (Integer) Arrays.stream(information).filter(info -> info instanceof Integer)
                .findFirst().orElse(null);
        if (timeoutInSeconds == null)
            return TIMEOUT_IN_SECONDS;
        else
            return timeoutInSeconds;
    }

    //--------------------------------------------------------------------------------------------------

    public void highlightElement(WebElement element) {
        for (int i = 0; i < 5; i++) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: yellow; border: 2px solid yellow;");
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
        }
    }

    public Page waitForPageLoadJQuery() {

        ExpectedCondition<Boolean> pageLoadCondition = driver -> {
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        return (js.executeScript("return document.readyState").equals("complete")
                                && (js.executeScript("return window.jQuery != undefined && jQuery.active === 0").equals(true)));
                };
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_IN_SECONDS);
        wait.until(pageLoadCondition);
        return this;
    }

    protected Page waitForPageLoad() {
        ExpectedCondition<Boolean> pageLoadCondition = driver -> {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return (js.executeScript("return document.readyState").equals("complete"));
        };
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_IN_SECONDS);
        wait.until(pageLoadCondition);
        return this;
    }


    protected void scrollIntoView(WebElement element) {
        JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
        jsDriver.executeScript("arguments[0].scrollIntoView(false);", element);
    }

    protected void scrollIntoViewByYLoc(WebElement element) {
        JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
        jsDriver.executeScript("window.scrollTo(0, [0]);", element.getLocation().getY());
    }

    protected WebDriver switchToIframeWindow(WebElement frame) {
        return driver.switchTo().frame(frame);
    }

    protected WebDriver switchToParentWindow() {
        return driver.switchTo().parentFrame();
    }

    protected WebDriver switchToNextWindow() {
        driver.getWindowHandles().toArray();
        Object[] allWindowNames = driver.getWindowHandles().toArray();
        String lastWindow = (String) allWindowNames[allWindowNames.length - 1];
        return driver.switchTo().window(lastWindow);
    }


}
