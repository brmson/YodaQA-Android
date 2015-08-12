package in.vesely.eclub.yodaqa.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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
            ArrayList<YodaSource> yodaSources= new ArrayList<>(response.getSources().values());
            ArrayList<YodaSource> arrayListSources=new ArrayList<>();
            for(int i=0;i<yodaSources.size();i++){
                boolean founded=false;
                YodaSource yodaSource=yodaSources.get(i);
                if (!yodaSource.getOrigin().equals("title-in-clue")){
                    String title=yodaSource.getTitle();
                    String type=yodaSource.getType();
                    String origin=yodaSource.getOrigin();
                    for (int j=0;j<arrayListSources.size();j++){
                        YodaSource s=arrayListSources.get(j);
                        if(s.getTitle().equals(title) && s.getType().equals(type)){
                            s.setOrigin(s.getOrigin() + ", " + origin);
                            founded=true;
                            break;
                        }
                    }
                    if(!founded){
                        arrayListSources.add(yodaSource);
                    }
                }
            }
            adapter.addAll(arrayListSources);
        }
    }

    @AfterViews
    protected void afterViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }
}
