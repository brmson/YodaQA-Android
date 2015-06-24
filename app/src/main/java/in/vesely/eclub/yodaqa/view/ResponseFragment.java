package in.vesely.eclub.yodaqa.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import in.vesely.eclub.yodaqa.R;
import in.vesely.eclub.yodaqa.bus.OttoBus;
import in.vesely.eclub.yodaqa.bus.RequestUpdateAction;
import in.vesely.eclub.yodaqa.bus.ResponseChangedAction;
import in.vesely.eclub.yodaqa.restclient.YodaAnswersResponse;

/**
 * Created by vesely on 6/16/15.
 */
@EFragment
public abstract class ResponseFragment extends Fragment {

    private static final String REFRESH_STATE = "refresh_state";
    private Boolean refreshing = null;

    @FragmentArg
    @InstanceState
    protected YodaAnswersResponse response;

    @Bean
    protected OttoBus bus;

    @ViewById(R.id.refreshView)
    protected SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
        if (savedInstanceState != null && savedInstanceState.containsKey(REFRESH_STATE)) {
            refreshing = savedInstanceState.getByte(REFRESH_STATE) == 1;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Subscribe
    public void setResponse(ResponseChangedAction action) {
        response = action.getResponse();
        responseChanged(response);
        if (refreshLayout != null && response != null) {
            refreshLayout.setRefreshing(!response.isFinished());
        }
    }

    @Subscribe
    public void onRequestRefreshResponse(RequestUpdateAction action) {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (refreshLayout != null) {
            outState.putByte(REFRESH_STATE, (byte) (refreshLayout.isRefreshing() ? 1 : 0));
        }
    }

    @AfterViews
    protected void initSwipeRefreshLayout() {
        refreshLayout.setEnabled(false);
        if (refreshing != null) {

            //TODO this is hack to show refreshing indicator before onMeasure is called. Remove this hack after this bug is resolved https://code.google.com/p/android/issues/detail?id=77712
            TypedValue typed_value = new TypedValue();
            getActivity().getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, typed_value, true);
            refreshLayout.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));

            refreshLayout.setRefreshing(refreshing);
            refreshing = null;
        }
    }

    protected abstract void responseChanged(YodaAnswersResponse response);
}
