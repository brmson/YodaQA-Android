package in.vesely.eclub.yodaqa.view;

import android.content.Context;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import in.vesely.eclub.yodaqa.R;
import in.vesely.eclub.yodaqa.adapters.BindableLinearLayout;
import in.vesely.eclub.yodaqa.restclient.YodaAnswer;
import in.vesely.eclub.yodaqa.restclient.YodaAnswerItem;
import in.vesely.eclub.yodaqa.utils.ColorUtils;

/**
 * Created by vesely on 6/16/15.
 */
@EViewGroup(R.layout.answer_item)
public class AnswerItem extends BindableLinearLayout<YodaAnswer> {

    @ViewById(R.id.text)
    protected TextView text;

    @ViewById(R.id.confidence)
    protected TextView confidence;

    @ColorRes(R.color.green)
    protected int accDarkColor;

    @ColorRes(R.color.red)
    protected int accColor;

    public AnswerItem(Context context) {
        super(context);
    }

    @Override
    public void bind(YodaAnswer data, int pos) {
        text.setText(data.getText());
        if (data instanceof YodaAnswerItem) {
            YodaAnswerItem d = (YodaAnswerItem) data;
            confidence.setVisibility(VISIBLE);
            confidence.setText(String.format("%3.1f %%", d.getConfidence() * 100));
            confidence.setTextColor(ColorUtils.interpolate(accColor, accDarkColor, (float) d.getConfidence()));
        } else {
            confidence.setVisibility(INVISIBLE);
        }
    }
}
