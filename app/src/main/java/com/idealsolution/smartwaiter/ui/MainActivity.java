package com.idealsolution.smartwaiter.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract;
import com.idealsolution.smartwaiter.service.SincronizarService;


public class MainActivity extends Activity implements View.OnClickListener{
    private Button mSincronizarButton;
    private RelativeLayout mSpinnerLayout;
    private RelativeLayout mSincronizarLayout;
    private ImageView mImage;
    private TextView mMensajeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get references to widgets
        mSincronizarButton = (Button) findViewById(R.id.sincronizarButton);
        mSpinnerLayout = (RelativeLayout) findViewById(R.id.sincronizarSpinnerLayout);
        mSincronizarLayout = (RelativeLayout) findViewById(R.id.sincronizarLayout);

        mMensajeTextView=(TextView)findViewById(R.id.syncDescripcionLabel);
        mImage=(ImageView)findViewById(R.id.syncImageView);
        // set listeners
        mSincronizarButton.setOnClickListener(this);

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
    public void onClick(View v) {
        // here get SharedPreferences and send them with the Intent
        Intent inputIntent = new Intent(MainActivity.this,
                SincronizarService.class);
        Log.d(SmartWaiterContract.TAG, "Antes de startService");
        // Display progress to the user
        showProgressIndicator(true);
        mSincronizarButton.setEnabled(false);
        startService(inputIntent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(syncDataReceiver);
        Log.d(SmartWaiterContract.TAG, "Entre a onPause - SincronizarActivity");

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(SmartWaiterContract.TAG, "Entre a onResume - SincronizarActivity");
        IntentFilter filter = new IntentFilter(
                SincronizarService.ACTION_SYNC_DATA);
        filter.setPriority(2);
        registerReceiver(syncDataReceiver, filter);
        if (!isMyServiceRunning(SincronizarService.class)) {
            boolean isDataSynchronized =false;
//                    mPrefControl.getBoolean(
//                    PREF_Control.DATA_SINCRONIZADA, false);
            if (isDataSynchronized) { // if data is already synchronized

                enableControles(true);

            } else { // data is not synchronized yet

                enableControles(false);
//                String exceptionMessageInService= mPrefControl.getString(
//                        PREF_Control.EXCEPCION_SERVICIO, "");
//                if(exceptionMessageInService!=""){
//
//                    //Clear out the value of 'excepcionServicio'
//                    //PREF_Control.save(mPrefControl, null,null, null, null, null, "", false);
//                    Toast.makeText(MainActivity.this,
//                            exceptionMessageInService,
//                            Toast.LENGTH_LONG).show();
//                }
            }
        }

    }

    private void enableControles(boolean isDataSynchronized){
        Drawable imageToDisplay;
        String mensaje;
        showProgressIndicator(false);
        if (isDataSynchronized) {
            imageToDisplay = getResources()
                    .getDrawable(R.drawable.ic_completed);
            mensaje = getString(R.string.sincronizarCompleto_label);
            mSincronizarButton.setVisibility(View.GONE);
        } else {
            imageToDisplay = getResources()
                    .getDrawable(R.drawable.ic_sync_data);
            mensaje = getString(R.string.sincronizarDescripcion_label);
            mSincronizarButton.setVisibility(View.VISIBLE);
            mSincronizarButton.setEnabled(true);
        }
        mImage.setImageDrawable(imageToDisplay);
        mMensajeTextView.setText(mensaje);
    }
    private BroadcastReceiver syncDataReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("QuickOrder", "SyncData broadcast received");
            // if necessary get data from intent
            boolean exito = intent.getBooleanExtra("exito", false);

            abortBroadcast();
            String mensaje;
            if (exito) {
                mensaje = String.valueOf(intent.getIntExtra("resultado", 0));
                enableControles(true);
                Log.d(SmartWaiterContract.TAG,
                        "Success from BroadcastReceiver within SincronizarActivity : "
                                + mensaje);

            } else {
                mensaje = intent.getStringExtra("mensaje");
                enableControles(false);
                Log.d("QuickOrder",
                        "Exception from BroadcastReceiver within SincronizarActivity :"
                                + mensaje);
                // update the display
                //PREF_Control.save(mPrefControl, null,null, null, null, null, "", false);
                Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG)
                        .show();
            }


        }

    };
    private void showProgressIndicator(boolean showValue) {
        if (showValue) {
            mSincronizarLayout.setVisibility(View.GONE);
            mSpinnerLayout.setVisibility(View.VISIBLE);
        } else {
            mSincronizarLayout.setVisibility(View.VISIBLE);
            mSpinnerLayout.setVisibility(View.GONE);
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
