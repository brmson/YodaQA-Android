package in.vesely.eclub.yodaqa.adapters.expandable_recyclerview;

import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

import in.vesely.eclub.yodaqa.adapters.Binder;

/**
 * Created by vesely on 11/29/15.
 */
public abstract class BindableExpandableRecyclerViewAdapter<T, V extends View & ParentBinder<T>, T2, V2 extends View & Binder<T2>> extends ExpandableRecyclerAdapter<ParentViewWrapper<T, V>, ChildViewWrapper<T2, V2>> {

    public BindableExpandableRecyclerViewAdapter(List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
    }

    @Override
    public ParentViewWrapper<T, V> onCreateParentViewHolder(ViewGroup parentViewGroup) {
        return new ParentViewWrapper<T, V>(onCreateParentItemView(parentViewGroup));
    }

    @Override
    public ChildViewWrapper<T2, V2> onCreateChildViewHolder(ViewGroup childViewGroup) {
        return new ChildViewWrapper<T2, V2>(onCreateChildItemView(childViewGroup));
    }

    protected abstract V onCreateParentItemView(ViewGroup parent);

    protected abstract V2 onCreateChildItemView(ViewGroup parent);

    @Override
    @SuppressWarnings("unchecked")
    public void onBindParentViewHolder(ParentViewWrapper<T, V> parentViewHolder, int position, ParentListItem parentListItem) {
        parentViewHolder.getView().bind((T) parentListItem, position, parentViewHolder.isExpanded());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindChildViewHolder(ChildViewWrapper<T2, V2> childViewHolder, int position, Object childListItem) {
        childViewHolder.getView().bind((T2) childListItem, position);
    }
}
