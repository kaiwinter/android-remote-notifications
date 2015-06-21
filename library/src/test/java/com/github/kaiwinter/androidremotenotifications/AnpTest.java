package com.github.kaiwinter.androidremotenotifications;

import com.github.kaiwinter.androidremotenotifications.model.UpdatePolicy;
import com.github.kaiwinter.androidremotenotifications.model.impl.AlertDialogNotification;
import com.github.kaiwinter.androidremotenotifications.model.impl.PersistentNotification;
import com.github.kaiwinter.androidremotenotifications.persistence.NotificationStore;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowLog;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class AnpTest {

    @BeforeClass
    public static void setUp() throws Exception {
        ShadowLog.stream = System.out;
    }

    /**
     * Tests if an IllegalArgumentException is thrown if the server URL is invalid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMissingServerFile() {
        URL serverUrl = AnpTest.class.getResource("/not-existing.json");
        Anp anp = new Anp(serverUrl, RuntimeEnvironment.application.getApplicationContext());
        anp.updateNotificationsFromServer(UpdatePolicy.NOW);
    }

    /**
     * Loads the notification file and checks if the notification gets added to the shared preferences.
     */
    @Test
    public void testInitialLoad() {
        URL serverUrl = AnpTest.class.getResource("/com/github/kaiwinter/androidremotenotifications/one_notification.json");
        Anp anp = new Anp(serverUrl, RuntimeEnvironment.application.getApplicationContext());

        Set<PersistentNotification> persistentNotifications = anp.getPreferenceStore().getPersistentNotifications();
        assertEquals(0, persistentNotifications.size());

        anp.updateNotificationsFromServer(UpdatePolicy.NOW);
        persistentNotifications = anp.getPreferenceStore().getPersistentNotifications();
        assertEquals(1, persistentNotifications.size());
    }

    /**
     * Tests if an existing notification gets removed. The shared preferences contain one notification. Then an update
     * from the server is triggered which returns 0 notifications. This should remove the existing one.
     */
    @Test
    public void testRemove() {
        PersistentNotification notification = new PersistentNotification(new AlertDialogNotification());
        Set<PersistentNotification> persistentNotifications = new HashSet<>();
        persistentNotifications.add(notification);

        URL serverUrl = AnpTest.class.getResource("/com/github/kaiwinter/androidremotenotifications/empty_notification.json");
        Anp anp = new Anp(serverUrl, RuntimeEnvironment.application.getApplicationContext());
        NotificationStore preferenceStore = anp.getPreferenceStore();
        preferenceStore.replacePersistentNotifications(persistentNotifications);

        // initially filled with one
        persistentNotifications = anp.getPreferenceStore().getPersistentNotifications();
        assertEquals(1, persistentNotifications.size());

        // update from server, notification should be removed
        anp.updateNotificationsFromServer(UpdatePolicy.NOW);

        persistentNotifications = anp.getPreferenceStore().getPersistentNotifications();
        assertEquals(0, persistentNotifications.size());
    }

    @Test
    public void testShow() {
        Set<PersistentNotification> persistentNotifications = new HashSet<>();
        PersistentNotification persistentNotification = new PersistentNotification(new AlertDialogNotification());
        assertEquals(0, persistentNotification.getShownCounter());
        persistentNotifications.add(persistentNotification);

        URL serverUrl = AnpTest.class.getResource("/com/github/kaiwinter/androidremotenotifications/empty_notification.json");
        Anp anp = new Anp(serverUrl, RuntimeEnvironment.application.getApplicationContext());
        NotificationStore preferenceStore = anp.getPreferenceStore();
        preferenceStore.replacePersistentNotifications(persistentNotifications);

        anp.showPendingNotificationsToUser(true);

        // Check if show counter is raised
        assertEquals(1, preferenceStore.getPersistentNotifications().iterator().next().getShownCounter());
    }
}
