package in.vesely.eclub.yodaqa.restclient;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by vesely on 6/16/15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class YodaSource implements Parcelable {
    @JsonProperty("origin")
    private String origin;

    @JsonProperty("URL")
    private String URL;

    @JsonProperty("isConcept")
    private boolean isConcept;

    @JsonProperty("type")
    private String type;

    @JsonProperty("title")
    private String title;

    @JsonProperty("state")
    private int state;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public boolean isConcept() {
        return isConcept;
    }

    public void setIsConcept(boolean isConcept) {
        this.isConcept = isConcept;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.origin);
        dest.writeString(this.URL);
        dest.writeByte(isConcept ? (byte) 1 : (byte) 0);
        dest.writeString(this.type);
        dest.writeString(this.title);
        dest.writeInt(this.state);
    }

    public YodaSource() {
    }

    protected YodaSource(Parcel in) {
        this.origin = in.readString();
        this.URL = in.readString();
        this.isConcept = in.readByte() != 0;
        this.type = in.readString();
        this.title = in.readString();
        this.state = in.readInt();
    }

    public static final Parcelable.Creator<YodaSource> CREATOR = new Parcelable.Creator<YodaSource>() {
        public YodaSource createFromParcel(Parcel source) {
            return new YodaSource(source);
        }

        public YodaSource[] newArray(int size) {
            return new YodaSource[size];
        }
    };
}
