package com.github.kaiwinter.androidremotenotifications.persistence;

import com.github.kaiwinter.androidremotenotifications.model.impl.PersistentNotification;

import java.util.Date;
import java.util.Set;

/**
 * Interface for persisting notifications and configurations.
 */
public interface NotificationStore {

    /**
     * Loads the date when the notifications was updated from the server the last time.
     *
     * @return The last update date.
     */
    Date getLastServerUpdate();

    /**
     * Saves the date when the notifications was updated from the server the last time.
     *
     * @param date The last update date.
     */
    void saveLastServerUpdate(Date date);

    /**
     * Loads all saved {@link PersistentNotification}s.
     *
     * @return The saved {@link PersistentNotification}s
     */
    Set<PersistentNotification> getPersistentNotifications();

    /**
     * Updates the {@link PersistentNotification} in the {@link NotificationStore} with the given
     * <code>updatePersistentNotifications</code>. The notifications to update are identified by their hashCode/equals
     * methods.
     *
     * @param updatePersistentNotifications The {@link PersistentNotification} to update.
     */
    void updatePersistentNotification(Set<PersistentNotification> updatePersistentNotifications);

    /**
     * Replaces the currently stored {@link PersistentNotification}s by the passed ones.
     *
     * @param persistentNotifications The {@link PersistentNotification}s to store.
     */
    void replacePersistentNotifications(Set<PersistentNotification> persistentNotifications);

    /**
     * Removes all notifications from the store.
     */
    void clearPersistentNotifications();
}