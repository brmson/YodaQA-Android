package in.vesely.eclub.yodaqa.restclient;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by vesely on 6/15/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class YodaAnswerItem extends YodaAnswer {

    @JsonProperty("confidence")
    private double confidence;

    @JsonProperty("snippetIDs")
    private int[] snippetIDs;

    private List<SnippetSourceContainer> snippets;

    public double getConfidence() {
        return confidence;
    }

    public int[] getSnippetIDs() {
        return snippetIDs;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(this.confidence);
        dest.writeIntArray(this.snippetIDs);
    }

    public YodaAnswerItem() {
        snippets = new LinkedList<>();
    }

    public YodaAnswerItem(String text, double confidence) {
        super(text);
        snippets = new LinkedList<>();
        this.confidence = confidence;
    }

    protected YodaAnswerItem(Parcel in) {
        super(in);
        snippets = new LinkedList<>();
        this.confidence = in.readDouble();
        this.snippetIDs = in.createIntArray();
    }

    public static final Parcelable.Creator<YodaAnswerItem> CREATOR = new Parcelable.Creator<YodaAnswerItem>() {
        public YodaAnswerItem createFromParcel(Parcel source) {
            return new YodaAnswerItem(source);
        }

        public YodaAnswerItem[] newArray(int size) {
            return new YodaAnswerItem[size];
        }
    };

    public void addSnippet(SnippetSourceContainer snippet) {
        snippets.add(snippet);
    }

    public List<SnippetSourceContainer> getSnippets() {
        return snippets;
    }

    @Override
    public List<SnippetSourceContainer> getChildItemList() {
        return getSnippets();
    }
}
