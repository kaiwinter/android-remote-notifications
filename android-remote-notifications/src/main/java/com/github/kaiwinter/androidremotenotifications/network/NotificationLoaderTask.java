package com.github.kaiwinter.androidremotenotifications.network;

import android.os.AsyncTask;
import android.util.Log;

import com.github.kaiwinter.androidremotenotifications.json.UnMarshaller;
import com.github.kaiwinter.androidremotenotifications.model.UserNotification;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Set;

import static com.github.kaiwinter.androidremotenotifications.RemoteNotifications.TAG;

/**
 * AsyncTask to load notifications from a server.
 */
public final class NotificationLoaderTask extends AsyncTask<Void, Void, Set<UserNotification>> {

    private final URL serverUrl;
    private final NotificationLoaderFinishListener listener;

    /**
     * Constructs a new NotificationLoaderTask.
     *
     * @param serverUrl the URL of the JSON file to load
     * @param listener  the NotificationLoaderFinishListener which is called after the JSON file has been loaded and parsed.
     */
    public NotificationLoaderTask(URL serverUrl, NotificationLoaderFinishListener listener) {
        this.serverUrl = serverUrl;
        this.listener = listener;
    }

    @Override
    protected Set<UserNotification> doInBackground(Void... params) {
        try {
            return UnMarshaller.getNotificationsFromJson(serverUrl);
        } catch (IOException e) {
            Log.e(TAG, "Error on requesting JSON from server", e);
            return Collections.emptySet();
        }
    }

    @Override
    protected void onPostExecute(Set<UserNotification> notifications) {
        super.onPostExecute(notifications);
        if (listener != null) {
            listener.onDownloadFinished(notifications);
        }
    }
}
