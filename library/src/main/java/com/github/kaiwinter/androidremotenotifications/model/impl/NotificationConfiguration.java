package com.github.kaiwinter.androidremotenotifications.model.impl;

import com.github.kaiwinter.androidremotenotifications.model.ExecutionPolicy;

import java.util.Date;

/**
 * Stores configurations for a Notification e.g. when and how often it should be shown and on which app versions.
 */
public final class NotificationConfiguration {

    /**
     * From this date on the notification will be shown. If <code>null</code> notification will be shown immediately (if
     * {@link ExecutionPolicy} strikes).
     */
    private Date startShowingDate;

    /**
     * Defines when to show the notification. If <code>null</code> the notification will be shown
     * {@link ExecutionPolicy#ALWAYS}.
     * <p/>
     * This will be evaluated only after {@link #startShowingDate} (if not <code>null</code>).
     */
    private ExecutionPolicy executionPolicy;

    /**
     * Defines how many times the notification is shown in regards to executionPolicy (null=infinite).
     */
    private Integer numberOfTotalViews;

    /**
     * The app version on which the notification should be shown. If <code>null</code> notification will be shown on all
     * versions.
     */
    private VersionCodePolicy versionCodePolicy;

    public Date getStartShowingDate() {
        return startShowingDate;
    }

    public void setStartShowingDate(Date startShowingDate) {
        this.startShowingDate = startShowingDate;
    }

    public ExecutionPolicy getExecutionPolicy() {
        return executionPolicy;
    }

    public void setExecutionPolicy(ExecutionPolicy executionPolicy) {
        this.executionPolicy = executionPolicy;
    }

    public Integer getNumberOfTotalViews() {
        return numberOfTotalViews;
    }

    public void setNumberOfTotalViews(Integer numberOfTotalViews) {
        this.numberOfTotalViews = numberOfTotalViews;
    }

    public VersionCodePolicy getVersionCodePolicy() {
        return versionCodePolicy;
    }

    public void setVersionCodePolicy(VersionCodePolicy versionCodePolicy) {
        this.versionCodePolicy = versionCodePolicy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationConfiguration that = (NotificationConfiguration) o;

        if (startShowingDate != null ? !startShowingDate.equals(that.startShowingDate) : that.startShowingDate != null)
            return false;
        if (executionPolicy != that.executionPolicy) return false;
        if (numberOfTotalViews != null ? !numberOfTotalViews.equals(that.numberOfTotalViews) : that.numberOfTotalViews != null)
            return false;
        return !(versionCodePolicy != null ? !versionCodePolicy.equals(that.versionCodePolicy) : that.versionCodePolicy != null);

    }

    @Override
    public int hashCode() {
        int result = startShowingDate != null ? startShowingDate.hashCode() : 0;
        result = 31 * result + (executionPolicy != null ? executionPolicy.hashCode() : 0);
        result = 31 * result + (numberOfTotalViews != null ? numberOfTotalViews.hashCode() : 0);
        result = 31 * result + (versionCodePolicy != null ? versionCodePolicy.hashCode() : 0);
        return result;
    }
}
