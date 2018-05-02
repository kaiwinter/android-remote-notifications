package com.github.kaiwinter.androidremotenotifications.model.buttonaction.impl;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.github.kaiwinter.androidremotenotifications.model.buttonaction.ButtonAction;

/**
 * Opens an available store app. Currently the following are supported:
 * <ul>
 * <li>Google Play</li>
 * <li>Google Play (Web)</li>
 * </ul>
 * If none of those can be opened as a fallback the web address of Google Play is opened.
 */
public final class OpenStoreButtonAction implements ButtonAction {


    private static final String MARKET_LINK_GOOGLE = "market://details?id=";
    private static final String MARKET_LINK_GOOGLE_WEB = "http://play.google.com/store/apps/details?id=";

    private final String packageName;

    /**
     * @param packageName The package name which should be opened in the Store.
     */
    // @JsonCreator
    public OpenStoreButtonAction(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public void execute(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri.parse(MARKET_LINK_GOOGLE + packageName));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            intent.setData(Uri.parse(MARKET_LINK_GOOGLE_WEB + packageName));
            context.startActivity(intent);
        }
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public String toString() {
        return "OpenStoreButtonAction [packageName=" + packageName + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
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
        OpenStoreButtonAction other = (OpenStoreButtonAction) obj;
        if (packageName == null) {
            if (other.packageName != null)
                return false;
        } else if (!packageName.equals(other.packageName))
            return false;
        return true;
    }
}
