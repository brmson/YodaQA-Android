package in.vesely.eclub.yodaqa.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.vesely.eclub.yodaqa.R;
import in.vesely.eclub.yodaqa.restclient.YodaAnswer;
import in.vesely.eclub.yodaqa.restclient.YodaSnippet;
import in.vesely.eclub.yodaqa.utils.ColorUtils;

/**
 * Created by ERMRK on 5.8.2015.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<YodaAnswer, List<String>> expendableListContent;
    private List<YodaAnswer> groups;

    public ExpandableListAdapter(Activity context, List<YodaAnswer> groups,
                                 Map<YodaAnswer, List<String>> expendableListContent) {
        if (groups == null) {
            groups = new ArrayList<>();
        }
        if (expendableListContent == null) {
            expendableListContent = new HashMap<>();
        }
        this.context = context;
        this.expendableListContent = expendableListContent;
        this.groups = groups;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return expendableListContent.get(groups.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String child = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.snippet_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.snippetText);

        if (child != null) {
            item.setText(Html.fromHtml(child));
        } else {
            item.setText("");
        }
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return expendableListContent.get(groups.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        YodaAnswer yodaAnswer = (YodaAnswer) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.answer_item,
                    null);
        }
        TextView text = (TextView) convertView.findViewById(R.id.text);
        text.setTypeface(null, Typeface.BOLD);
        text.setText(yodaAnswer.getText());

        TextView confidence = (TextView) convertView.findViewById(R.id.confidence);
        confidence.setText(String.format("%3.1f %%", yodaAnswer.getConfidence() * 100));
        int color = ColorUtils.interpolate(context.getResources().getColor(R.color.red),
                context.getResources().getColor(R.color.green), (float) yodaAnswer.getConfidence());
        confidence.setTextColor(color);

        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        progressBar.setProgress((int) (yodaAnswer.getConfidence() * 100));
        progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void addAll(List<YodaAnswer> answers, HashMap<String, YodaSnippet> snippets) {
        for (YodaAnswer yodaAnswer : answers) {
            groups.add(yodaAnswer);
            ArrayList snippetsToShow = createSnippets(yodaAnswer, snippets);
            expendableListContent.put(yodaAnswer, snippetsToShow);
        }
        this.notifyDataSetChanged();
        this.notifyDataSetInvalidated();
    }

    private ArrayList createSnippets(YodaAnswer answer, HashMap<String, YodaSnippet> snippets) {
        ArrayList snippetTexts = new ArrayList();
        int snippetIDs[] = answer.getSnippetIDs();
        for (int snippetId : snippetIDs
                ) {
            String passageText = (snippets.get(String.valueOf(snippetId))).getPassageText();

            if (passageText != null) {
                passageText = passageText.replaceAll(answer.getText(), "<font color='green'>" + answer.getText() + "</font>");
            }
            snippetTexts.add(passageText);
        }
        return snippetTexts;
    }

    public void clear() {
        groups.clear();
        expendableListContent.clear();
        this.notifyDataSetChanged();
        this.notifyDataSetInvalidated();
    }
}