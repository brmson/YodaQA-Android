package in.vesely.eclub.yodaqa.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import in.vesely.eclub.yodaqa.R;
import in.vesely.eclub.yodaqa.adapters.AnswersExpandableRecyclerViewAdapter;
import in.vesely.eclub.yodaqa.restclient.YodaAnswer;
import in.vesely.eclub.yodaqa.restclient.YodaAnswersResponse;
import in.vesely.eclub.yodaqa.view.utils.SimpleDividerItemDecoration;


@EFragment(R.layout.fragment_answers)
public class AnswersFragment extends ResponseFragment {

    @ViewById(R.id.recyclerView)
    protected RecyclerView recyclerView;
    protected List<YodaAnswer> answers;
    private AnswersExpandableRecyclerViewAdapter adapter;
    private Bundle savedData;

    public AnswersFragment() {
    }

    @Override
    protected void responseChanged(YodaAnswersResponse response) {
        if (response == null) {
            adapter.clearAll();
            return;
        }
        adapter.setGlobalData(response.isFinished());
        List<YodaAnswer> newAnswers = response.getAllAnswers();
        int i;
        for (i = 0; i < answers.size() && i < newAnswers.size(); i++) {
            answers.set(i, newAnswers.get(i));
            adapter.notifyParentItemChanged(i);
        }
        if (i < answers.size())/*newAnswers has less items*/ {
            for (int j = answers.size() - 1; j >= i; j--) {
                answers.remove(j);
                adapter.notifyParentItemRemoved(j);
            }
        } else if (i < newAnswers.size())/*newAnswers has more items*/ {
            for (int j = i; j < newAnswers.size(); j++) {
                answers.add(newAnswers.get(j));
                adapter.notifyParentItemInserted(j);
            }
        }
        if (savedData != null) {
            adapter.onRestoreInstanceState(savedData);
            savedData = null;
        }
    }

    @AfterViews
    protected void afterViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        answers = new ArrayList<>();
        adapter = new AnswersExpandableRecyclerViewAdapter(answers);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedData = savedInstanceState;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!refreshLayout.isRefreshing()) {
            adapter.onSaveInstanceState(outState);
        }
    }
}
