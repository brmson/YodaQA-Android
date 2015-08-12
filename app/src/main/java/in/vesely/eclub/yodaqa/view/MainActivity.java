package in.vesely.eclub.yodaqa.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.Transactional;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DrawableRes;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.vesely.eclub.yodaqa.R;
import in.vesely.eclub.yodaqa.adapters.ExpandableListAdapter;
import in.vesely.eclub.yodaqa.adapters.ResponseFragmentTabAdapter;
import in.vesely.eclub.yodaqa.bus.OttoBus;
import in.vesely.eclub.yodaqa.bus.RequestUpdateAction;
import in.vesely.eclub.yodaqa.bus.ResponseChangedAction;
import in.vesely.eclub.yodaqa.db.DBHelper;
import in.vesely.eclub.yodaqa.db.SearchItem;
import in.vesely.eclub.yodaqa.restclient.YodaAnswersResponse;
import in.vesely.eclub.yodaqa.restclient.YodaErrorHandler;
import in.vesely.eclub.yodaqa.restclient.YodaExecuter;
import in.vesely.eclub.yodaqa.restclient.YodaRestClient;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements SearchBox.SearchListener {

    private static final String RESPONSE_STATE = "response_state";

    @ViewById(R.id.tabLayout)
    protected TabLayout tabLayout;

    @ViewById(R.id.pager)
    protected ViewPager pager;

    @ViewById(R.id.searchbox)
    protected SearchBox search;

    @DrawableRes(R.drawable.ic_action_history)
    protected Drawable historyDrawable;

    @RestService
    protected YodaRestClient restClient;

    @Bean
    protected YodaErrorHandler errorHandler;

    @Bean
    protected OttoBus bus;

    private Map<String, String> ids = new HashMap<>();

    private final Object lock = new Object();

    TextToSpeech t1;

    private int group1Id = 1;

    private int settingsId = Menu.FIRST;

    @NonConfigurationInstance
    protected YodaExecuter executer;
    private DBHelper dbHelper;
    private YodaAnswersResponse response;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        bus.register(this);
        dbHelper = new DBHelper(this);
        initTextToSpeech();
        setEndpoint();
    }

    @Override
    public void onResume() {
        super.onResume();
        initTextToSpeech();
        setEndpoint();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    public void onPause() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    private void initTextToSpeech() {
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
    }

    private void setEndpoint(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String settedEndpoint = sharedPref.getString("endpoint", "");
        if (settedEndpoint.equals("Movies")){
            restClient.setRootUrl("http://qa.ailao.eu:4000/");
        }else{
            restClient.setRootUrl("http://qa.ailao.eu/");
        }
    }

    @AfterViews
    protected void afterViews() {
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
        ResponseFragmentTabAdapter adapter = new ResponseFragmentTabAdapter(
                getSupportFragmentManager(),
                getFragments(),
                new String[]{getString(R.string.answer_title), getString(R.string.sources_title)});
        pager.setAdapter(adapter);
        tabLayout.setTabsFromPagerAdapter(adapter);
        search.enableVoiceRecognition(this);
        search.setSearchListener(this);
        search.setLogoText(getString(R.string.enter_something));
        loadResults();
    }

    @Background
    protected void loadResults() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<SearchResult> results = new ArrayList<>();
        for (String s : SearchItem.select(db)) {
            results.add(new SearchResult(s, historyDrawable));
        }
        search.setInitialResults(results);
        db.close();
    }

    private List<Class<? extends ResponseFragment>> getFragments() {
        List<Class<? extends ResponseFragment>> fragments = new LinkedList<>();
        fragments.add(AnswersFragment_.class);
        fragments.add(SourcesFragment_.class);
        return fragments;
    }

    @AfterInject
    protected void afterInject() {
        restClient.getRestTemplate().setRequestFactory(new OkHttpClientHttpRequestFactory());
        restClient.setRestErrorHandler(errorHandler);
    }

    @Subscribe
    public void onRequestRefresh(RequestUpdateAction action) {
    }

    @Subscribe
    public void onResponseChanged(ResponseChangedAction action) {
        response = action.getResponse();
        if (response != null && response.isFinished()) {
            t1.speak(response.getAnswers().get(0).getText(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayList<String> best = new ArrayList<>();
            best.add(matches.get(0));
            search.populateEditText(best);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        //TODO Hack to force the search box voice recognition language to en-US. The code of the search box should be modified in the future to remove this hack.
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
//        intent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{});
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (response != null) {
            outState.putParcelable(RESPONSE_STATE, response);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(RESPONSE_STATE)) {
            response = savedInstanceState.getParcelable(RESPONSE_STATE);
            bus.post(new ResponseChangedAction(response));
        }
    }

    @Override
    public void onSearchOpened() {

    }

    @Override
    public void onSearchCleared() {

    }

    @Override
    public void onSearchClosed() {

    }

    @Override
    public void onSearchTermChanged() {

    }

    @Override
    public void onSearch(String s) {
        ArrayList<SearchResult> searchables = search.getSearchables();
        List<SearchResult> toRemove = new LinkedList<>();
        for (SearchResult result : searchables) {
            if (result.title.equals(s)) {
                toRemove.add(result);
            }
        }
        searchables.removeAll(toRemove);
        searchables.add(0, new SearchResult(s, historyDrawable));
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        addResult(db, s);
        db.close();
        bus.post(new RequestUpdateAction());
        initiateSearch(s);
    }

    @Transactional
    protected void addResult(SQLiteDatabase db, String s) {
        SearchItem.insert(s, db);
    }

    private void initiateSearch(String term) {
        synchronized (lock) {
            if (executer != null) {
                executer.cancel(true);
            }
            response = null;
            executer = new YodaExecuter(bus, restClient, ids);
            executer.execute(term);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(group1Id, settingsId, settingsId, R.string.settings);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case 1:
                Intent intent=new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
