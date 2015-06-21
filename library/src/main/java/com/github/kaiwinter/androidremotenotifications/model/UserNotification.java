package com.github.kaiwinter.androidremotenotifications.model;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.kaiwinter.androidremotenotifications.model.impl.AlertDialogNotification;
import com.github.kaiwinter.androidremotenotifications.model.impl.NotificationConfiguration;
import com.github.kaiwinter.androidremotenotifications.model.impl.ToastNotification;

/**
 * Interface for different types of Notifications.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ //
        @JsonSubTypes.Type(value = ToastNotification.class),//
        @JsonSubTypes.Type(value = AlertDialogNotification.class), //
})
public interface UserNotification {

    /**
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
