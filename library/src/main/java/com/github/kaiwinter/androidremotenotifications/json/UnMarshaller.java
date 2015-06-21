package com.github.kaiwinter.androidremotenotifications.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.kaiwinter.androidremotenotifications.model.UserNotification;
import com.github.kaiwinter.androidremotenotifications.model.impl.PersistentNotification;

import java.io.IOException;
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
     * @throws IOException
     */
    public static String getJsonFromPersistentNotifications(Set<PersistentNotification> notifications)
            throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        return ow.writeValueAsString(notifications);
    }

    /**
     * Creates a JSON string from a Set of {@link UserNotification}s.
     *
     * @param notifications the notifications
     * @return the JSON string
     * @throws IOException
     */
    public static String getJsonFromNotifications(Set<UserNotification> notifications) throws IOException {

        return new ObjectMapper().writerWithType(new TypeReference<Set<UserNotification>>() {
        }).withDefaultPrettyPrinter().writeValueAsString(notifications);
    }

    /**
     * Creates a Set of {@link UserNotification}s from JSON string which is queried from the given URL.
     *
     * @param url the URL which is parsed as JSON
     * @return Set of {@link UserNotification}s
     * @throws IOException
     */
    public static Set<UserNotification> getNotificationsFromJson(URL url) throws IOException {
        ObjectReader or = new ObjectMapper().reader().withType(new TypeReference<Set<UserNotification>>() {
        });

        return or.readValue(url);
    }

    /**
     * Creates a Set of {@link PersistentNotification}s from a JSON string.
     *
     * @param json the JSON string.
     * @return Set of {@link PersistentNotification}
     * @throws IOException
     */
    public static Set<PersistentNotification> getPersistentNotificationsFromJson(String json) throws IOException {
        ObjectReader or = new ObjectMapper().reader().withType(new TypeReference<Set<PersistentNotification>>() {
        });

        return or.readValue(json);
    }
}
