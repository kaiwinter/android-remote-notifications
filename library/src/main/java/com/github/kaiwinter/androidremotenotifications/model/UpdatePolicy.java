package com.github.kaiwinter.androidremotenotifications.model;

import java.util.Date;

/**
 * Defines how often the notifications are updated from the server. Doing this on each start of your app ({@link #NOW})
 * might lead to too many server calls. You can reduce them by using {@link #WEEKLY} or {@link #MONTHLY}.
 */
public enum UpdatePolicy {

    /**
     * The update is made now, regardless when the last one was.
     */
    NOW(0),

    /**
     * The update is made once a week only. @{link Anp} uses an internal shared preference to track when the last update
     * was.
     */
    WEEKLY(7 * 24 * 60 * 60 * 1000),

    /**
     * The update is made once a month only. @{link Anp} uses an internal shared preference to track when the last
     * update was.
     */
    MONTHLY(WEEKLY.getInterval() * 4);

    private final long interval;

    /**
     * @param interval Interval in which no updates should be triggered.
     */
    UpdatePolicy(long interval) {
        this.interval = interval;
    }

    private long getInterval() {
        return interval;
    }

    /**
     * Checks if the interval of this {@link UpdatePolicy} is over in regard to the <code>lastUpdate</code>.
     *
     * @param lastUpdate The {@link Date} of the last update.
     * @return <code>true</code> if an update should be done, else <code>false</code>.
     */
    public boolean shouldUpdate(Date lastUpdate) {
        if (lastUpdate == null) {
            return true;
        }
        // be robust against small time differences when using NOW
        return System.currentTimeMillis() - interval >= lastUpdate.getTime();
    }
}
