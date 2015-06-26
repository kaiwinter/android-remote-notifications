package com.github.kaiwinter.androidremotenotifications.model.buttonaction;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.kaiwinter.androidremotenotifications.model.buttonaction.impl.ExitAppButtonAction;
import com.github.kaiwinter.androidremotenotifications.model.buttonaction.impl.OpenStoreButtonAction;
import com.github.kaiwinter.androidremotenotifications.model.buttonaction.impl.OpenUrlButtonAction;

/**
 * Provides an action which will be carried out by the selection of a button.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ //
        @JsonSubTypes.Type(value = OpenStoreButtonAction.class),//
        @JsonSubTypes.Type(value = OpenUrlButtonAction.class), //
        @JsonSubTypes.Type(value = ExitAppButtonAction.class) //
})
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
