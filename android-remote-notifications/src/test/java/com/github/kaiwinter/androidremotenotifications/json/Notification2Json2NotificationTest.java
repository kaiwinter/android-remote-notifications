package com.github.kaiwinter.androidremotenotifications.json;

import com.github.kaiwinter.androidremotenotifications.model.ExecutionPolicy;
import com.github.kaiwinter.androidremotenotifications.model.UserNotification;
import com.github.kaiwinter.androidremotenotifications.model.buttonaction.impl.OpenStoreButtonAction;
import com.github.kaiwinter.androidremotenotifications.model.buttonaction.impl.OpenUrlButtonAction;
import com.github.kaiwinter.androidremotenotifications.model.impl.AlertDialogNotification;
import com.github.kaiwinter.androidremotenotifications.model.impl.NotificationConfiguration;
import com.github.kaiwinter.androidremotenotifications.model.impl.PersistentNotification;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public final class Notification2Json2NotificationTest {

    @Test
    public void testNotificationFromURL() throws IOException {
        URL url = Notification2Json2NotificationTest.class.getResource("/com/github/kaiwinter/androidremotenotifications/one_notification.json");
        Set<UserNotification> notificationsFromJson = UnMarshaller.getNotificationsFromJson(url);
        assertEquals(1, notificationsFromJson.size());
    }

    @Test
    public void testPersistentNotification() throws IOException {
        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration.setExecutionPolicy(ExecutionPolicy.ALWAYS);
        notificationConfiguration.setExecutionPolicy(ExecutionPolicy.EVERY_MONDAY);
        notificationConfiguration.setNumberOfTotalViews(5);

        AlertDialogNotification notification = new AlertDialogNotification(notificationConfiguration);
        notification.setTitle("the title");
        notification.setMessage("the message");
        notification.setPositiveButtonText("positive");
        notification.setNegativeButtonText("negative");
        notification.setPositiveButtonAction(new OpenStoreButtonAction("my.package"));
        notification.setNegativeButtonAction(new OpenUrlButtonAction("http://www"));

        PersistentNotification persistentNotification = new PersistentNotification(notification);
        String json = UnMarshaller.getJsonFromPersistentNotifications(Collections
                .singleton(persistentNotification));

        PersistentNotification persistentNotificationsFromJson = UnMarshaller
                .getPersistentNotificationsFromJson(json).iterator().next();

        AlertDialogNotification alertDialogNotification = (AlertDialogNotification) persistentNotification.getNotification();
        AlertDialogNotification alertDialogNotificationJson = (AlertDialogNotification) persistentNotificationsFromJson.getNotification();
        assertEquals(alertDialogNotification.getMessage(), alertDialogNotificationJson.getMessage());
    }
}
