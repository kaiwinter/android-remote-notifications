package com.github.kaiwinter.androidremotenotifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.github.kaiwinter.androidremotenotifications.model.UpdatePolicy;
import com.github.kaiwinter.androidremotenotifications.model.UserNotification;
import com.github.kaiwinter.androidremotenotifications.model.impl.PersistentNotification;
import com.github.kaiwinter.androidremotenotifications.network.NotificationLoaderFinishListener;
import com.github.kaiwinter.androidremotenotifications.network.NotificationLoaderTask;
import com.github.kaiwinter.androidremotenotifications.persistence.NotificationStore;
import com.github.kaiwinter.androidremotenotifications.persistence.impl.SharedPreferencesStore;

import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The starting class of this library. Construct an instance with the URL to your JSON file on a server and call
 * {@link #updateNotificationsFromServer(UpdatePolicy)}. This will load the configured notifications and store it in the
 * libraries store. Call {@link #showPendingNotificationsToUser(boolean)} to automatically show notifications to the
 * user by an {@link AlertDialog}.
 */
public final class Anp {

    public static final String TAG = "Anp";

    private static final String ANP_PREFERENCES_NAME = "Anp";

    private final URL serverUrl;
    private final Context context;
    private final NotificationStore preferenceStore;
    //private final NotificationConsumer notificationConsumer;

    private final Integer appVersionCode;

    /**
     * This guard is used for two things:
     * <ul>
     * <li>doing the notification update only once at a time</li>
     * <li>don't try to show notifications when update is running. In this case the show event is scheduled and {@link #scheduledShowEvent} is set to true.</li>
     * </ul>
     * Guard to run the notification only one at a time.
     */
    private AtomicBoolean notificationUpdateRunning = new AtomicBoolean(false);

    /**
     * This is set to true if {@link #showPendingNotificationsToUser} was called then an update of the notification was running.
     * It is used to show the notifications after the update has finished.
     */
    private AtomicBoolean scheduledShowEvent = new AtomicBoolean(false);
    private boolean scheduledShowEventParameter;

    /**
     * Constructs a new {@link Anp}. The Notifications from the server and the last server update timestamp will be
     * stored in a Shared Preferences file with the name {@value #ANP_PREFERENCES_NAME}. To change the name use
     * {@link #Anp(URL, Context, String)}.
     *
     * @param serverUrl The absolute URL to the notification JSON on a server (or file system), not <code>null</code>.
     */
    public Anp(URL serverUrl, Context context) {
        this(serverUrl, context, ANP_PREFERENCES_NAME);
    }

    /**
     * Constructs a new {@link Anp}.
     *
     * @param serverUrl            The absolute URL to the notification JSON on a server (or file system), not <code>null</code>.
     * @param sharedPreferenceName The file name to use for the Shared Preferences file.
     */
    public Anp(URL serverUrl, Context context, String sharedPreferenceName) {
        if (serverUrl == null) {
            throw new IllegalArgumentException("serverUrl must not be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        if (sharedPreferenceName == null) {
            throw new IllegalArgumentException("sharedPreferenceName must not be null");
        }
        this.serverUrl = serverUrl;
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
        this.preferenceStore = new SharedPreferencesStore(sharedPreferences);
        //this.notificationConsumer = new DialogNotificationConsumer();
        this.appVersionCode = getAppVersionCode(context);
    }

    private Integer getAppVersionCode(Context context) {
        int versionCode;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e1) {
            versionCode = -1;
        }
        return versionCode;
    }

    /**
     * Shows all due notifications. If a server update of the notifications is running this call gets scheduled and is
     * executed automatically after the update has finished.
     *
     * @see #showPendingNotificationsToUser(boolean)
     */
    public void showPendingNotificationsToUser() {
        showPendingNotificationsToUser(true);
    }

    /**
     * Shows due notifications. If a server update of the notifications is running this call gets scheduled and is
     * executed automatically after the update has finished.
     *
     * @param showAll if <code>true</code> all pending notification dialogs will be shown (may result in bad user experience
     *                if several dialogs are popping up). If <code>false</code> only one dialog will be shown on each call
     *                of this method.
     */
    public void showPendingNotificationsToUser(boolean showAll) {

        if (notificationUpdateRunning.get()) {
            Log.w(TAG, "Notification update running, scheduling show event");
            scheduledShowEvent.set(true);
            // store passed parameter
            scheduledShowEventParameter = showAll;
        }

        Set<PersistentNotification> persistentNotifications = preferenceStore.getPersistentNotifications();

        Set<PersistentNotification> updateNotifications = new HashSet<>();
        for (PersistentNotification persistentNotification : persistentNotifications) {
            if (persistentNotification.hasToBeShown(appVersionCode)) {
                //notificationConsumer.show(context, persistentNotification.getNotification());
                persistentNotification.getNotification().show(context);
                persistentNotification.setLastShown(new Date());
                persistentNotification.setShownCounter(persistentNotification.getShownCounter() + 1);
                updateNotifications.add(persistentNotification);
                if (!showAll) {
                    break;
                }
            }
        }

        if (!updateNotifications.isEmpty()) {
            preferenceStore.updatePersistentNotification(updateNotifications);
        }
    }

    public void updateNotificationsFromServer(UpdatePolicy updatePolicy, final NotificationLoaderFinishListener listener) {
        if (updatePolicy == null) {
            throw new IllegalArgumentException("updatePolicy must not be null");
        }

        Date date = preferenceStore.getLastServerUpdate();
        boolean shouldUpdate = updatePolicy.shouldUpdate(date);
        Log.v(TAG, "UpdatePolicy: " + updatePolicy.toString() + ", shouldUpdate: " + shouldUpdate);
        if (!shouldUpdate) {
            return;
        }

        boolean updateRunning = notificationUpdateRunning.getAndSet(true);
        if (updateRunning) {
            Log.w(TAG, "Notification update already running, skipping this update");
            return;
        }

        NotificationLoaderFinishListener internalListener = new NotificationLoaderFinishListener() {
            @Override
            public void onDownloadFinished(Set<UserNotification> notifications) {
                Log.v(TAG, "Received " + notifications.size() + " notifications");
                handleNotificationsFromServer(notifications);
                notificationUpdateRunning.set(false);
                if (scheduledShowEvent.getAndSet(false)) {
                    Log.v(TAG, "executing scheduled show event");
                    showPendingNotificationsToUser(scheduledShowEventParameter);
                }

                // Call custom listener
                if (listener != null) {
                    listener.onDownloadFinished(notifications);
                }
            }
        };
        NotificationLoaderTask notificationLoaderTask = new NotificationLoaderTask(serverUrl, internalListener);
        notificationLoaderTask.execute();
    }

    /**
     * Starts an AsyncTask which loads the notifications from the server, removes local notifications which doesn't exist anymore and adds new
     * one.
     *
     * @param updatePolicy Use this parameter to reduce the number of server calls, not <code>null</code>.
     */
    public void updateNotificationsFromServer(UpdatePolicy updatePolicy) {
        updateNotificationsFromServer(updatePolicy, null);
    }

    private void handleNotificationsFromServer(Set<UserNotification> notifications) {
        // Remove notifications from app which aren't anymore in the server notifications
        Set<PersistentNotification> persistentNotifications = preferenceStore.getPersistentNotifications();

        boolean save = false;
        for (Iterator<PersistentNotification> iterator = persistentNotifications.iterator(); iterator.hasNext(); ) {
            PersistentNotification persistentNotification = iterator.next();
            if (!notifications.contains(persistentNotification.getNotification())) {
                // Remove notification
                iterator.remove();
                save = true;
                Log.v(TAG, "Removed Persistent Notification " + persistentNotification.toString());
            }
        }

        // Add notifications from server which aren't in the app notification
        for (UserNotification notification : notifications) {
            PersistentNotification persistentNotification = new PersistentNotification(notification);
            if (!persistentNotifications.contains(persistentNotification)) {
                // Add new notification
                persistentNotifications.add(persistentNotification);
                save = true;
                Log.v(TAG, "Added Persistent Notification " + persistentNotification.toString());
            }
        }

        if (save) {
            preferenceStore.replacePersistentNotifications(persistentNotifications);
        }
    }

    public NotificationStore getPreferenceStore() {
        return preferenceStore;
    }
}
