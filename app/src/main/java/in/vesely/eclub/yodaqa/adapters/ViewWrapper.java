package in.vesely.eclub.yodaqa.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vesely on 2/3/15.
 */
public class ViewWrapper<T, V extends View & Binder<T>> extends RecyclerView.ViewHolder {

    private V view;

    public ViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }


}
