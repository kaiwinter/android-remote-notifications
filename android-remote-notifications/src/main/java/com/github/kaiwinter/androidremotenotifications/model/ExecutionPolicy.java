package com.github.kaiwinter.androidremotenotifications.model;

import com.github.kaiwinter.androidremotenotifications.model.impl.NotificationConfiguration;

/**
 * Defines when the notification is shown. Does not define the number of times the notification is shown. E.g. for a
 * notification which is shown once only: Use {@link #EVERY_DAY} and set {@link NotificationConfiguration#numberOfTotalViews} to 1.
 */
public enum ExecutionPolicy {

    /**
     * The notification should be shown always when API gets called. Use with caution!
     */
    ALWAYS,

    /**
     * The notification should be shown on every day.
     */
    EVERY_DAY,

    /**
     * The notification should be shown on every Monday.
     */
    EVERY_MONDAY,

    /**
     * The notification should be shown on every Sunday.
     */
    EVERY_SUNDAY,

    /**
     * The notification should be shown on 1st of every month.
     */
    EVERY_FIRST_OF_MONTH
}
