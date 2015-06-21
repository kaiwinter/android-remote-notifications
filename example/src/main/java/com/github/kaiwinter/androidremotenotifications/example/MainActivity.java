package com.github.kaiwinter.androidremotenotifications.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.kaiwinter.androidremotenotifications.Anp;
import com.github.kaiwinter.androidremotenotifications.model.UpdatePolicy;
import com.github.kaiwinter.androidremotenotifications.model.UserNotification;
import com.github.kaiwinter.androidremotenotifications.network.NotificationLoaderFinishListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/**
 * Sample app.
 */
public class MainActivity extends Activity {

    private Anp anp;

    private TextView textViewStatus;

    private TextView textViewNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name_long);

        initializeARN();
    }

    private void initializeARN() {
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        textViewNotifications = (TextView) findViewById(R.id.textViewNotifications);

        try {
            URL url = new URL("http://www.vorlesungsfrei.de/files/test/notifications.json");
            anp = new Anp(url, this);
        } catch (MalformedURLException e) {
            Log.e("ANL-example", "URL cannot be parsed", e);
        }

        updateNotificationText();
    }

    public void updateNotificationFromServer(View v) {
        NotificationLoaderFinishListener listener = new NotificationLoaderFinishListener() {
            @Override
            public void onDownloadFinished(Set<UserNotification> notifications) {
                updateNotificationText();

                textViewStatus.setText("Status: loaded " + notifications.size() + " notifications");
            }
        };

        textViewStatus.setText("Status: running");
        anp.updateNotificationsFromServer(UpdatePolicy.NOW, listener);
    }

    public void showNotifications(View v) {
        anp.showPendingNotificationsToUser(true);
        updateNotificationText();
    }

    public void deleteLocalNotifications(View v) {
        anp.getPreferenceStore().clearPersistentNotifications();
        textViewStatus.setText("Deleted notifications");
        updateNotificationText();
    }

    private void updateNotificationText() {
        textViewNotifications.setText(anp.getPreferenceStore().getPersistentNotifications().toString());
    }
}
