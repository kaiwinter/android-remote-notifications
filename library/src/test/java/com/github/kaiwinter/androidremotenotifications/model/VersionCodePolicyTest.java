package com.github.kaiwinter.androidremotenotifications.model;

import com.github.kaiwinter.androidremotenotifications.model.impl.VersionCodePolicy;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class VersionCodePolicyTest {

    /**
     * The {@link VersionCodePolicy} has no versions set so the notification should not be shown as no one matches.
     */
    @Test
    public void testNoRestriction() {
        VersionCodePolicy versionCodePolicy = new VersionCodePolicy();
        boolean shouldBeShownForThisVersion = versionCodePolicy.hasBeShownForThisVersion(2);
        assertFalse(shouldBeShownForThisVersion);
    }

    @Test
    public void testOnAllBeforeShow() {
        VersionCodePolicy versionCodePolicy = new VersionCodePolicy();
        versionCodePolicy.setOnAllBefore(10);
        boolean shouldBeShownForThisVersion = versionCodePolicy.hasBeShownForThisVersion(5);
        assertTrue(shouldBeShownForThisVersion);
    }

    @Test
    public void testOnAllBeforeNoShow() {
        VersionCodePolicy versionCodePolicy = new VersionCodePolicy();
        versionCodePolicy.setOnAllBefore(10);
        boolean shouldBeShownForThisVersion = versionCodePolicy.hasBeShownForThisVersion(15);
        assertFalse(shouldBeShownForThisVersion);
    }

    @Test
    public void testOnAllAfterShow() {
        VersionCodePolicy versionCodePolicy = new VersionCodePolicy();
        versionCodePolicy.setOnAllAfter(10);
        boolean shouldBeShownForThisVersion = versionCodePolicy.hasBeShownForThisVersion(15);
        assertTrue(shouldBeShownForThisVersion);
    }

    @Test
    public void testOnAllAfterNoShow() {
        VersionCodePolicy versionCodePolicy = new VersionCodePolicy();
        versionCodePolicy.setOnAllAfter(10);
        boolean shouldBeShownForThisVersion = versionCodePolicy.hasBeShownForThisVersion(5);
        assertFalse(shouldBeShownForThisVersion);
    }

    @Test
    public void testOnSpecificShow() {
        VersionCodePolicy versionCodePolicy = new VersionCodePolicy();
        versionCodePolicy.setOnSpecific(Arrays.asList(12, 13, 14, 15));
        boolean shouldBeShownForThisVersion = versionCodePolicy.hasBeShownForThisVersion(15);
        assertTrue(shouldBeShownForThisVersion);
    }

    @Test
    public void testOnSpecificNoShow() {
        VersionCodePolicy versionCodePolicy = new VersionCodePolicy();
        versionCodePolicy.setOnSpecific(Arrays.asList(12, 13, 14, 15));
        boolean shouldBeShownForThisVersion = versionCodePolicy.hasBeShownForThisVersion(11);
        assertFalse(shouldBeShownForThisVersion);
    }
}
