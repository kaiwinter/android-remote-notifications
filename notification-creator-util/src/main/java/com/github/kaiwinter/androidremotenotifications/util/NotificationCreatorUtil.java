package com.github.kaiwinter.androidremotenotifications.util;

import com.github.kaiwinter.androidremotenotifications.json.UnMarshaller;
import com.github.kaiwinter.androidremotenotifications.model.ExecutionPolicy;
import com.github.kaiwinter.androidremotenotifications.model.UserNotification;
import com.github.kaiwinter.androidremotenotifications.model.buttonaction.impl.ExitAppButtonAction;
import com.github.kaiwinter.androidremotenotifications.model.buttonaction.impl.OpenStoreButtonAction;
import com.github.kaiwinter.androidremotenotifications.model.buttonaction.impl.OpenUrlButtonAction;
import com.github.kaiwinter.androidremotenotifications.model.impl.AlertDialogNotification;
import com.github.kaiwinter.androidremotenotifications.model.impl.NotificationConfiguration;
import com.github.kaiwinter.androidremotenotifications.model.impl.ToastNotification;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class with a main method to create a JSON string of notifications which you can put on your server.
 */
public final class NotificationCreatorUtil {

    public static void main(String[] args) throws IOException {

        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration.setExecutionPolicy(ExecutionPolicy.ALWAYS);

        // Create AlertDialog notification
        AlertDialogNotification notification = new AlertDialogNotification(notificationConfiguration);
        notification.setTitle("Title");
        notification.setMessage("This is an AlertDialog notification");

        notification.setNegativeButtonText("Exit App");
        notification.setNegativeButtonAction(new ExitAppButtonAction());

        notification.setNeutralButtonText("Open web page");
        notification.setNeutralButtonAction(new OpenUrlButtonAction("http://www.github.com/kaiwinter"));

        notification.setPositiveButtonText("Open Play Store");
        notification.setPositiveButtonAction(new OpenStoreButtonAction("de.vorlesungsfrei.taekwondo.ads"));

        // Create Toast Notification
        ToastNotification toastNotification = new ToastNotification(notificationConfiguration);
        toastNotification.setDuration(1);
        toastNotification.setMessage("This is a Toast Notification");

        // Print JSON to console
        Set<UserNotification> list = new HashSet<>();
        list.add(notification);
        list.add(toastNotification);
        printToConsole(list);
    }

    private static void printToConsole(Set<UserNotification> set) throws IOException {
        String json = UnMarshaller.getJsonFromNotifications(set);
        System.out.println(json);
    }
}