package nl.frankkie.cab;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * Created by FrankkieNL on 14-2-2015.
 */
public class ChooseSystemActivity extends Activity {

    ChooseSystemActivity thisAct;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    
        thisAct = this;
        initUI();        
    }
    
    public void initUI(){
        setContentView(R.layout.activity_choosesystem);
        findViewById(R.id.choose_bluetooth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(thisAct);
                prefs.edit().putString("connection_method","bluetooth").commit();
                Intent i = new Intent();
                i.setClass(thisAct,BluetoothConnectActivity.class);
                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                //finish();
            }
        });
    }
}
