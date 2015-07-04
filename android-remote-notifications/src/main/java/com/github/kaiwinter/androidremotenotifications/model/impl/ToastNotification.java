package com.github.kaiwinter.androidremotenotifications.model.impl;

import android.content.Context;
import android.widget.Toast;

/**
 * A Notification which is shown as a {@link Toast}. Set a message and a duration of {@link Toast#LENGTH_LONG} or {@link Toast#LENGTH_LONG}.
 */
public final class ToastNotification extends AbstractUserNotification {

    /**
     * The message in the notification.
     */
    private String message;

    /**
     * Either {@link Toast#LENGTH_SHORT} or {@link Toast#LENGTH_LONG}.
     */
    private int duration;

    public ToastNotification() {
        super();
    }

    public ToastNotification(NotificationConfiguration notificationConfiguration) {
        super(notificationConfiguration);
    }

    @Override
    public void show(final Context context) {
        Toast.makeText(context, message, duration).show();
    }

    /**
     * @return the Toast message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the Toast message.
     *
     * @param message the Toast message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the Toast duration (either {@link Toast#LENGTH_SHORT} or {@link Toast#LENGTH_LONG}
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the Toast duration (either {@link Toast#LENGTH_SHORT} (0) or {@link Toast#LENGTH_LONG} (1).
     *
     * @param duration the Toast duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "ToastNotification{" +
                "message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ToastNotification that = (ToastNotification) o;

        if (duration != that.duration) return false;
        return !(message != null ? !message.equals(that.message) : that.message != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + duration;
        return result;
    }
}
