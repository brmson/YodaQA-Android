package in.vesely.eclub.yodaqa.bus;

import in.vesely.eclub.yodaqa.restclient.YodaAnswersResponse;

/**
 * Created by vesely on 6/16/15.
 */
public class ResponseChangedAction {
    private final YodaAnswersResponse response;

    public ResponseChangedAction(YodaAnswersResponse response) {
        this.response = response;
    }

    public YodaAnswersResponse getResponse() {
        return response;
    }
}
