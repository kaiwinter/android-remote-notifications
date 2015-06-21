package com.github.kaiwinter.androidremotenotifications.model.impl;

import java.util.Collection;

/**
 * Defines the app version codes on which a notification should be shown. If the current app install doesn't match this
 * {@link VersionCodePolicy} the notification will not be shown.
 * <p/>
 * There are three ways to define on which versions of your app the notification should be shown: onAllBefore,
 * onAllAfter and onSpecific. You can also mix them but have to keep them sane. Only one of the previous named
 * parameters have to match to let the notification show. They are evaluated in the previous named order.
 */
public final class VersionCodePolicy {

    /**
     * The notification will be shown on all versions < <code>onAllBefore</code>.
     */
    private Integer onAllBefore;

    /**
     * The notification will be shown on all versions > <code>onAllAfter</code>.
     */
    private Integer onAllAfter;

    /**
     * The notification will be shown on all versions which are in <code>onSpecific</code>.
     */
    private Collection<Integer> onSpecific;

    /**
     * Checks if one of <code>onAllAfter</code>, <code>onAllBefore</code> or <code>onSpecific</code> matches the given
     * <code>versionCode</code>. If one matches <code>true</code> is returned, else <code>false</code>.
     *
     * @param appVersionCode The version code to match against this {@link VersionCodePolicy}.
     * @return <code>true</code> if the notification should be shown, else <code>false</code>.
     */
    public boolean hasBeShownForThisVersion(int appVersionCode) {

        if (onAllAfter != null) {
            if (appVersionCode > onAllAfter) {
                return true;
            }
        }

        if (onAllBefore != null) {
            if (appVersionCode < onAllBefore) {
                return true;
            }
        }

        if (onSpecific != null) {
            if (onSpecific.contains(appVersionCode)) {
                return true;
            }
        }
        return false;
    }

    public Integer getOnAllBefore() {
        return onAllBefore;
    }

    public void setOnAllBefore(Integer onAllBefore) {
        this.onAllBefore = onAllBefore;
    }

    public Integer getOnAllAfter() {
        return onAllAfter;
    }

    public void setOnAllAfter(Integer onAllAfter) {
        this.onAllAfter = onAllAfter;
    }

    public Collection<Integer> getOnSpecific() {
        return onSpecific;
    }

    public void setOnSpecific(Collection<Integer> onSpecific) {
        this.onSpecific = onSpecific;
    }
}
