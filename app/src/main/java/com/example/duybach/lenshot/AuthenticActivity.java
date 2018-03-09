package com.example.duybach.lenshot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duybach.lenshot.AuthenticUtils.AuthFrag;
import com.example.duybach.lenshot.AuthenticUtils.AuthPagerAdapter;

public class AuthenticActivity extends AppCompatActivity {

    public static final String LOGGED_IN_USER = "LOGGED_IN_USER";
    private static final String SELECTED_ITEM_POSITION = "SELECTED_ITEM_POSITION";
    ViewPager pager;
    FloatingActionButton fabAuth;
    RadioGroup radioGroup;
    RadioButton rbSignIn, rbSignUp;
    private ImageView logo;
    private boolean isInternetConnected;
    Snackbar snackbar;

    public BroadcastReceiver networkBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            processInternetChange(context);
        }
    };

    private void processInternetChange(Context context) {
        final int DEFAULT_MARGIN = (int) getResources().getDimension(R.dimen.fab_margin);
        isInternetConnected = getConnectivityStatus(context);

        if (!isInternetConnected) {
            // Show snack bar
            ConstraintLayout.LayoutParams params =
                    (ConstraintLayout.LayoutParams) fabAuth.getLayoutParams();
            params.setMargins(0, 0, DEFAULT_MARGIN,
                    DEFAULT_MARGIN + snackbar.getView().getHeight());
            fabAuth.setLayoutParams(params);
            snackbar.show();
        } else {
            ConstraintLayout.LayoutParams params =
                    (ConstraintLayout.LayoutParams) fabAuth.getLayoutParams();
            params.setMargins(0, 0, DEFAULT_MARGIN, DEFAULT_MARGIN);
            fabAuth.setLayoutParams(params);
            snackbar.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentic);

        startIntroPage();
        initUI();
    }

    private void initUI() {
        // Find views by Id
        pager = (ViewPager) findViewById(R.id.container_switcher);
        fabAuth = (FloatingActionButton) findViewById(R.id.fab_auth);
        radioGroup = (RadioGroup) findViewById(R.id.rg_auth_switcher);
        rbSignIn = (RadioButton) findViewById(R.id.rb_sign_in);
        rbSignUp = (RadioButton) findViewById(R.id.rb_sign_up);
        logo = (ImageView) findViewById(R.id.logo);

        // Construct snack bar
        snackbar = Snackbar.make(findViewById(R.id.main_layout),
                        "No internet connection",
                        Snackbar.LENGTH_INDEFINITE);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // Init other component
        initAuthenticateButton();
        initActionSwitcher();
        initSnackbarReposition();
    }

    private void initSnackbarReposition() {
        findViewById(R.id.main_layout)
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {processInternetChange(AuthenticActivity.this);
            }
        });
    }

    private void initActionSwitcher() {
        pager.setAdapter(new AuthPagerAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float OFFSET_THRESHOLD = 0.45f;
                if (positionOffset < OFFSET_THRESHOLD) return;

                // Animate the logo
                logo.setRotationY(0);
                logo.animate().rotationY(360f)
                        .setDuration(650)
                        .setStartDelay(0)
                        .start();
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) rbSignIn.setChecked(true);
                else rbSignUp.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId) {
                if (checkId == rbSignIn.getId()) {
                    rbSignIn.setTypeface(null, Typeface.BOLD);
                    rbSignUp.setTypeface(null, Typeface.NORMAL);
                    pager.setCurrentItem(0);
                }
                else {
                    rbSignIn.setTypeface(null, Typeface.NORMAL);
                    rbSignUp.setTypeface(null, Typeface.BOLD);
                    pager.setCurrentItem(1);
                }
            }
        });

        rbSignIn.setChecked(true);
    }

    private void initAuthenticateButton() {
        fabAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInternetConnected)
                    Toast.makeText(AuthenticActivity.this, "Please connect to internet", Toast.LENGTH_SHORT)
                            .show();
                else {
                    ((AuthFrag) AuthPagerAdapter.getActiveFragment(pager,
                            pager.getCurrentItem(),
                            getSupportFragmentManager())).handleAuthentication();
                }
            }
        });
    }

    private void startIntroPage() {
        Thread introThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("FIRST_START", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent introIntent = new Intent(AuthenticActivity.this, IntroActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(introIntent);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("FIRST_START", false);

                    //  Apply changes
                    e.apply();
                    }
            }
        });

        introThread.start();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the state of item position
        outState.putInt(SELECTED_ITEM_POSITION, pager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Read the state of item position
        pager.setCurrentItem(savedInstanceState.getInt(SELECTED_ITEM_POSITION));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkBroadcastReceiver);
    }

    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkBroadcastReceiver, internetFilter);
    }

    public boolean getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null &&
                networkInfo.isConnected() &&
                networkInfo.isAvailable() &&
                networkInfo.isConnectedOrConnecting());
    }


}
