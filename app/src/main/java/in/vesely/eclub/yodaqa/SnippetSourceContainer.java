package in.vesely.eclub.yodaqa;

import in.vesely.eclub.yodaqa.restclient.YodaSnippet;
import in.vesely.eclub.yodaqa.restclient.YodaSource;

/**
 * Created by ERMRK on 7.8.2015.
 */
public class SnippetSourceContainer {
    private YodaSnippet yodaSnippet;
    private YodaSource yodaSource;

    public SnippetSourceContainer(YodaSnippet yodaSnippet, YodaSource yodaSource) {
        this.yodaSnippet = yodaSnippet;
        this.yodaSource = yodaSource;
    }

    public YodaSnippet getYodaSnippet() {
        return yodaSnippet;
    }

    public YodaSource getYodaSource() {
        return yodaSource;
    }
}
