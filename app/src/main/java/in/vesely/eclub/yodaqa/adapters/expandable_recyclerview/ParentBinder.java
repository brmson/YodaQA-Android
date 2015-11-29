package in.vesely.eclub.yodaqa.adapters.expandable_recyclerview;

public interface ParentBinder<T> extends ExpansionListener {
    void bind(T data, int position, boolean isExpanded);
}
