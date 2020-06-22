package com.bp.eventboxtest;

import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import eventbox.LoginActivity;
import eventbox.R;

import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
//@Config(constants = BuildConfig.class , sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class  LoginTest {
    private LoginActivity mLoginActivity;
    private Button mEtUserName;

    @Before
    public void setUp()  {
        mLoginActivity = Robolectric.buildActivity(LoginActivity.class).create().get();
        mEtUserName =  mLoginActivity.findViewById(R.id.ButtonLoginVlu);

    }

    @Test
    public void testViewStart(){
        assertNotNull(mEtUserName);
//        assertThat(mLoginActivity.btnLoginVlu.getText().toString(), equalTo("Email Văn Lang"));
//        assertThat(mLoginActivity.mTextViewResult.getText().toString(), equalTo("Kết quả"));
    }

//    @Test
//    public void testClicking() throws Exception {
//        // Test thử chức năng 1 + 1 = 2;
//        mLoginActivity.mButtonSum.performClick();
//        assertThat(mLoginActivity.mTextViewResult.getText().toString(), equalTo("2"));
//    }
}