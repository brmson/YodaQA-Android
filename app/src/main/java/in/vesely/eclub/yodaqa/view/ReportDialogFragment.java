package in.vesely.eclub.yodaqa.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.acra.ACRA;

import in.vesely.eclub.yodaqa.R;

/**
 * Created by vesely on 11/29/15.
 */
public class ReportDialogFragment extends DialogFragment implements MaterialDialog.InputCallback {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity())
                .title(R.string.report_error)
                .input(R.string.describe_problem, R.string.describe_problem_prefill, true, this)
                .cancelable(true)
                .negativeText(R.string.cancel)
                .positiveText(R.string.send)
                .build();
    }

    @Override
    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
        ACRA.getErrorReporter().reportBuilder().customData("Problem", String.valueOf(input)).forceSilent().send();
        Toast.makeText(getActivity(), R.string.thank_you_feedback, Toast.LENGTH_LONG).show();
    }
}
