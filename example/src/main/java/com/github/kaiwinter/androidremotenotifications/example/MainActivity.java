package com.github.kaiwinter.androidremotenotifications.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.kaiwinter.androidremotenotifications.RemoteNotifications;
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

    private static final String JSON_URL = "https://raw.githubusercontent.com/kaiwinter/android-remote-notifications/master/example/src/test/resources/notifications.json";
    private RemoteNotifications remoteNotifications;

    private TextView textViewStatus;

    private TextView textViewNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name_long);

        textViewStatus = findViewById(R.id.textViewStatus);
        textViewNotifications = findViewById(R.id.textViewNotifications);

        initializeARN();
    }

    private void initializeARN() {
        try {
            URL url = new URL(JSON_URL);
            remoteNotifications = new RemoteNotifications(this, url);
        } catch (MalformedURLException e) {
            Log.e("ARN-example", "URL cannot be parsed", e);
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
        remoteNotifications.updateNotificationsFromServer(UpdatePolicy.NOW, listener);
    }

    public void showNotifications(View v) {
        remoteNotifications.showPendingNotificationsToUser(true);
        updateNotificationText();
    }

    public void deleteLocalNotifications(View v) {
        remoteNotifications.getPreferenceStore().clearPersistentNotifications();
        textViewStatus.setText("Deleted notifications");
        updateNotificationText();
    }

    private void updateNotificationText() {
        textViewNotifications.setText(remoteNotifications.getPreferenceStore().getPersistentNotifications().toString());
    }
}
