package android.nomadproject.com.nomad;

import android.app.ActionBar;
import android.content.Intent;
import android.nomadproject.com.nomad.mapfragment.MapsFragment;
import android.nomadproject.com.nomad.parametres.ParametreFragment;
import android.nomadproject.com.nomad.parametres.ServicesData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

public class MainDrawerActivity extends FragmentActivity
        implements AdapterView.OnItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mMenuTitles;

    public static CallbackManager callbackManager;

    public static final String TAG = "MyServiceTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        final ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        mMenuTitles = getResources().getStringArray(R.array.menu_titles_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(
                new ArrayAdapter<String>(this,R.layout.activity_drawer_list_item,mMenuTitles));

        mDrawerList.setOnItemClickListener(this);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.opendrawer,
                R.string.closedrawer);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        Intent intent = new Intent(getApplicationContext(), ServicesData.class);
        intent.addCategory(TAG);
        startService(intent);


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }

    private void selectItem(int position) {

        Fragment fragment;

        switch(position){
            case 0:
                fragment = new MapsFragment();
                break;
            case 3:
                fragment = new ParametreFragment();
                break;
            default:
                fragment = new MapsFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        mDrawerList.setItemChecked(position,true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    public CallbackManager getCallbackManager(){
        return this.callbackManager;
    }
}
