package nl.frankkie.cab;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.plus.Plus;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = "CardsAgainstBlackjack";

    GoogleApiClient.ConnectionCallbacks mConnectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            Log.v(TAG, "onConnected " + bundle);
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.v(TAG, "onConnectionSuspended " + i);
        }
    };

    GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.v(TAG, "onConnectionFailed " + connectionResult);
        }
    };

    RealTimeMessageReceivedListener mRealTimeMessageReceivedListener = new RealTimeMessageReceivedListener() {
        @Override
        public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
            Log.v(TAG, "onRealTimeMessageReceived " + realTimeMessage);
        }
    };

    RoomStatusUpdateListener mRoomStatusUpdateListener = new RoomStatusUpdateListener() {
        @Override
        public void onRoomConnecting(Room room) {
            Log.v(TAG, "onRoomConnecting " + room);
        }

        @Override
        public void onRoomAutoMatching(Room room) {
            Log.v(TAG, "onRoomAutoMatching " + room);
        }

        @Override
        public void onPeerInvitedToRoom(Room room, List<String> strings) {
            Log.v(TAG, "onPeerInvitedToRoom " + room + " " + strings);
        }

        @Override
        public void onPeerDeclined(Room room, List<String> strings) {
            Log.v(TAG, "onPeerDeclined " + room + " " + strings);

        }

        @Override
        public void onPeerJoined(Room room, List<String> strings) {
            Log.v(TAG, "onPeerJoined " + room + " " + strings);
        }

        @Override
        public void onPeerLeft(Room room, List<String> strings) {
            Log.v(TAG, "onPeerLeft " + room + " " + strings);
        }

        @Override
        public void onConnectedToRoom(Room room) {
            Log.v(TAG, "onConnectedToRoom " + room);
        }

        @Override
        public void onDisconnectedFromRoom(Room room) {
            Log.v(TAG, "onDisconnectedFromRoom " + room);
        }

        @Override
        public void onPeersConnected(Room room, List<String> strings) {
            Log.v(TAG, "onPeersConnected " + room + " " + strings);
        }

        @Override
        public void onPeersDisconnected(Room room, List<String> strings) {
            Log.v(TAG, "onPeerDisconnected " + room + " " + strings);
        }

        @Override
        public void onP2PConnected(String s) {
            Log.v(TAG, "onP2PConnected " + s);
        }

        @Override
        public void onP2PDisconnected(String s) {
            Log.v(TAG, "onP2PDisconnected " + s);
        }
    };

    GoogleApiClient mGoogleApiClient;

    //Request Codes for StartActivityForResult
    //https://github.com/playgameservices/android-basic-samples/blob/master/BasicSamples/ButtonClicker/src/main/java/com/google/example/games/bc/MainActivity.java#L94
    public static final int RC_SELECT_PLAYERS = 10000;
    public static final int RC_INVITATION_INBOX = 10001;
    public static final int RC_WAITING_ROOM = 10002;
    public static final int RC_SIGN_IN = 9001;

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = true;

    // Room ID where the currently active game is taking place; null if we're
    // not playing.
    String mRoomId = null;

    // Are we playing in multiplayer mode?
    boolean mMultiplayer = false;

    // The participants in the currently active game
    ArrayList<Participant> mParticipants = null;

    // My participant ID in the currently active game
    String mMyId = null;

    // If non-null, this is the id of the invitation we received via the
    // invitation listener
    String mIncomingInvitationId = null;

    // Message buffer for sending messages
    byte[] mMsgBuf = new byte[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleApiClient.Builder b = new GoogleApiClient.Builder(this);
        b.addConnectionCallbacks(mConnectionCallbacks).addOnConnectionFailedListener(mOnConnectionFailedListener).
                addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).addApi(Games.API).addScope(Games.SCOPE_GAMES);
        mGoogleApiClient = b.build();
    }

}
