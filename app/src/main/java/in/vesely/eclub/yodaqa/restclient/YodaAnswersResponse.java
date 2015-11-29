package in.vesely.eclub.yodaqa.restclient;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by vesely on 6/15/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class YodaAnswersResponse implements Parcelable {

    @JsonProperty("answers")
    private List<YodaAnswerItem> answers;

    @JsonProperty("sources")
    private HashMap<String, YodaSource> sources;

    @JsonProperty("snippets")
    private HashMap<String, YodaSnippet> snippets;

    @JsonProperty("finished")
    private boolean finished;

    @JsonProperty("gen_sources")
    private int generatedSources;

    @JsonProperty("gen_answers")
    private int generatedAnswers;

    @JsonProperty("answerSentence")
    private String answerSentence;

    private List<YodaAnswer> answersAll;

    public YodaAnswersResponse() {
    }

    public List<YodaAnswerItem> getAnswers() {
        return answers;
    }

    public void setAnswers(List<YodaAnswerItem> answers) {
        this.answers = answers;
    }

    public HashMap<String, YodaSource> getSources() {
        return sources;
    }

    public void setSources(HashMap<String, YodaSource> sources) {
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

    public HashMap<String, YodaSnippet> getSnippets() {
        return snippets;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(answers);

        dest.writeInt(sources.size());
        for (Map.Entry<String, YodaSource> entry : sources.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }

        dest.writeInt(snippets.size());
        for (Map.Entry<String, YodaSnippet> entry : snippets.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }

        dest.writeByte(finished ? (byte) 1 : (byte) 0);
        dest.writeInt(this.generatedSources);
        dest.writeInt(this.generatedAnswers);
        dest.writeByte(answerSentence == null ? (byte) 1 : (byte) 0);
        if (answerSentence != null) {
            dest.writeString(answerSentence);
        }
    }

    protected YodaAnswersResponse(Parcel in) {
        this.answers = in.createTypedArrayList(YodaAnswerItem.CREATOR);
        //this.sources = in.(YodaSource.CREATOR);

        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            YodaSource value = in.readParcelable(null);
            sources.put(key, value);
        }

        size = in.readInt();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            YodaSnippet value = in.readParcelable(null);
            snippets.put(key, value);
        }

        this.finished = in.readByte() != 0;
        this.generatedSources = in.readInt();
        this.generatedAnswers = in.readInt();
        boolean isAbsent = in.readByte() != 0;
        if (!isAbsent) {
            this.answerSentence = in.readString();
        }
    }

    public String getTextForSpokenAnswer() {
        if (answerSentence != null) {
            return answerSentence;
        }
        if (answers.size() > 0) {
            return answers.get(0).getText();
        }
        return null;
    }

    public List<YodaAnswer> getAllAnswers() {
        if (answersAll == null) {
            answersAll = new LinkedList<>();
            String answerSentence = getAnswerSentence();
            if (answerSentence != null) {
                answersAll.add(new YodaAnswer(answerSentence));
            }
            for (YodaAnswerItem a : getAnswers()) {
                int snippetIDs[] = a.getSnippetIDs();
                for (int snippetId : snippetIDs) {
                    YodaSnippet snippet = (snippets.get(String.valueOf(snippetId)));
                    int sourceID = snippet.getSourceID();
                    YodaSource source = sources.get(String.valueOf(sourceID));
                    a.addSnippet(new SnippetSourceContainer(snippet, source));
                }
                answersAll.add(a);
            }
        }
        return answersAll;
    }

    public String getAnswerSentence() {
        return answerSentence;
    }

    public void setAnswerSentence(String answerSentence) {
        this.answerSentence = answerSentence;
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
