package in.vesely.eclub.yodaqa.adapters.expandable_recyclerview;

public interface ParentBinder<T, B> extends ExpansionListener {
    void bind(T data, int position, boolean isExpanded, B globalData);
}
