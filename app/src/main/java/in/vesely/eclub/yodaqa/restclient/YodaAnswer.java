package in.vesely.eclub.yodaqa.restclient;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by vesely on 6/15/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class YodaAnswer implements Parcelable {

    @JsonProperty("text")
    private String text;

    @JsonProperty("confidence")
    private double confidence;

    @JsonProperty("snippetIDs")
    private int[] snippetIDs;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

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
        dest.writeString(this.text);
        dest.writeDouble(this.confidence);
        dest.writeIntArray(this.snippetIDs);
    }

    public YodaAnswer() {
    }

    public YodaAnswer(String text, double confidence) {
        this.text = text;
        this.confidence = confidence;
    }

    protected YodaAnswer(Parcel in) {
        this.text = in.readString();
        this.confidence = in.readDouble();
        this.snippetIDs=in.createIntArray();
    }

    public static final Parcelable.Creator<YodaAnswer> CREATOR = new Parcelable.Creator<YodaAnswer>() {
        public YodaAnswer createFromParcel(Parcel source) {
            return new YodaAnswer(source);
        }

        public YodaAnswer[] newArray(int size) {
            return new YodaAnswer[size];
        }
    };
}
