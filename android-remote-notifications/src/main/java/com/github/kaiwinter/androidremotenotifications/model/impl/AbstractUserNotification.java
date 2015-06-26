package com.github.kaiwinter.androidremotenotifications.model.impl;

import com.github.kaiwinter.androidremotenotifications.model.UserNotification;

/**
 * Abstract implementation of {@link UserNotification} which implements common functionality.
 */
public abstract class AbstractUserNotification implements UserNotification {

    private static final NotificationConfiguration DEFAULT_CONFIGURATION = new NotificationConfiguration();

    /**
     * The configuration about when to show this Notification.
     */
    private final NotificationConfiguration notificationConfiguration;

    public AbstractUserNotification() {
        this(DEFAULT_CONFIGURATION);
    }

    public AbstractUserNotification(NotificationConfiguration notificationConfiguration) {
        if (notificationConfiguration == null) {
            this.notificationConfiguration = DEFAULT_CONFIGURATION;
        } else {
            this.notificationConfiguration = notificationConfiguration;
        }
    }

    @Override
    public NotificationConfiguration getNotificationConfiguration() {
        return notificationConfiguration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractUserNotification that = (AbstractUserNotification) o;
        return !(notificationConfiguration != null ? !notificationConfiguration.equals(that.notificationConfiguration) : that.notificationConfiguration != null);
    }

    @Override
    public int hashCode() {
        return notificationConfiguration != null ? notificationConfiguration.hashCode() : 0;
    }
}
