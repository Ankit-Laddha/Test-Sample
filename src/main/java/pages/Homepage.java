package pages;

import helpers.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class Homepage extends Page{

    @FindBy(css = ".metabar-predictiveSearch.is-touched input.js-predictiveSearchInput")
    protected WebElement searchTextBox;

    @FindBy(className = "svgIcon--search")
    protected WebElement searchBtn;

    @FindBy(className = "js-predictiveSearchResults")
    protected WebElement searchResultsPane;

    @FindBy(css = ".popover-inner span.avatar-text")
    protected List<WebElement> searchResultsAvatarText;


    public Homepage() {
        initElements(this);
    }

    public boolean isReady() {
        waitForPageLoad().waitUntilElementIsDisplayed(searchBtn, "Search Button");
        return true;
    }

    public boolean isSearchButtonAvailable()
    {
        return isElementDisplayed(searchBtn);
    }

    public boolean isSearchBoxAvailable()
    {
        return isElementDisplayed(searchTextBox);
    }

    public Homepage searchForKeyword(String keywordToSearch)
    {
        click(searchBtn);
        waitUntilElementIsDisplayed(searchTextBox);
        searchTextBox.click();
        searchTextBox.clear();
        searchTextBox.sendKeys(keywordToSearch);
        waitUntilElementIsDisplayed(searchResultsPane);
        return this;
    }

    public boolean areResultsAsPerSearchCriteria(String keyword)
    {
        searchResultsAvatarText.forEach(element -> System.out.println(element.getText()));
        return searchResultsAvatarText.stream().allMatch(p-> p.getText().toLowerCase()
                .contains(keyword.toLowerCase()));
    }

    public ResultPage clickOnFirstLinkOfResult()
    {
        WebElement element = searchResultsAvatarText.stream().findFirst().orElse(null);
        String lnkTxt =element.getText();
        click(element);
        return new ResultPage(lnkTxt);
    }



}
