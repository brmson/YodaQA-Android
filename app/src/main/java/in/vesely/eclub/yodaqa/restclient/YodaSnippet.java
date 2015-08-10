package in.vesely.eclub.yodaqa.restclient;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ERMRK on 6.8.2015.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class YodaSnippet implements Parcelable{

    @JsonProperty("passageText")
    private String passageText;

    @JsonProperty("snippetID")
    private int snippetID;

    @JsonProperty("sourceID")
    private int sourceID;

    @JsonProperty("propertyLabel")
    private String propertyLabel;

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPassageText() {
        return passageText;
    }

    public int getSnippetID() {
        return snippetID;
    }

    public int getSourceID() {
        return sourceID;
    }

    public String getPropertyLabel() {
        return propertyLabel;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.passageText);
        dest.writeInt(this.snippetID);
        dest.writeInt(this.sourceID);
        dest.writeString(this.propertyLabel);
    }

    public YodaSnippet(){};

    public YodaSnippet(String passageText, int snippetID, int sourceID, String propertyLabel){
        this.passageText=passageText;
        this.snippetID=snippetID;
        this.sourceID=sourceID;
        this.propertyLabel=propertyLabel;
    }

    protected YodaSnippet(Parcel in) {
        this.passageText = in.readString();
        this.snippetID = in.readInt();
        this.sourceID = in.readInt();
        this.propertyLabel= in.readString();
    }

    public static final Parcelable.Creator<YodaSnippet> CREATOR = new Parcelable.Creator<YodaSnippet>() {
        public YodaSnippet createFromParcel(Parcel source) {
            return new YodaSnippet(source);
        }

        public YodaSnippet[] newArray(int size) {
            return new YodaSnippet[size];
        }
    };
}
