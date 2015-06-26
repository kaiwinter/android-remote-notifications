package com.github.kaiwinter.androidremotenotifications.model.buttonaction.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.kaiwinter.androidremotenotifications.model.buttonaction.ButtonAction;

import static com.github.kaiwinter.androidremotenotifications.RemoteNotifications.TAG;

/**
 * Opens the link with the platform browser.
 */
public final class OpenUrlButtonAction implements ButtonAction {
    private final String link;

    /**
     * @param link the link to open.
     */
    public OpenUrlButtonAction(@JsonProperty("link") String link) {
        this.link = link;
    }

    @Override
    public void execute(Context context) {
        Log.v(TAG, "Opening URL: " + link);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        context.startActivity(browserIntent);
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "OpenURLButtonAction [link=" + link + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((link == null) ? 0 : link.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OpenUrlButtonAction other = (OpenUrlButtonAction) obj;
        if (link == null) {
            if (other.link != null)
                return false;
        } else if (!link.equals(other.link))
            return false;
        return true;
    }

}
