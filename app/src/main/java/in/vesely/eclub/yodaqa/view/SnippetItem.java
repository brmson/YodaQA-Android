package in.vesely.eclub.yodaqa.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import in.vesely.eclub.yodaqa.R;
import in.vesely.eclub.yodaqa.adapters.BindableLinearLayout;
import in.vesely.eclub.yodaqa.restclient.SnippetSourceContainer;

/**
 * Created by vesely on 11/29/15.
 */
@EViewGroup(R.layout.snippet_item)
public class SnippetItem extends BindableLinearLayout<SnippetSourceContainer> {

    @ViewById(R.id.snippetSourceButton)
    protected ImageButton button;

    @ViewById(R.id.snippetName)
    protected TextView name;

    @ViewById(R.id.snippetText)
    protected TextView text;

    public SnippetItem(Context context) {
        super(context);
    }

    @Override
    public void bind(SnippetSourceContainer data, int pos) {
        text.setText(data.getYodaSnippet().getPassageText());
        if (data.getYodaSnippet().getPropertyLabel() != null) {
            text.append(data.getYodaSnippet().getPropertyLabel());
        }
        name.setText(Html.fromHtml("<b>" + data.getYodaSource().getTitle() + "</b>"));
        name.append(" (" + data.getYodaSource().getOrigin() + ")");
        setImageButtonImage(data.getYodaSource().getType());
        setOnClickButtonImage(data.getYodaSource().getURL());
    }

    private void setImageButtonImage(String type) {
        switch (type) {
            case "enwiki":
                button.setImageResource(R.drawable.ic_wikipedia_logo);
                break;
            case "freebase":
                button.setImageResource(R.drawable.ic_freebase_logo);
                break;
            case "dbpedia":
                button.setImageResource(R.drawable.ic_dbpedia_logo);
                break;
            case "bing":
                button.setImageResource(R.drawable.ic_bing_logo);
                break;
        }
    }

    private void setOnClickButtonImage(final String url) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl(url);
            }
        });
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        getContext().startActivity(launchBrowser);
    }
}
