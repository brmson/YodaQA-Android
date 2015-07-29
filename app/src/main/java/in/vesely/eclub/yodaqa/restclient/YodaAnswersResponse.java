package in.vesely.eclub.yodaqa.restclient;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.vesely.eclub.yodaqa.view.MainActivity_;

/**
 * Created by vesely on 6/15/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class YodaAnswersResponse implements Parcelable {

    @JsonProperty("answers")
    private List<YodaAnswer> answers;

    @JsonProperty("sources")
    private HashMap<String,YodaSource> sources;

    @JsonProperty("finished")
    private boolean finished;

    @JsonProperty("gen_sources")
    private int generatedSources;

    @JsonProperty("gen_answers")
    private int generatedAnswers;

    public YodaAnswersResponse() {
    }

    public List<YodaAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<YodaAnswer> answers) {
        this.answers = answers;
    }

    public HashMap<String,YodaSource> getSources() {
        return sources;
    }

    public void setSources(HashMap<String,YodaSource> sources) {
        this.sources = sources;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getGeneratedSources() {
        return generatedSources;
    }

    public void setGeneratedSources(int generatedSources) {
        this.generatedSources = generatedSources;
    }

    public int getGeneratedAnswers() {
        return generatedAnswers;
    }

    public void setGeneratedAnswers(int generatedAnswers) {
        this.generatedAnswers = generatedAnswers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(answers);

        dest.writeInt(sources.size());
        for(Map.Entry<String,YodaSource> entry : sources.entrySet()){
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }

        dest.writeByte(finished ? (byte) 1 : (byte) 0);
        dest.writeInt(this.generatedSources);
        dest.writeInt(this.generatedAnswers);
    }

    protected YodaAnswersResponse(Parcel in) {
        this.answers = in.createTypedArrayList(YodaAnswer.CREATOR);
        //this.sources = in.(YodaSource.CREATOR);

        int size = in.readInt();
        for(int i = 0; i < size; i++){
            String key = in.readString();
            YodaSource value = in.readParcelable(null);
            sources.put(key,value);
        }

        this.finished = in.readByte() != 0;
        this.generatedSources = in.readInt();
        this.generatedAnswers = in.readInt();
    }

    public static final Creator<YodaAnswersResponse> CREATOR = new Creator<YodaAnswersResponse>() {
        public YodaAnswersResponse createFromParcel(Parcel source) {
            return new YodaAnswersResponse(source);
        }

        public YodaAnswersResponse[] newArray(int size) {
            return new YodaAnswersResponse[size];
        }
    };
}
