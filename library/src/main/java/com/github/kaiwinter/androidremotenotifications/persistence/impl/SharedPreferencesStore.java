package com.github.kaiwinter.androidremotenotifications.persistence.impl;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.github.kaiwinter.androidremotenotifications.json.UnMarshaller;
import com.github.kaiwinter.androidremotenotifications.model.impl.AlertDialogNotification;
import com.github.kaiwinter.androidremotenotifications.model.impl.PersistentNotification;
import com.github.kaiwinter.androidremotenotifications.persistence.NotificationStore;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.github.kaiwinter.androidremotenotifications.RemoteNotifications.TAG;

/**
 * Implementation of {@link NotificationStore} which utilizes Androids Shared Preferences.
 */
public final class SharedPreferencesStore implements NotificationStore {

    /**
     * Key in the Shared Preferences of the date of the last server update.
     */
    private static final String LAST_SERVER_UPDATE = "LAST_SERVER_UPDATE";

    /**
     * Key in the Shared Preferences of the Persistent Notifications.
     */
    private static final String PERSISTENT_NOTIFICATIONS = "PERSISTENT_NOTIFICATIONS";

    private final SharedPreferences sharedPreferences;

    /**
     * Constructs a new {@link SharedPreferencesStore} and initializes it with the underlying {@link SharedPreferences}
     * object.
     *
     * @param sharedPreferences The {@link SharedPreferences} instance which is used to store {@link AlertDialogNotification}s.
     */
    public SharedPreferencesStore(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Date getLastServerUpdate() {
        if (sharedPreferences.contains(LAST_SERVER_UPDATE)) {
            return new Date(sharedPreferences.getLong(LAST_SERVER_UPDATE, 0));
        }
        return null;
    }

    @Override
    public void saveLastServerUpdate(Date date) {
        Log.v(TAG, date.toString());
        Editor edit = sharedPreferences.edit();
        edit.putLong(LAST_SERVER_UPDATE, date.getTime());
        edit.apply();
    }

    @Override
    public Set<PersistentNotification> getPersistentNotifications() {
        Set<PersistentNotification> persistentNotifications = new HashSet<>();

        if (sharedPreferences.contains(PERSISTENT_NOTIFICATIONS)) {
            String json = sharedPreferences.getString(PERSISTENT_NOTIFICATIONS, null);
            try {
                persistentNotifications = UnMarshaller.getPersistentNotificationsFromJson(json);
            } catch (IOException e) {
                Log.e(TAG, "Error loading Persistent Notifications", e);
            }
        }

        Log.v(TAG, "Loaded " + persistentNotifications.size() + " Persistent Notifications");
        return persistentNotifications;
    }

    @Override
    public void updatePersistentNotification(Set<PersistentNotification> updatePersistentNotifications) {
        Set<PersistentNotification> persistentNotifications = getPersistentNotifications();
        for (PersistentNotification updatePersistentNotification : updatePersistentNotifications) {
            Log.v(TAG, "Updating " + updatePersistentNotification.toString());
            persistentNotifications.remove(updatePersistentNotification);
            persistentNotifications.add(updatePersistentNotification);
        }
        replacePersistentNotifications(persistentNotifications);
    }

    @Override
    public void replacePersistentNotifications(Set<PersistentNotification> persistentNotifications) {
        try {
            String json = UnMarshaller.getJsonFromPersistentNotifications(persistentNotifications);
            Editor editor = sharedPreferences.edit();
            editor.putString(PERSISTENT_NOTIFICATIONS, json);
            editor.apply();

            Log.v(TAG, "Saved " + persistentNotifications.size() + " Persistent Notifications");
        } catch (IOException e) {
            Log.e(TAG, "Error saving Persistent Notifications", e);
        }
    }

    @Override
    public void clearPersistentNotifications() {
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Log.v(TAG, "Persistent Notifications removed");
    }
}
