package com.github.kaiwinter.androidremotenotifications.model;

import com.github.kaiwinter.androidremotenotifications.model.impl.AlertDialogNotification;
import com.github.kaiwinter.androidremotenotifications.model.impl.NotificationConfiguration;
import com.github.kaiwinter.androidremotenotifications.model.impl.PersistentNotification;
import com.github.kaiwinter.androidremotenotifications.model.impl.VersionCodePolicy;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests if the time to show a notification to the user is calculated correctly.
 */
public final class PersistentNotificationTest {

    @Test
    public void testAlwaysExecutionSameDay() {
        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration.setExecutionPolicy(ExecutionPolicy.ALWAYS);
        AlertDialogNotification notification = new AlertDialogNotification(notificationConfiguration);
        PersistentNotification persistentNotification = new PersistentNotification(notification);
        persistentNotification.setLastShown(new Date());

        assertTrue(persistentNotification.hasToBeShown(1));
    }

    @Test
    public void testDailyExecutionSameDay() {

        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration.setExecutionPolicy(ExecutionPolicy.EVERY_DAY);
        AlertDialogNotification notification = new AlertDialogNotification(notificationConfiguration);

        PersistentNotification persistentNotification = new PersistentNotification(notification);
        persistentNotification.setLastShown(new Date());

        assertFalse(persistentNotification.hasToBeShown(1));
    }

    @Test
    public void testDailyExecutionNextDay() {
        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration.setExecutionPolicy(ExecutionPolicy.ALWAYS);
        AlertDialogNotification notification = new AlertDialogNotification(notificationConfiguration);

        PersistentNotification persistentNotification = new PersistentNotification(notification);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1); // one day before
        date = calendar.getTime();

        persistentNotification.setLastShown(date);

        assertTrue(persistentNotification.hasToBeShown(1));
    }

    @Test
    public void testMonthlyExecutionSameMonth() {
        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration.setExecutionPolicy(ExecutionPolicy.EVERY_FIRST_OF_MONTH);
        AlertDialogNotification notification = new AlertDialogNotification(notificationConfiguration);

        PersistentNotification persistentNotification = new PersistentNotification(notification);
        persistentNotification.setLastShown(new Date());

        assertFalse(persistentNotification.hasToBeShown(1));
    }

    @Test
    public void testMonthlyExecutionNextMonth() {
        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration.setExecutionPolicy(ExecutionPolicy.EVERY_FIRST_OF_MONTH);
        AlertDialogNotification notification = new AlertDialogNotification(notificationConfiguration);

        PersistentNotification persistentNotification = new PersistentNotification(notification);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        date = calendar.getTime();

        persistentNotification.setLastShown(date);

        assertTrue(persistentNotification.hasToBeShown(1));
    }

    @Test
    public void testMondaylyExecutionSameDay() {
        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration.setExecutionPolicy(ExecutionPolicy.EVERY_MONDAY);
        AlertDialogNotification notification = new AlertDialogNotification(notificationConfiguration);

        PersistentNotification persistentNotification = new PersistentNotification(notification);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK));
        date = calendar.getTime();

        persistentNotification.setLastShown(date);

        assertFalse(persistentNotification.hasToBeShown(1));
    }

    /**
     * Tests if {@link NotificationConfiguration#startShowingDate} is evaluated. Even if {@link ExecutionPolicy#ALWAYS} is used the
     * notification should be shown in a month for the first time. The notification should not be shown by this test
     * even if it hasn't a 'last shown date'.
     */
    @Test
    public void testHasToBeShownAfterStartDate() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, +1); // Next month
        date = calendar.getTime();

        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration.setExecutionPolicy(ExecutionPolicy.ALWAYS);
        notificationConfiguration.setStartShowingDate(date);
        AlertDialogNotification notification = new AlertDialogNotification(notificationConfiguration);

        PersistentNotification persistentNotification = new PersistentNotification(notification);

        assertFalse(persistentNotification.hasToBeShown(1));
    }

    /**
     * Tests if the notification gets shown if the last shown date is null.
     */
    @Test
    public void testHasToBeShownFirstTime() {
        PersistentNotification persistentNotification = new PersistentNotification(new AlertDialogNotification());

        boolean hasToBeShown = persistentNotification.hasToBeShown(1);
        assertTrue(hasToBeShown);
    }

    /**
     * Tests if the notification gets shown if the maximum number of view was not reached.
     */
    @Test
    public void testHasToBeShownMaxNumberNotReached() {
        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration.setExecutionPolicy(ExecutionPolicy.ALWAYS);
        notificationConfiguration.setNumberOfTotalViews(5);

        AlertDialogNotification notification = new AlertDialogNotification(notificationConfiguration);
        PersistentNotification persistentNotification = new PersistentNotification(notification);
        persistentNotification.setShownCounter(3);

        boolean hasToBeShown = persistentNotification.hasToBeShown(1);
        assertTrue(hasToBeShown);
    }

    /**
     * Tests if the notification not gets shown if the maximum number of view was reached.
     */
    @Test
    public void testHasToBeShownMaxNumberReached() {
        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration.setExecutionPolicy(ExecutionPolicy.ALWAYS);
        notificationConfiguration.setNumberOfTotalViews(5);
        AlertDialogNotification notification = new AlertDialogNotification(notificationConfiguration);
        PersistentNotification persistentNotification = new PersistentNotification(notification);
        persistentNotification.setShownCounter(5);

        boolean hasToBeShown = persistentNotification.hasToBeShown(1);
        assertFalse(hasToBeShown);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPassed() {
        new PersistentNotification(null);
    }

    /**
     * The VersionCodePolicy is null and should not be considered.
     */
    @Test
    public void testVersionCodePolicyNull() {
        AlertDialogNotification notification = new AlertDialogNotification();
        PersistentNotification persistentNotification = new PersistentNotification(notification);

        boolean hasToBeShown = persistentNotification.hasToBeShown(1);
        assertTrue(hasToBeShown);
    }

    /**
     * The VersionCodePolicy is set and have to be evaluated (results in not showing the notification).
     */
    @Test
    public void testVersionCodePolicyNotNullShow() {
        VersionCodePolicy versionCodePolicy = new VersionCodePolicy();
        versionCodePolicy.setOnAllAfter(10);

        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration.setExecutionPolicy(ExecutionPolicy.ALWAYS);
        notificationConfiguration.setVersionCodePolicy(versionCodePolicy);
        AlertDialogNotification notification = new AlertDialogNotification(notificationConfiguration);

        PersistentNotification persistentNotification = new PersistentNotification(notification);

        int versionCode = 1;
        boolean hasToBeShown = persistentNotification.hasToBeShown(versionCode);
        assertFalse(hasToBeShown);
    }

    /**
     * The VersionCodePolicy is set and have to be evaluated (results in showing the notification).
     */
    @Test
    public void testVersionCodePolicyNotNullNoShow() {
        VersionCodePolicy versionCodePolicy = new VersionCodePolicy();
        versionCodePolicy.setOnAllAfter(10);

        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration.setExecutionPolicy(ExecutionPolicy.ALWAYS);
        notificationConfiguration.setVersionCodePolicy(versionCodePolicy);

        AlertDialogNotification notification = new AlertDialogNotification(notificationConfiguration);

        PersistentNotification persistentNotification = new PersistentNotification(notification);

        int versionCode = 11;
        boolean hasToBeShown = persistentNotification.hasToBeShown(versionCode);
        assertTrue(hasToBeShown);
    }

    /**
     * If no ExecutionPolicy is set then the Notification should always be shown.
     */
    @Test
    public void testVersionExecutionPolicyNull() {
        AlertDialogNotification notification = new AlertDialogNotification();
        PersistentNotification persistentNotification = new PersistentNotification(notification);
        persistentNotification.setLastShown(new Date());

        boolean hasToBeShown = persistentNotification.hasToBeShown(1);
        assertTrue(hasToBeShown);
    }
}
