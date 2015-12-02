package in.vesely.eclub.yodaqa.adapters;

import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

import in.vesely.eclub.yodaqa.adapters.expandable_recyclerview.BindableExpandableRecyclerViewAdapter;
import in.vesely.eclub.yodaqa.restclient.SnippetSourceContainer;
import in.vesely.eclub.yodaqa.restclient.YodaAnswer;
import in.vesely.eclub.yodaqa.view.AnswerItem;
import in.vesely.eclub.yodaqa.view.AnswerItem_;
import in.vesely.eclub.yodaqa.view.SnippetItem;
import in.vesely.eclub.yodaqa.view.SnippetItem_;

/**
 * Created by vesely on 11/29/15.
 */
public class AnswersExpandableRecyclerViewAdapter extends BindableExpandableRecyclerViewAdapter<Boolean, YodaAnswer, AnswerItem, SnippetSourceContainer, SnippetItem> {
    public AnswersExpandableRecyclerViewAdapter(List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
    }

    @Override
    protected AnswerItem onCreateParentItemView(ViewGroup parent) {
        return AnswerItem_.build(parent.getContext());
    }

    @Override
    protected SnippetItem onCreateChildItemView(ViewGroup parent) {
        return SnippetItem_.build(parent.getContext());
    }

    public void clearAll() {
        int i = getParentItemList().size();
        getParentItemList().clear();
        for (int j = i - 1; j >= 0; j--) {
            notifyParentItemRemoved(j);
        }
    }
}
