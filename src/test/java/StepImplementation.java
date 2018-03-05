import com.thoughtworks.gauge.Step;
import pages.Homepage;
import pages.ResultPage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class StepImplementation {

    ResultPage resultPage;

    @Step("User is on app's homepage")
    public void openHomepage() {
        assertThat("Page is not ready", new Homepage().isReady(), is(true));
    }

    @Step("Then user should see search icon but not search textbox")
    public void assertSearchIconAndTextbox() {
        assertThat("Search button is not available", new Homepage().isSearchButtonAvailable(), is(true));
        assertThat("Search box is available", new Homepage().isSearchBoxAvailable(), is(false));
    }

    @Step("Search for keyword <Ada>")
    public void searchFor(String keywordToEnter) {
        Homepage homepage = new Homepage();
        homepage.searchForKeyword(keywordToEnter);
    }

    @Step("Application lists all the people results matching the keyword <Ada>")
    public void assertResults(String keyword) {
        Homepage homepage = new Homepage();
        assertThat("Search Results do not match", homepage.areResultsAsPerSearchCriteria(keyword), is(true));
    }

    @Step("Click on first result from people section")
    public void implementation1() {
        Homepage homepage = new Homepage();
        resultPage = homepage.clickOnFirstLinkOfResult();
        resultPage.isReady();
    }


    @Step("App shows the result page corresponding to clicked link")
    public void correspondingResult() {
        assertThat("Title is not correct", resultPage.title.equalsIgnoreCase(resultPage.getTitleText()));
    }
}
