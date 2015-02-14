package nl.frankkie.cab;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by FrankkieNL on 14-2-2015.
 */
public class BluetoothConnectActivity extends Activity {

    BluetoothConnectActivity thisAct;
    boolean isHost = false;
    boolean startedHost = false;
    boolean enableItMyself = false; //Enable bluetooth without user-consent.
    public static final int REQUEST_ENABLE_BT = 9001;
    BluetoothStateBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisAct = this;
        initUI();
        initBluetooth();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        broadcastReceiver = new BluetoothStateBroadcastReceiver();
        Intent stickyIntent = registerReceiver(broadcastReceiver, filter);
        if (stickyIntent != null) {
            Log.e("CAB", "STATE: " + stickyIntent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1));
            Log.e("CAB", "PREVIOUS_STATE" + stickyIntent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, -1));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    public void initUI() {
        setContentView(R.layout.activity_bluetoothconnect);
        findViewById(R.id.bluetooth_host).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBluetoothHost();
            }
        });
        
        findViewById(R.id.bluetooth_join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        //Disable Start button till at least 2 players have joined. (min 3 players to play)
        findViewById(R.id.bluetooth_start).setEnabled(false);
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.isEnabled()){
            findViewById(R.id.bluetooth_host).setEnabled(false);
            findViewById(R.id.bluetooth_join).setEnabled(false);
        }
    }

    public void initBluetooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            Toast.makeText(thisAct, "This device does not support bluetooth!!", Toast.LENGTH_LONG).show();
            thisAct.finish();
            return;
        }
        if (!adapter.isEnabled()) {
            if (enableItMyself) {
                adapter.enable();
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(thisAct, "Okay, enabling Bluetooth...", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(thisAct, "Bluetooth is required", Toast.LENGTH_LONG).show();
                //go back one screen
                thisAct.finish();
                return;
            }
        }
    }

    public void startBluetoothHost() {
        if (startedHost) {
            //press again, is cancel
            Toast.makeText(thisAct, "Stopping Bluetooth host", Toast.LENGTH_LONG).show();
            startedHost = false;
            isHost = false;
            //revert button text
            ((Button) findViewById(R.id.bluetooth_host)).setText("Host");
            return;
        }
        startedHost = true;
        isHost = true;
        Toast.makeText(thisAct, "Starting Bluetooth host, wait for others to join", Toast.LENGTH_LONG).show();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(thisAct);
        prefs.edit().putBoolean("bluetooth_is_host", true).commit();        
        //cannot join if you're the host.
        findViewById(R.id.bluetooth_join).setEnabled(false);
        ((Button) findViewById(R.id.bluetooth_host)).setText("Cancel Host");
        //
    }

    public class BluetoothStateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (!adapter.isEnabled()) {
                Toast.makeText(thisAct, "Bluetooth is enabled", Toast.LENGTH_LONG).show();
                findViewById(R.id.bluetooth_host).setEnabled(true);
                if (!isHost) {
                    findViewById(R.id.bluetooth_join).setEnabled(true);
                }
            }
        }
    }

    public class BluetoothServerThread extends Thread {
        

    }
}
