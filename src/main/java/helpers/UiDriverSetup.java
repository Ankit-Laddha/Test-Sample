package helpers;

import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.ExecutionContext;

public class UiDriverSetup {

    @BeforeScenario(tags = {"Ui"})
    public void setup(ExecutionContext context){
        DriverUtils.getInstance().getDriver().get("https://www.medium.com");
    }

}
