package in.vesely.eclub.yodaqa.view;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import in.vesely.eclub.yodaqa.R;
import in.vesely.eclub.yodaqa.adapters.ExpandableListAdapter;
import in.vesely.eclub.yodaqa.adapters.ListRecyclerViewAdapter;
import in.vesely.eclub.yodaqa.restclient.YodaAnswer;
import in.vesely.eclub.yodaqa.restclient.YodaAnswersResponse;
import in.vesely.eclub.yodaqa.restclient.YodaSnippet;


@EFragment(R.layout.fragment_answers)
public class AnswersFragment extends ResponseFragment {

    @ViewById(R.id.recyclerView)
    protected RecyclerView recyclerView;
    private ListRecyclerViewAdapter<YodaAnswer, AnswerItem> adapter = new ListRecyclerViewAdapter<YodaAnswer, AnswerItem>() {
        @Override
        protected AnswerItem onCreateItemView(ViewGroup parent, int viewType) {
            return AnswerItem_.build(parent.getContext());
        }
    };

    @ViewById(R.id.Expandable_list)
    protected ExpandableListView expListView;

    private ExpandableListAdapter expandableListAdapter;

    public AnswersFragment() {

    }

    @Override
    protected void responseChanged(YodaAnswersResponse response) {
        expandableListAdapter.clear();
        if (response != null) {
            expandableListAdapter.addAll(response.getAnswers(), response.getSnippets(), response.getSources());
        }
    }

    @AfterViews
    protected void afterViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        expandableListAdapter = new ExpandableListAdapter(getActivity(), null, null);
        expListView.setAdapter(expandableListAdapter);
        //TODO repair indicator to right
        indicatorToRight();
    }

    public void indicatorToRight() {
        int drawable_width = 30;

        if(android.os.Build.VERSION.SDK_INT <
                android.os.Build.VERSION_CODES.JELLY_BEAN_MR2){
            expListView.setIndicatorBounds(
                    expListView.getWidth()-drawable_width,
                    expListView.getWidth());
        }else{
            expListView.setIndicatorBoundsRelative(
                    expListView.getWidth()-drawable_width,
                    expListView.getWidth());
        }
    }
}
