package com.github.kaiwinter.androidremotenotifications.model.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.kaiwinter.androidremotenotifications.model.UserNotification;

import java.util.Calendar;
import java.util.Date;

/**
 * A {@link PersistentNotification} is a {@link UserNotification} which can be stored in the shared preferences. It stores
 * metadata about the {@link UserNotification} such as last shown date etc.
 */
public final class PersistentNotification {

    /**
     * The notification to show
     */
    private final UserNotification notification;

    /**
     * The time when the notification was shown the last time
     */
    private Date lastShown;

    /**
     * The number of times the notification was shown already
     */
    private int shownCounter = 0;

    /**
     * @param notification The {@link AlertDialogNotification} to persist, not <code>null</code>.
     */
    public PersistentNotification(@JsonProperty("notification") UserNotification notification) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification must not be null");
        }
        this.notification = notification;
    }

    public boolean hasToBeShown(int appVersionCode) {
        NotificationConfiguration notificationConfiguration = notification.getNotificationConfiguration();

        Date now = new Date();
        if (notificationConfiguration.getStartShowingDate() != null && now.before(notificationConfiguration.getStartShowingDate())) {
            // Should not be shown yet
            return false;
        }

        // if notification was shown configured times return immediately
        if (notificationConfiguration.getNumberOfTotalViews() != null && shownCounter >= notificationConfiguration.getNumberOfTotalViews()) {
            //Log.v(TAG, "Not showing notification, max number of views reached.");
            return false;
        }

        if (notificationConfiguration.getVersionCodePolicy() != null) {
            boolean hasBeShownForThisVersion = notificationConfiguration.getVersionCodePolicy().hasBeShownForThisVersion(
                    appVersionCode);
            if (!hasBeShownForThisVersion) {
                return false;
            }
        }

        if (lastShown == null) {
            return true;
        }

        if (notificationConfiguration.getExecutionPolicy() == null) {
            return true;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastShown);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        switch (notificationConfiguration.getExecutionPolicy()) {
            case ALWAYS:
                return true;

            case EVERY_DAY:
                // Calculate date for next midnight
                calendar.add(Calendar.DATE, 1);
                Date nextDayMidnight = calendar.getTime();
                return now.after(nextDayMidnight);

            case EVERY_FIRST_OF_MONTH:
                // Calculate 1st of next month
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                Date nextFirstOfMonthMidnight = calendar.getTime();
                return now.after(nextFirstOfMonthMidnight);

            case EVERY_MONDAY:
                Date nextMondayMidnight = nextDayOfWeek(calendar, Calendar.MONDAY);
                return now.after(nextMondayMidnight);

            case EVERY_SUNDAY:
                Date nextSundayMidnight = nextDayOfWeek(calendar, Calendar.SUNDAY);
                return now.after(nextSundayMidnight);

            default:
                throw new UnsupportedOperationException("Unknown execution mode: "
                        + notificationConfiguration.getExecutionPolicy().name());
        }
    }

    private static Date nextDayOfWeek(Calendar calendar, int dow) {
        int diff = dow - calendar.get(Calendar.DAY_OF_WEEK);
        if (!(diff > 0)) {
            diff += 7;
        }
        calendar.add(Calendar.DAY_OF_MONTH, diff);
        return calendar.getTime();
    }

    public Date getLastShown() {
        return lastShown;
    }

    public void setLastShown(Date lastShown) {
        this.lastShown = lastShown;
    }

    public int getShownCounter() {
        return shownCounter;
    }

    public void setShownCounter(int shownCounter) {
        this.shownCounter = shownCounter;
    }

    public UserNotification getNotification() {
        return notification;
    }

    @Override
    public String toString() {
        return "PersistentNotification [lastShown=" + lastShown + ", shownCounter=" + shownCounter + ", notification="
                + notification + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((notification == null) ? 0 : notification.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PersistentNotification other = (PersistentNotification) obj;
        if (notification == null) {
            if (other.notification != null)
                return false;
        } else if (!notification.equals(other.notification))
            return false;
        return true;
    }
}
