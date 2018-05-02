package com.github.kaiwinter.androidremotenotifications.model;

import android.content.Context;

import com.github.kaiwinter.androidremotenotifications.model.impl.NotificationConfiguration;

/**
 * Interface for different types of Notifications.
 */
public interface UserNotification {

    /**
     * Returns the {@link NotificationConfiguration} of this Notification.
     *
     * @return the {@link NotificationConfiguration} of this Notification.
     */
    NotificationConfiguration getNotificationConfiguration();

    /**
     * Shows the Notification to the user.
     *
     * @param context the {@link Context} of the app.
     */
    void show(Context context);
}
