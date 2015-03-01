package com.idealsolution.smartwaiter.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.ui.MainActivity;

public class SincronizarReceiver extends BroadcastReceiver {
    private static final int SYNC_NOTIFY_ID=1000;
    @Override
    public void onReceive(Context context, Intent intentFrom) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        boolean exito=intentFrom.getBooleanExtra("exito",false);
        String mensajeError=intentFrom.getStringExtra("mensaje");
        NotificationManager notificationMgr= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
        Intent intentTo=new Intent(context, MainActivity.class);
        intentTo.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        builder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis());
        if(exito){
            builder.setContentTitle(context.getString(R.string.sync_notify_title_complete))
                    .setContentText(context.getText(R.string.sync_notify_text_complete))
                    .setSmallIcon(android.R.drawable.ic_popup_sync)
                    .setTicker(context.getString(R.string.sync_notify_title_complete));        }
        else{
            builder.setContentTitle(context.getString(R.string.sync_notify_title_error))
                    .setContentText(mensajeError)
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setTicker(context.getString(R.string.sync_notify_title_error));
        }
        PendingIntent pendingIntent=
                PendingIntent.getActivity(context,SYNC_NOTIFY_ID + 2 ,intentTo,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setDefaults(Notification.DEFAULT_SOUND| Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        builder.setContentIntent(pendingIntent);
        notificationMgr.notify(SYNC_NOTIFY_ID,builder.build());

    }
}
