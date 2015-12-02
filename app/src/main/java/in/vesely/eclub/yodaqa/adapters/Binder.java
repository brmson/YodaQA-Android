package in.vesely.eclub.yodaqa.adapters;

public interface Binder<T> {
    void bind(T data, int position);
}
