package android.nomadproject.com.nomad.parametres;

import android.content.Intent;
import android.nomadproject.com.nomad.MainDrawerActivity;
import android.nomadproject.com.nomad.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;
import android.widget.CompoundButton;

/*
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;*/

import com.facebook.*;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by corentin on 21/03/2015.
 */
public class ParametreFragment extends Fragment {

    /*private CallbackManager callbackManager ;//= MainDrawerActivity.callbackManager;

    private AccessToken tk = null;*/

    private UiLifecycleHelper uiHelper;


    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                Log.d("MainActivity", "Facebook session opened.");
            } else if (state.isClosed()) {
                Log.d("MainActivity", "Facebook session closed.");

            }
        }
    };


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

        uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);

        uiHelper.onCreate(savedInstanceState);


        LoginButton authButton = (LoginButton) v.findViewById(R.id.login_button);
        authButton.setReadPermissions(Arrays.asList("user_likes", "user_status", "user_events","rsvp_event"));
        authButton.setFragment(this);
        authButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback()
        {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    Log.d("FACEBOOK" , user.getName());
                } else {
                    Log.d("FACEBOOK" ,"PAS USER");
                }
            }
        });



        /*FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) v.findViewById(R.id.login_button);
        //loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions(Arrays.asList("user_likes", "user_status", "user_events","user_friends"));
        // If using in a fragment
        loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                Log.d("FACEBOOK","SUCCESS");

                tk = loginResult.getAccessToken();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,GraphResponse response) {
                                // Application code
                                JSONObject  jso = response.getJSONObject();
                                Log.d("FACEBOOK",jso.toString());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("FACEBOOK","CANCELL");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("FACEBOOK","ERRORRRRR");
            }
        });

*/
        Button brf = (Button) v.findViewById(R.id.buttonRefreshLocationFacebook);
        brf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                /*Log.d("FACEBOOK","FACEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");

                if(AccessToken.getCurrentAccessToken() != null){
                    //LoginManager.getInstance().logInWithReadPermissions(me(),Arrays.asList("user_likes", "user_status", "user_events","user_friends"));
                    Log.d("FACEBOOK","TOKENNNNNNNNNNN");
                    GraphRequest request = GraphRequest.newMeRequest(
                            AccessToken.getCurrentAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object,GraphResponse response) {
                                    // Application code
                                    JSONObject  jso = response.getJSONObject();
                                    Log.d("FACEBOOK",jso.toString());
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,link,events{description,id,name}");
                    request.setParameters(parameters);
                    request.executeAsync();
                }*/

                getEventList();

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

   /*@Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data);

    }



    @Override

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);

        uiHelper.onSaveInstanceState(savedState);

    }

    public void getEventList(){
        final Session session = Session.getActiveSession();

        Log.i("TAG", "Dans fonction FILL"+session);

        if(session == null){
            Session.openActiveSession(getActivity(), true, statusCallback);
        }

        if(session != null && session.isOpened()){
            String fqlQuery = "SELECT name from event where eid IN (SELECT eid from event_member where uid = me())";
            Bundle params = new Bundle();
            params.putString("q", fqlQuery);
            Request request = new Request(session,
                    "/fql",
                    params,
                    HttpMethod.GET,
                    new Request.Callback(){
                        public void onCompleted(Response response) {

                            Log.i("wimp", "RES: "+response);

                            try
                            {
                                GraphObject go  = response.getGraphObject();
                                JSONObject jso = go.getInnerJSONObject();
                                JSONArray arr = jso.getJSONArray( "data" );

                                for ( int i = 0; i < ( arr.length() ); i++ )
                                {
                                    JSONObject json_obj = arr.getJSONObject( i );
                                    String name = json_obj.getString("name");
                                    Double longitude = null;
                                    Double latitude = null;
                                    String location = json_obj.getString("location");

                                    try{
                                        longitude = json_obj.getJSONObject("venue").getDouble("longitude");
                                        latitude = json_obj.getJSONObject("venue").getDouble("latitude");
                                    }catch(JSONException e){}

                                    if( (longitude == null) && (latitude == null)){
                                        if(location != null){
                                            Log.i("tag", "Nom premier = "+name);
//		                            		Event evt = new Event(name, location, null, null);
                                            //MainActivity.listeEvent.put(name, new Event(name, location, latitude, longitude,json_obj.getInt("eid")));
                                            //locateEventList(evt);
                                        }
                                    }else{
                                        /*MainActivity.listeEvent.put(name, new Event(name, location, latitude, longitude,json_obj.getInt("eid")));
                                        map.addMarker(new MarkerOptions()
                                                .title(name)
                                                .snippet("The most populous city in Australia.")
                                                .position(new LatLng(latitude, longitude)));*/
                                        Log.d("FACEBOOK","EVENT:"+name+location+latitude+longitude);
                                    }
                                }
                            }
                            catch ( Throwable t ){t.printStackTrace();}
                        }
                    });
            Request.executeBatchAsync(request);

        }else{
            Log.i("TAG", "SESSION NUL ou PAS COM"+ session);
        }
    }

}
