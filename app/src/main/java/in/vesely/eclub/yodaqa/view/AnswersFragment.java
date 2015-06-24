package in.vesely.eclub.yodaqa.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import in.vesely.eclub.yodaqa.R;
import in.vesely.eclub.yodaqa.adapters.ListRecyclerViewAdapter;
import in.vesely.eclub.yodaqa.restclient.YodaAnswer;
import in.vesely.eclub.yodaqa.restclient.YodaAnswersResponse;


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

    public AnswersFragment() {
    }

    @Override
    protected void responseChanged(YodaAnswersResponse response) {
        adapter.clear();
        if (response != null) {
            adapter.addAll(response.getAnswers());
        }
    }

    @AfterViews
    protected void afterViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}
