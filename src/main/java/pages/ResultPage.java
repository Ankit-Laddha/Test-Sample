package pages;

import helpers.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ResultPage extends Page{
    public String title;

    public ResultPage() {
        initElements(this);
    }

    public ResultPage(String title) {
        initElements(this);
        this.title = title;
    }

    public boolean isReady() {
        waitForPageLoad().waitUntilElementIsDisplayed(weResultTitle);
        return true;
    }

    @FindBy(className = "hero-title")
    protected WebElement weResultTitle;

    public String getTitleText()
    {
        System.out.println("weResultTitle.getText() = " + weResultTitle.getText());
        return weResultTitle.getText();
    }
}
