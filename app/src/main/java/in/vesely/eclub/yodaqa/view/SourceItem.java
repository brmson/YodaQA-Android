package in.vesely.eclub.yodaqa.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DrawableRes;

import in.vesely.eclub.yodaqa.R;
import in.vesely.eclub.yodaqa.adapters.BindableLinearLayout;
import in.vesely.eclub.yodaqa.restclient.YodaSource;

/**
 * Created by vesely on 6/16/15.
 */
@EViewGroup(R.layout.source_item)
public class SourceItem extends BindableLinearLayout<YodaSource> {

    @ViewById(R.id.text)
    protected TextView text;

    @DrawableRes(R.drawable.ic_wikipedia_w_logo)
    protected Drawable wikiLogoDrawable;

    public SourceItem(Context context) {
        super(context);
    }

    @Override
    public void bind(YodaSource data, int position) {
        String url = String.format("http://en.wikipedia.org/?curid=%s", data.getPageId());
        text.setText(Html.fromHtml(String.format("<a href=\"%s\">%s (%s)</a>", url, data.getTitle(), data.getOrigin())));
        text.setMovementMethod(LinkMovementMethod.getInstance());
        text.setCompoundDrawablesWithIntrinsicBounds(wikiLogoDrawable, null, null, null);
    }
}
