//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.Robolectric;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.annotation.Config;
//
//import eventbox.BuildConfig;
//import eventbox.LoginActivity;
//import eventbox.MainActivity;
//
//import static org.junit.Assert.assertThat;
//
//@RunWith(RobolectricTestRunner.class)
//@Config(constants = BuildConfig.class)
//public class LoginTest {
//    private LoginActivity mLoginActivity;
//
//    @Before
//    public void setUp() throws Exception {
//        mLoginActivity = Robolectric.buildActivity(LoginActivity.class).create().get();
//    }
//
//    @Test
//    public void testViewStart() throws Exception {
//        assertThat(mLoginActivity.btn.getText().toString(), equalTo("Cộng"));
//        assertThat(mLoginActivity.mTextViewResult.getText().toString(), equalTo("Kết quả"));
//    }
//
//    @Test
//    public void testClicking() throws Exception {
//        // Test thử chức năng 1 + 1 = 2;
//        mLoginActivity.mButtonSum.performClick();
//        assertThat(mLoginActivity.mTextViewResult.getText().toString(), equalTo("2"));
//    }
//}