package com.github.kaiwinter.androidremotenotifications.json;

import com.github.kaiwinter.androidremotenotifications.model.UserNotification;
import com.github.kaiwinter.androidremotenotifications.model.buttonaction.ButtonAction;
import com.github.kaiwinter.androidremotenotifications.model.buttonaction.impl.ExitAppButtonAction;
import com.github.kaiwinter.androidremotenotifications.model.buttonaction.impl.OpenStoreButtonAction;
import com.github.kaiwinter.androidremotenotifications.model.buttonaction.impl.OpenUrlButtonAction;
import com.github.kaiwinter.androidremotenotifications.model.impl.AlertDialogNotification;
import com.github.kaiwinter.androidremotenotifications.model.impl.PersistentNotification;
import com.github.kaiwinter.androidremotenotifications.model.impl.ToastNotification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Set;

/**
 * Utility class for marshalling and unmarshalling JSON and Notification objects.
 */
public final class UnMarshaller {

    /**
     * Creates a JSON string from a Set of {@link PersistentNotification}s.
     *
     * @param notifications the notifications
     * @return the JSON string
     */
    public static String getJsonFromPersistentNotifications(Set<PersistentNotification> notifications) {
        Type listType = new TypeToken<Set<PersistentNotification>>() {
        }.getType();
        return createGson().toJson(notifications, listType);
    }

    /**
     * Creates a JSON string from a Set of {@link UserNotification}s.
     *
     * @param notifications the notifications
     * @return the JSON string
     */
    public static String getJsonFromNotifications(Set<UserNotification> notifications) {
        Type listType = new TypeToken<Set<UserNotification>>() {
        }.getType();
        return createGson().toJson(notifications, listType);
    }

    /**
     * Creates a Set of {@link UserNotification}s from JSON string which is queried from the given URL.
     *
     * @param url the URL which is parsed as JSON
     * @return Set of {@link UserNotification}s
     * @throws IOException if reading the JSON from the URL fails
     */
    public static Set<UserNotification> getNotificationsFromJson(URL url) throws IOException {

        Reader reader = null;
        try {
            reader = new InputStreamReader(url.openStream());

            Type listType = new TypeToken<Set<UserNotification>>() {
            }.getType();
            return createGson().fromJson(reader, listType);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Creates a Set of {@link PersistentNotification}s from a JSON string.
     *
     * @param json the JSON string.
     * @return Set of {@link PersistentNotification}
     */
    public static Set<PersistentNotification> getPersistentNotificationsFromJson(String json) {
        Type listType = new TypeToken<Set<PersistentNotification>>() {
        }.getType();
        return createGson().fromJson(json, listType);
    }

    private static Gson createGson() {
        RuntimeTypeAdapterFactory<UserNotification> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(UserNotification.class, "type")
                .registerSubtype(AlertDialogNotification.class, "AlertDialogNotification")
                .registerSubtype(ToastNotification.class, "ToastNotification");

        RuntimeTypeAdapterFactory<ButtonAction> runtimeTypeAdapterButtonFactory = RuntimeTypeAdapterFactory
                .of(ButtonAction.class, "type")
                .registerSubtype(OpenUrlButtonAction.class, "OpenUrlButtonAction")
                .registerSubtype(ExitAppButtonAction.class, "ExitAppButtonAction")
                .registerSubtype(OpenStoreButtonAction.class, "OpenStoreButtonAction");

        return new GsonBuilder()
                .registerTypeAdapterFactory(runtimeTypeAdapterFactory)
                .registerTypeAdapterFactory(runtimeTypeAdapterButtonFactory)
                .create();
    }
}
