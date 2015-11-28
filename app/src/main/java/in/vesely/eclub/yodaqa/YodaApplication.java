package in.vesely.eclub.yodaqa;

import android.app.Application;

import com.github.anrwatchdog.ANRError;
import com.github.anrwatchdog.ANRWatchDog;

import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

/**
 * Created by vesely on 11/27/15.
 */
@ReportsCrashes(
        formUri = "https://vesely.cloudant.com/acra-yodaqa/_design/acra-storage/_update/report",
        reportType = HttpSender.Type.JSON,
        httpMethod = HttpSender.Method.POST,
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE,
                ReportField.USER_COMMENT,
                ReportField.LOGCAT
        },
        mode = ReportingInteractionMode.DIALOG,
        logcatFilterByPid = true,
        logcatArguments = {"-t", "400", "-v", "time"},
        resDialogTitle = R.string.crash_dialog_title,
        resDialogText = R.string.crash_dialog_text,
        resDialogIcon = 0,
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. when defined, adds a user text field input with this text resource as a label
        resDialogOkToast = R.string.crash_dialog_ok_toast // optional. displays a Toast message when the user accepts to send a report.
)
public class YodaApplication extends Application {

    @Override
    public void onCreate() {
        ACRA.init(this);
        ACRAConfiguration config = ACRA.getConfig();
        config.setFormUriBasicAuthLogin(getString(R.string.acra_login));
        config.setFormUriBasicAuthPassword(getString(R.string.acra_password));
        if (!BuildConfig.DEBUG) {
            new ANRWatchDog().setANRListener(new ANRWatchDog.ANRListener() {
                @Override
                public void onAppNotResponding(ANRError error) {
                    ACRA.getErrorReporter().handleException(in.vesely.eclub.yodaqa.ANRError.New("app", false));
                }
            }).start();
        }
        super.onCreate();

    }
}
