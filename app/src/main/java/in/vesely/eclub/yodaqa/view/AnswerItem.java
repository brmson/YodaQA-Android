package in.vesely.eclub.yodaqa.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import in.vesely.eclub.yodaqa.R;
import in.vesely.eclub.yodaqa.adapters.expandable_recyclerview.ParentBindableLinearLayout;
import in.vesely.eclub.yodaqa.restclient.YodaAnswer;
import in.vesely.eclub.yodaqa.restclient.YodaAnswerItem;
import in.vesely.eclub.yodaqa.utils.ColorUtils;

/**
 * Created by vesely on 6/16/15.
 */
@EViewGroup(R.layout.answer_item)
public class AnswerItem extends ParentBindableLinearLayout<YodaAnswer> {

    @ViewById(R.id.text)
    protected TextView text;

    @ViewById(R.id.confidence)
    protected TextView confidence;

    @ViewById(R.id.progressBar)
    protected ProgressBar progressBar;

    @ViewById(R.id.indicator)
    protected ImageView indicator;

    @ColorRes(R.color.green)
    protected int accDarkColor;

    @ColorRes(R.color.red)
    protected int accColor;

    private boolean animate;
    private ValueAnimator animator;

    public AnswerItem(Context context) {
        super(context);
        animate = true;
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        indicator.animate().rotation(expanded ? 0 : 180).start();
    }

    @Override
    public void bind(YodaAnswer data, int position, boolean isExpanded) {
        text.setText(data.getText());
        indicator.setVisibility(data.getChildItemList().size() == 0 ? View.INVISIBLE : View.VISIBLE);
        indicator.setRotation(isExpanded ? 180 : 0);
        boolean isAnswerSentence = !(data instanceof YodaAnswerItem);
        int visibility = isAnswerSentence ? GONE : VISIBLE;
        text.setMaxLines(isAnswerSentence ? 5 : 1);
        if (!isAnswerSentence) {
            YodaAnswerItem d = (YodaAnswerItem) data;
            if (animator != null) {
                animator.cancel();
            }
            if (animate) {
                animate((float) d.getConfidence());
                animate = false;
            } else {
                setWithoutAnimation((float) d.getConfidence());
            }
        }
        progressBar.setVisibility(visibility);
        confidence.setVisibility(visibility);
    }

    private void setWithoutAnimation(float confVal) {
        int color = confVal == 0 ? accColor : ColorUtils.interpolate(accColor, accDarkColor, confVal);
        confidence.setText(String.format("%3.1f %%", confVal * 100));
        confidence.setTextColor(color);
        progressBar.setProgress((int) (confVal * 100));
        progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    private void animate(final float confVal) {
        setWithoutAnimation(0);
        animator = ValueAnimator.ofFloat(confVal * 100);
        animator.setDuration(700);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (Float) valueAnimator.getAnimatedValue();
                int color = ColorUtils.interpolate(accColor, accDarkColor, animatedValue / 100f);
                String stringVal = String.format("%3.1f %%", animatedValue);
                if (!stringVal.equals(confidence.getText())) {
                    confidence.setTextColor(color);
                    confidence.setText(stringVal);
                }
                if ((int) animatedValue != progressBar.getProgress()) {
                    progressBar.setProgress((int) animatedValue);
                    progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                }
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}
