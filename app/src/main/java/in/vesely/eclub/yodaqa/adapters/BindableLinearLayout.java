package in.vesely.eclub.yodaqa.adapters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by vesely on 2/3/15.
 */
public abstract class BindableLinearLayout<T> extends LinearLayout implements ViewWrapper.Binder<T> {
    public BindableLinearLayout(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public BindableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
