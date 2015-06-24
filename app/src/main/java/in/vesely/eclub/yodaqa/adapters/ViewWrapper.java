package in.vesely.eclub.yodaqa.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vesely on 2/3/15.
 */
public class ViewWrapper<T, V extends View & ViewWrapper.Binder<T>> extends RecyclerView.ViewHolder {

    private V view;

    public ViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }

    public interface Binder<T> {
        void bind(T data, int position);
    }
}
