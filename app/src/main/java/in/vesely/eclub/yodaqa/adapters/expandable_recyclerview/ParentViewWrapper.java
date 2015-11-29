package in.vesely.eclub.yodaqa.adapters.expandable_recyclerview;

import android.view.View;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

/**
 * Created by vesely on 11/29/15.
 */
public class ParentViewWrapper<T, V extends View & ParentBinder<T>> extends ParentViewHolder {

    private V view;

    public ParentViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        view.onExpansionToggled(expanded);
    }
}
