package helpers;

public class WaitException extends RuntimeException{

    public interface ForElement {

        String IS_STILL_NOT_VISIBILE = "was not displayed";
        String IS_STILL_NOT_CLICKABLE = "was not clickable";
        String IS_STILL_VISIBLE = "was still displayed";
        String IS_STILL_CLICKABLE = "was still clickable";
    }


    public WaitException(String elementDesc, String waitType, Integer durationInSeconds) {
        super(String.format("Element(s) '%s' %s after waiting for %d seconds", elementDesc, waitType, durationInSeconds));
    }

    public WaitException(String elementDesc, String waitType,  Integer durationInSeconds, Exception e) {
        super(String.format("Element(s) '%s' %s after waiting for %d seconds, Exception: %s", elementDesc, waitType, durationInSeconds, e.getMessage()));
    }

    public WaitException(String elementDesc, String waitType) {
        super(String.format("Element(s) '%s' %s", elementDesc,waitType));
    }
}
