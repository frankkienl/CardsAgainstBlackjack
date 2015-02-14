package nl.frankkie.cab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import nl.frankkie.cab.util.GoogleApiUtil;

/**
 * Created by FrankkieNL on 18-1-2015.
 */
public class GooglePlayLoginActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, 
        GoogleApiClient.OnConnectionFailedListener {

    //Google Plus Stuff
    //https://github.com/googleplus/gplus-quickstart-android
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final String SAVED_PROGRESS = "sign_in_progress";
    private static final int RC_SIGN_IN = 0; //requestcode
    //
    private GoogleApiClient mGoogleApiClient;
    private int mSignInProgress;
    private PendingIntent mSignInIntent;
    private int mSignInError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initGooglePlus();

        initUI();
    }

    public void initGooglePlus() {
        mGoogleApiClient = buildGoogleApiClient();
    }

    private GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    public void initUI() {
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GooglePlayLoginActivity.this, R.string.logging_in, Toast.LENGTH_LONG).show();
                resolveSignInError();
            }
        });
        findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Games.signOut(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                mGoogleApiClient = null; //kill old instance
                initGooglePlus(); //make new client
                //delete data
                GoogleApiUtil.setUserLoggedIn(GooglePlayLoginActivity.this, false);
                GoogleApiUtil.setUserEmail(GooglePlayLoginActivity.this, "");
                GoogleApiUtil.setUserNickname(GooglePlayLoginActivity.this, "");
                //
                findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                findViewById(R.id.sign_out_button).setVisibility(View.GONE);
                findViewById(R.id.view_achievements).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.view_achievements).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GoogleApiUtil.showAchievements(LoginActivity.this, mGoogleApiClient);
                Intent i = new Intent(GooglePlayLoginActivity.this, GameActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGoogleApiClient.connect();
    }

    private void resolveSignInError() {
        if (mSignInIntent != null) {
            //Start intent to let user login
            try {
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {
            //Show Error Dialog
            if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
                Dialog d =  GooglePlayServicesUtil.getErrorDialog(
                        mSignInError,
                        this,
                        RC_SIGN_IN,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                Log.e(GooglePlayLoginActivity.this.getString(R.string.app_name), "Google Play services resolution cancelled");
                                mSignInProgress = STATE_DEFAULT;
                            }
                        });
                d.show();
            } else {
                Toast.makeText(GooglePlayLoginActivity.this,R.string.google_play_games_error,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, mSignInProgress);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        String currentUserEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
        GoogleApiUtil.setUserEmail(this, currentUserEmail);
        //Ask user for Nickname (forum-name)
        //askUserForNickname(currentUser);
        //Now, set logged in and stuff.
        GoogleApiUtil.setUserLoggedIn(this, true);
        //Toast.makeText(LoginActivity.this,"Logged In", Toast.LENGTH_SHORT).show();
        //Remove login button, as already logged in.
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        findViewById(R.id.view_achievements).setVisibility(View.VISIBLE);
        //Unlock the first Achievement
        //Games.Achievements.unlock(mGoogleApiClient,getString(R.string.achievement_ready_to_go));
        //Do some sync here
    }

    public void askUserForNickname(final Person currentUser){
        if (GoogleApiUtil.isUserLoggedIn(this)
                || !"".equals(GoogleApiUtil.getUserNickname(this))){
            //Is already logged in, no need to ask for username
            return;
        }
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(R.string.ask_nickname);
        final EditText ed = new EditText(this);
        ed.setHint(R.string.ask_nickname);
        b.setView(ed);
        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nickname = ed.getText().toString();
                GoogleApiUtil.setUserNickname(GooglePlayLoginActivity.this, nickname);
            }
        });
        b.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //no nickname? we use your name from Google Plus                                
                String nickname = currentUser.getDisplayName();
                GoogleApiUtil.setUserNickname(GooglePlayLoginActivity.this, nickname);
            }
        });
        b.create().show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Google Play Services: Connection Suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Toast.makeText(this,"Google Play Services: Connection Failed", Toast.LENGTH_LONG).show();
        //this can happen when user is not logged in yet, so don't display error message in that case.
        mSignInIntent = connectionResult.getResolution();
    }
}