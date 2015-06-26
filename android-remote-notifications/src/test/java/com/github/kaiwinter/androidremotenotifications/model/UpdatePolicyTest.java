package com.github.kaiwinter.androidremotenotifications.model;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests if the execution time for a notification update from the server is correctly calculated.
 */
public final class UpdatePolicyTest {

    private static final long DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;

    /**
     * Tests if an update is made if no previous update ran.
     */
    @Test
    public void testFirstTime() {
        boolean shouldUpdate = UpdatePolicy.NOW.shouldUpdate(null);
        assertTrue(shouldUpdate);
    }

    @Test
    public void testNow() {
        boolean shouldUpdate = UpdatePolicy.NOW.shouldUpdate(new Date());
        assertTrue(shouldUpdate);
    }

    /**
     * Tests if a weekly update is recognized if 8 days have passed.
     */
    @Test
    public void testWeeklyUpdate() {
        Date date = new Date(System.currentTimeMillis() - (8 * DAY_IN_MILLISECONDS));
        boolean shouldUpdate = UpdatePolicy.WEEKLY.shouldUpdate(date);
        assertTrue(shouldUpdate);
    }

    /**
     * Tests if no weekly update is recognized if 5 days have passed.
     */
    @Test
    public void testWeeklyNoUpdate() {
        Date date = new Date(System.currentTimeMillis() - (5 * DAY_IN_MILLISECONDS));
        boolean shouldUpdate = UpdatePolicy.WEEKLY.shouldUpdate(date);
        assertFalse(shouldUpdate);
    }

    /**
     * Tests if a monthly update is recognized if 32 days have passed.
     */
    @Test
    public void testMonthlyUpdate() {
        Date date = new Date(System.currentTimeMillis() - (32 * DAY_IN_MILLISECONDS));
        boolean shouldUpdate = UpdatePolicy.MONTHLY.shouldUpdate(date);
        assertTrue(shouldUpdate);
    }

    /**
     * Tests if no monthly update is recognized if 15 days have passed.
     */
    @Test
    public void testMonthlyNoUpdate() {
        Date date = new Date(System.currentTimeMillis() - (15 * DAY_IN_MILLISECONDS));
        boolean shouldUpdate = UpdatePolicy.MONTHLY.shouldUpdate(date);
        assertFalse(shouldUpdate);
    }
}
