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
import android.util.Log;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Transactional;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DrawableRes;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import in.vesely.eclub.yodaqa.R;
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
@OptionsMenu(R.menu.main)
public class MainActivity extends AppCompatActivity implements SearchBox.SearchListener, TextToSpeech.OnInitListener {

    private static final String RESPONSE_STATE = "response_state";
    private static final String TAG = "MainActivity";

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

    private final Object lock = new Object();

    private TextToSpeech t1;

    @NonConfigurationInstance
    protected YodaExecuter executer;
    private DBHelper dbHelper;
    private YodaAnswersResponse response;
    private boolean searchOpened;
    private static final int MY_DATA_CHECK_CODE = 131;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        bus.register(this);
        dbHelper = new DBHelper(this);
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        setEndpoint();
    }

    @Override
    public void onResume() {
        super.onResume();
        setEndpoint();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
            t1 = null;
        }
        bus.unregister(this);
    }

    private void setEndpoint() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String settedEndpoint = sharedPref.getString("endpoint", "");
        if (settedEndpoint.equals("Movies")) {
            restClient.setRootUrl("http://qa.ailao.eu:4000/");
        } else {
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
        search.setMenuListener(new SearchBox.MenuListener() {
            @Override
            public void onMenuClick() {
                if (!searchOpened) {
                    Log.d(TAG, "Search bar menu clicked -> opening.");
                    search.toggleSearch();
                }
            }
        });
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
            if (t1 != null) {
                t1.speak(response.getTextForSpokenAnswer(), TextToSpeech.QUEUE_FLUSH, null);
            } else {
                Log.d(TAG, "TTS is not instantiated.");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayList<String> best = new ArrayList<>();
            best.add(matches.get(0));
            search.populateEditText(best);
        } else if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                t1 = new TextToSpeech(getApplicationContext(), this);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        //TODO Hack to force the search box voice recognition language to en-US. The code of the search box should be modified in the future to remove this hack.
        forceSpeechRecognitionLanguage(intent, requestCode);
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        //TODO Hack to force the search box voice recognition language to en-US. The code of the search box should be modified in the future to remove this hack.
        forceSpeechRecognitionLanguage(intent, requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    private void forceSpeechRecognitionLanguage(Intent intent, int requestCode) {
        if (requestCode == SearchBox.VOICE_RECOGNITION_CODE) {
            Log.d(TAG, "Recognition language: " + Locale.US.toString());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());
        }
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
        searchOpened = true;
        Log.d(TAG, "Search opened.");
    }

    @Override
    public void onSearchCleared() {

    }

    @Override
    public void onSearchClosed() {
        searchOpened = false;
        Log.d(TAG, "Search closed.");
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
            executer = new YodaExecuter(bus, restClient);
            executer.execute(term);
        }
    }

    @Override
    public void onBackPressed() {
        if (searchOpened) {
            search.toggleSearch();
        } else {
            super.onBackPressed();
        }
    }

    @OptionsItem(R.id.menu_settings)
    public void onSettingsMenuItemClicked() {
        Intent intent = new Intent(this, SettingsActivity_.class);
        startActivity(intent);
    }

    @OptionsItem(R.id.menu_report_error)
    public void onReportErrorMenuItemClicked() {
        new ReportDialogFragment().show(getFragmentManager(), "dialog");
    }

    @Override
    public void onInit(int status) {
        Log.d(TAG, "Text to speech init " + status);
        if (status == TextToSpeech.SUCCESS) {
            int res;
            if (t1.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                res = t1.setLanguage(Locale.US);
            } else {
                res = t1.setLanguage(Locale.UK);
            }
            if (res == TextToSpeech.LANG_NOT_SUPPORTED || res == TextToSpeech.LANG_MISSING_DATA) {
                Log.d(TAG, "Language not supported");
            }
        } else {
            Log.d(TAG, "Text to speech init failed. " + status);
        }
    }
}
