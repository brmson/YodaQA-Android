package in.vesely.eclub.yodaqa.restclient;

import android.os.AsyncTask;

import org.springframework.core.NestedRuntimeException;

import java.util.Map;

import in.vesely.eclub.yodaqa.bus.OttoBus;
import in.vesely.eclub.yodaqa.bus.ResponseChangedAction;

/**
 * Created by vesely on 6/16/15.
 */
public class YodaExecuter extends AsyncTask<String, YodaAnswersResponse, YodaAnswersResponse> {
    private OttoBus bus;
    private YodaRestClient restClient;
    private Map<String, String> ids;

    public YodaExecuter(OttoBus bus, YodaRestClient restClient, Map<String, String> ids) {
        this.bus = bus;
        this.restClient = restClient;
        this.ids = ids;
    }

    @Override
    protected void onProgressUpdate(YodaAnswersResponse... values) {
        super.onProgressUpdate(values);
        bus.post(new ResponseChangedAction(values[0]));
    }

    @Override
    protected YodaAnswersResponse doInBackground(String... params) {
        try {
            YodaUtils.setContentType(restClient);
            String id = ids.get(params[0]);
            if (id == null) {
                id = restClient.getId(YodaUtils.getQuestionPostData(params[0]));
                ids.put(params[0], id);
            }
            YodaAnswersResponse response = null;
            while (true) {
                response = restClient.getResponse(id);
                if (response != null && response.isFinished()) {
                    return response;
                }
                if (isCancelled()) {
                    return null;
                }
                if (response != null) {
                    publishProgress(response);
                }
                try {
                    synchronized (this) {
                        wait(500);
                    }
                } catch (InterruptedException e) {
                }
                if (isCancelled()) {
                    return null;
                }
            }
        } catch (NestedRuntimeException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(YodaAnswersResponse yodaAnswersResponse) {
        super.onPostExecute(yodaAnswersResponse);
        if (yodaAnswersResponse != null) {
            bus.post(new ResponseChangedAction(yodaAnswersResponse));
        }
    }
}
