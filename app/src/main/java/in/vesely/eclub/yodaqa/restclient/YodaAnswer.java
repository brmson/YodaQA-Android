package in.vesely.eclub.yodaqa.restclient;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by vesely on 11/27/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class YodaAnswer implements Parcelable {

    @JsonProperty("text")
    protected String text;

    public YodaAnswer(String text) {
        this.text = text;
    }

    public YodaAnswer() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public static final Parcelable.Creator<YodaAnswer> CREATOR = new Parcelable.Creator<YodaAnswer>() {
        public YodaAnswer createFromParcel(Parcel source) {
            return new YodaAnswer(source);
        }

        public YodaAnswer[] newArray(int size) {
            return new YodaAnswer[size];
        }
    };

    public YodaAnswer(Parcel source) {
        text = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }
}
