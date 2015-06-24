package in.vesely.eclub.yodaqa.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vesely on 2/3/15.
 */
public abstract class RecyclerViewAdapterBase<T, V extends View & ViewWrapper.Binder<T>> extends RecyclerView.Adapter<ViewWrapper<T, V>> {

    @Override
    public final ViewWrapper<T, V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<T, V>(onCreateItemView(parent, viewType));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(ViewWrapper<T, V> viewHolder, int position) {
        V view = viewHolder.getView();
        T data = getItem(position);
        view.bind(data, recalculateBindPosition(position));
    }

    protected int recalculateBindPosition(int position) {
        return position;
    }

    protected abstract T getItem(int position);
}
