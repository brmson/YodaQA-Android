package in.vesely.eclub.yodaqa.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import in.vesely.eclub.yodaqa.R;
import in.vesely.eclub.yodaqa.adapters.ListRecyclerViewAdapter;
import in.vesely.eclub.yodaqa.restclient.YodaAnswersResponse;
import in.vesely.eclub.yodaqa.restclient.YodaSource;


/**
 * A placeholder fragment containing a simple view.
 */
@EFragment(R.layout.fragment_sources)
public class SourcesFragment extends ResponseFragment {

    @ViewById(R.id.recyclerView)
    protected RecyclerView recyclerView;
    private ListRecyclerViewAdapter<YodaSource, SourceItem> adapter = new ListRecyclerViewAdapter<YodaSource, SourceItem>() {
        @Override
        protected SourceItem onCreateItemView(ViewGroup parent, int viewType) {
            return SourceItem_.build(parent.getContext());
        }
    };

    public SourcesFragment() {
    }

    @Override
    protected void responseChanged(YodaAnswersResponse response) {
        adapter.clear();
        if(response!=null) {
            adapter.addAll(response.getSources());
        }
    }

    @AfterViews
    protected void afterViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }
}
