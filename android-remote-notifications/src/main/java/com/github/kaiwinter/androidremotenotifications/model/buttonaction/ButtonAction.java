package com.github.kaiwinter.androidremotenotifications.model.buttonaction;

import android.content.Context;

/**
 * Provides an action which will be carried out by the selection of a button.
 */
public interface ButtonAction {

    /**
     * Executes the action of this button.
     *
     * @param context the application context
     */
    void execute(Context context);

    @Override
    int hashCode();

    @Override
    boolean equals(Object obj);
}
