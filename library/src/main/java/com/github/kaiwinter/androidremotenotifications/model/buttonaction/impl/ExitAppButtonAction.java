package com.github.kaiwinter.androidremotenotifications.model.buttonaction.impl;

import android.content.Context;
import android.content.Intent;

import com.github.kaiwinter.androidremotenotifications.model.buttonaction.ButtonAction;

/**
 * Ends the app.
 */
public final class ExitAppButtonAction implements ButtonAction {

    @Override
    public void execute(Context context) {
        //android.os.Process.killProcess(android.os.Process.myPid());

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // Make sure all of this actions are equal so that no change is detected when updating an existing notification.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 17;
    }
}
