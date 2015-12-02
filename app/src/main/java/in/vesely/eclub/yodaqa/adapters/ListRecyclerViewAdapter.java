package in.vesely.eclub.yodaqa.adapters;

import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by vesely on 2/3/15.
 */
public abstract class ListRecyclerViewAdapter<T, V extends View & Binder<T>> extends RecyclerViewAdapterBase<T, V> {

    protected List<T> items = new ArrayList<T>();

    @Override
    protected T getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(T item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void addAll(Collection<T> collection) {
        items.addAll(collection);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
    }
}
