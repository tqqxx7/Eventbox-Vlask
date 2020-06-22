package com.bp.eventboxtest;

//import org.junit.Assert.assertEquals;

import android.app.Instrumentation;

import org.junit.Test;

import eventbox.LoginActivity;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
public class DemoTest {

    private LoginActivity mLoginActivity;


    private Instrumentation.ActivityMonitor mBrowserActivityMonitor;


    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testViewStart() throws Exception {
        mLoginActivity = (LoginActivity) mBrowserActivityMonitor.waitForActivityWithTimeout(5 * 1000);
        assertEquals("Email Văn Lang", mLoginActivity.btnLoginVlu.getText().toString());


    }
}
