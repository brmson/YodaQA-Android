package in.vesely.eclub.yodaqa.restclient;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;

import in.vesely.eclub.yodaqa.bus.OttoBus;
import in.vesely.eclub.yodaqa.bus.ResponseChangedAction;

/**
 * Created by vesely on 6/15/15.
 */
@EBean
public class YodaErrorHandler implements RestErrorHandler {

    @RootContext
    protected Context ctx;

    @Bean
    protected OttoBus bus;

    @Override
    public void onRestClientExceptionThrown(final NestedRuntimeException e) {
        Handler mainHandler = new Handler(ctx.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
                bus.post(new ResponseChangedAction(null));
            }
        });
        throw e;
    }
}
