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
public class RemoteNotificationsTest {

    @BeforeClass
    public static void setUp() throws Exception {
        ShadowLog.stream = System.out;
    }

    /**
     * Tests if an IllegalArgumentException is thrown if the server URL is invalid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMissingServerFile() {
        URL serverUrl = RemoteNotificationsTest.class.getResource("/not-existing.json");
        RemoteNotifications remoteNotifications = new RemoteNotifications(RuntimeEnvironment.application.getApplicationContext(), serverUrl);
        remoteNotifications.updateNotificationsFromServer(UpdatePolicy.NOW);
    }

    /**
     * Loads the notification file and checks if the notification gets added to the shared preferences.
     */
    @Test
    public void testInitialLoad() {
        URL serverUrl = RemoteNotificationsTest.class.getResource("/com/github/kaiwinter/androidremotenotifications/one_notification.json");
        RemoteNotifications remoteNotifications = new RemoteNotifications(RuntimeEnvironment.application.getApplicationContext(), serverUrl);

        Set<PersistentNotification> persistentNotifications = remoteNotifications.getPreferenceStore().getPersistentNotifications();
        assertEquals(0, persistentNotifications.size());

        remoteNotifications.updateNotificationsFromServer(UpdatePolicy.NOW);
        persistentNotifications = remoteNotifications.getPreferenceStore().getPersistentNotifications();
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

        URL serverUrl = RemoteNotificationsTest.class.getResource("/com/github/kaiwinter/androidremotenotifications/empty_notification.json");
        RemoteNotifications remoteNotifications = new RemoteNotifications(RuntimeEnvironment.application.getApplicationContext(), serverUrl);
        NotificationStore preferenceStore = remoteNotifications.getPreferenceStore();
        preferenceStore.replacePersistentNotifications(persistentNotifications);

        // initially filled with one
        persistentNotifications = remoteNotifications.getPreferenceStore().getPersistentNotifications();
        assertEquals(1, persistentNotifications.size());

        // update from server, notification should be removed
        remoteNotifications.updateNotificationsFromServer(UpdatePolicy.NOW);

        persistentNotifications = remoteNotifications.getPreferenceStore().getPersistentNotifications();
        assertEquals(0, persistentNotifications.size());
    }

    @Test
    public void testShow() {
        Set<PersistentNotification> persistentNotifications = new HashSet<>();
        PersistentNotification persistentNotification = new PersistentNotification(new AlertDialogNotification());
        assertEquals(0, persistentNotification.getShownCounter());
        persistentNotifications.add(persistentNotification);

        URL serverUrl = RemoteNotificationsTest.class.getResource("/com/github/kaiwinter/androidremotenotifications/empty_notification.json");
        RemoteNotifications remoteNotifications = new RemoteNotifications(RuntimeEnvironment.application.getApplicationContext(), serverUrl);
        NotificationStore preferenceStore = remoteNotifications.getPreferenceStore();
        preferenceStore.replacePersistentNotifications(persistentNotifications);

        remoteNotifications.showPendingNotificationsToUser(true);

        // Check if show counter is raised
        assertEquals(1, preferenceStore.getPersistentNotifications().iterator().next().getShownCounter());
    }
}
