package android.nomadproject.com.nomad.mapfragment;

import android.app.Dialog;
import android.nomadproject.com.nomad.R;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by David Levayer on 21/03/15.
 */
public class MapsDialogFragment extends DialogFragment {

    public final static String TITLE = "mapsdialogfragmenttitle";
    public final static String INFO = "mapsdialogfragmentinfo";

    private TextView title, content;
    private final static float dialogSize = 0.8f;

    public MapsDialogFragment(){ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map_dialogfragment, container, false);

        this.title = (TextView) view.findViewById(R.id.title);
        this.content = (TextView) view.findViewById(R.id.information);

        Bundle b = getArguments();
        this.title.setText(b.getString(TITLE));
        this.content.setText(b.getString(INFO));

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int dialogHeight = (int)(displaymetrics.heightPixels * dialogSize);
        int dialogWidth = (int)(displaymetrics.widthPixels * dialogSize);

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }
}
