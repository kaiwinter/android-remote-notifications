package com.github.kaiwinter.androidremotenotifications.network;

import com.github.kaiwinter.androidremotenotifications.RemoteNotifications;
import com.github.kaiwinter.androidremotenotifications.model.UpdatePolicy;
import com.github.kaiwinter.androidremotenotifications.model.UserNotification;

import java.util.Set;

/**
 * Listener to attach to {@link RemoteNotifications#updateNotificationsFromServer(UpdatePolicy, NotificationLoaderFinishListener)}
 * to be informed when the update of the Notifications is finished.
 */
public interface NotificationLoaderFinishListener {

    /**
     * Listener to attach to {@link RemoteNotifications#updateNotificationsFromServer(UpdatePolicy, NotificationLoaderFinishListener)}
     * to be informed when the update of the Notifications is finished.
     *
     * @param notifications the notifications
     */
    void onDownloadFinished(Set<UserNotification> notifications);
}
