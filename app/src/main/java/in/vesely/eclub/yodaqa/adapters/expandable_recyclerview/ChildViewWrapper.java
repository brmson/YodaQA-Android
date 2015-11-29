package in.vesely.eclub.yodaqa.adapters.expandable_recyclerview;

import android.view.View;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import in.vesely.eclub.yodaqa.adapters.Binder;

/**
 * Created by vesely on 11/29/15.
 */
public class ChildViewWrapper<T, V extends View & Binder<T>> extends ChildViewHolder {

    private V view;

    public ChildViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }

}
