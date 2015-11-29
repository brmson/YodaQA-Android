package in.vesely.eclub.yodaqa.adapters.expandable_recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by vesely on 2/3/15.
 */
public abstract class ParentBindableLinearLayout<T> extends LinearLayout implements ParentBinder<T> {
    public ParentBindableLinearLayout(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public ParentBindableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
