package android.nomadproject.com.nomad.parametres;

//import android.app.Fragment;

import android.content.Intent;
import android.nomadproject.com.nomad.MainDrawerActivity;
import android.nomadproject.com.nomad.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import android.widget.CompoundButton;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


/**
 * Created by corentin on 21/03/2015.
 */
public class ParametreFragment extends Fragment {

    private CallbackManager callbackManager = MainDrawerActivity.callbackManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_parametres, container, false);

        ToggleButton toggle = (ToggleButton) v.findViewById(R.id.togglebutton);

        toggle.setChecked(true);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("SERVICE","TOGGLE SERVICE");
                if (isChecked) {
                    // The toggle is enabledIntent
                    processStartService(MainDrawerActivity.TAG);
                } else {
                    // The toggle is disabled
                    processStopService(MainDrawerActivity.TAG);
                }
            }
        });

        LoginButton loginButton = (LoginButton) v.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        return v;
    }


    private void processStartService(final String tag) {

        Intent intent = new Intent(getActivity().getApplicationContext(), ServicesData.class);
        intent.addCategory(tag);
        getActivity().startService(intent);
    }

    private void processStopService(final String tag) {
        Intent intent = new Intent(getActivity().getApplicationContext(), ServicesData.class);
        intent.addCategory(tag);
        getActivity().stopService(intent);
    }

}
