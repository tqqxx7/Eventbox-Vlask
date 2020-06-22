package eventbox;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;
import com.microsoft.identity.client.exception.MsalUiRequiredException;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import services.APIUtils;
import services.DataClient;

public class SigninActivity extends AppCompatActivity {
    TextView btnVlu, btnLogin;
    EditText edtMail, edtPass;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initView();
        dialog = new Dialog(SigninActivity.this);
        eventClick();
        /* Configure your sample app and save state for this activity */
        sampleApp = new PublicClientApplication(
                this.getApplicationContext(),
                R.raw.auth_config);

        /* Attempt to get a user and acquireTokenSilent */
        sampleApp.getAccounts(new PublicClientApplication.AccountsLoadedCallback() {
            @Override
            public void onAccountsLoaded(final List<IAccount> accounts) {
                if (!accounts.isEmpty()) {
                    /* This sample doesn't support multi-account scenarios, use the first account */
                    sampleApp.acquireTokenSilentAsync(SCOPES, accounts.get(0), getAuthSilentCallback());
                } else {
                    /* No accounts */
                }
            }
        });

        Intent intentOut = getIntent();
        String out="";
        if(intentOut.hasExtra("out")){
            out = intentOut.getStringExtra("out");
        }

        if(out.isEmpty()){
//            onCallGraphClicked();
        }else{
            if(out.equals("out")){
                out = "";
                sampleApp.acquireToken(getActivity(), SCOPES, getAuthInteractiveCallback());
                onSignOutClicked();
            }
        }
    }

    private void eventClick() {
        btnVlu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCallGraphClicked();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginNormal();
            }
        });


    }

    /* Login by Account Normal (<> Accouct VLU) */
    private void loginNormal() {
            final String email = edtMail.getText().toString();
            String password = edtPass.getText().toString();
            if(!email.isEmpty() && !password.isEmpty()){
                showDialogLoadingLogin(dialog);
                DataClient dataClient = APIUtils.getData();
                Call<String> callback = dataClient.Login(email, password);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        String result = response.body();
                        if(result.equals("success")){
                            SharedPreferences preferences = getSharedPreferences("user_email", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("email", email);
                            editor.commit();
                            Intent intent = new Intent(SigninActivity.this, ListEventActivity.class);
                            intent.putExtra("mail", email);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(SigninActivity.this, R.string.faillogin, Toast.LENGTH_SHORT).show();
                            dissmisDialogLoading(dialog);
                        }


                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("fail", t.getMessage());
                    }
                });
            }else{
                Toast.makeText(SigninActivity.this, R.string.pleasefillout, Toast.LENGTH_SHORT).show();

            }

    }

    private void initView() {

        btnVlu =  findViewById(R.id.ButtonLoginVlu);
        btnLogin = findViewById(R.id.ButtonLoginNormal);
        edtMail = findViewById(R.id.tv_email_login);
        edtPass = findViewById(R.id.tv_password_login);

    }

    final static String SCOPES [] = {"https://graph.microsoft.com/User.Read"};
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";

    /* UI & Debugging Variables */
    private static final String TAG = MainActivity.class.getSimpleName();
    Button callGraphButton;
    Button signOutButton;

    /* Azure AD Variables */
    private PublicClientApplication sampleApp;
    private IAuthenticationResult authResult;



    /* Set the UI for signed out account */
    private void updateSignedOutUI() {
        callGraphButton.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.INVISIBLE);
        findViewById(R.id.welcome).setVisibility(View.INVISIBLE);
        findViewById(R.id.graphData).setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.graphData)).setText("No Data");

        Toast.makeText(getBaseContext(), "Signed Out!", Toast.LENGTH_SHORT)
                .show();
    }

    /* Use MSAL to acquireToken for the end-user
     * Callback will call Graph api w/ access token & update UI
     */
    private void onCallGraphClicked() {
        sampleApp.acquireToken(getActivity(), SCOPES, getAuthInteractiveCallback());
    }

    public Activity getActivity() {
        return this;
    }

    /* Callback used in for silent acquireToken calls.
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */
    private AuthenticationCallback getAuthSilentCallback() {
        return new AuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");

                /* Store the authResult */
                authResult = authenticationResult;

                /* call graph */
                callGraphAPI();

                /* update the UI to post call graph state */
//                updateSuccessUI();
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside the exception */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                } else if (exception instanceof MsalUiRequiredException) {
                    /* Tokens expired or no session, retry with interactive */
                }
            }

            @Override
            public void onCancel() {
                /* User cancelled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    /* Callback used for interactive request.  If succeeds we use the access
     * token to call the Microsoft Graph. Does not check cache
     */

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences("user_email", Context.MODE_PRIVATE);
        String emailGet = preferences.getString("email", "unknown");
        if(!emailGet.equals("unknown")){
            Intent intent = new Intent(SigninActivity.this, ListEventActivity.class);
            intent.putExtra("mail", emailGet);
            startActivity(intent);
            finish();
        }

    }

    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "ID Token: " + authenticationResult.getIdToken());

                /* Store the auth result */
                authResult = authenticationResult;

                /* call graph */
                callGraphAPI();
                showDialogLoadingLogin(dialog);
                SharedPreferences preferences = getSharedPreferences("user_email", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", authResult.getAccount().getUsername());
                editor.commit();
                Intent intent = new Intent(SigninActivity.this, ListEventActivity.class);
                intent.putExtra("mail", authResult.getAccount().getUsername());
                intent.putExtra("email", authResult.getAccount().getUsername());
                intent.putExtra("account", String.valueOf(sampleApp));
                startActivity(intent);
                finish();

                /* update the UI to post call graph state */
//                updateSuccessUI();
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside the exception */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            @Override
            public void onCancel() {
                /* User cancelled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    /* Clears an account's tokens from the cache.
     * Logically similar to "sign out" but only signs out of this app.
     * User will get interactive SSO if trying to sign back-in.
     */
    private void onSignOutClicked() {
        /* Attempt to get a user and acquireTokenSilent
         * If this fails we do an interactive request
         */
        sampleApp.getAccounts(new PublicClientApplication.AccountsLoadedCallback() {
            @Override
            public void onAccountsLoaded(final List<IAccount> accounts) {

                if (accounts.isEmpty()) {
                    /* No accounts to remove */

                } else {
                    for (final IAccount account : accounts) {
                        sampleApp.removeAccount(
                                account,
                                new PublicClientApplication.AccountsRemovedCallback() {
                                    @Override
                                    public void onAccountsRemoved(Boolean isSuccess) {
                                        if (isSuccess) {
                                            /* successfully removed account */
                                        } else {
                                            /* failed to remove account */
                                        }
                                    }
                                });
                    }
                }
//                Intent intentOut = new Intent(SigninActivity.this, SigninActivity.class);
////                updateSignedOutUI();
//                startActivity(intentOut);
            }
        });
    }

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    private void callGraphAPI() {
        Log.d(TAG, "Starting volley request to graph");

        /* Make sure we have a token to send to graph */
        if (authResult.getAccessToken() == null) {return;}

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL,
                parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());
                updateGraphUI(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authResult.getAccessToken());
                return headers;
            }
        };

        Log.d(TAG, "Adding HTTP GET to Queue, Request: " + request.toString());

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    /* Sets the graph response */
    private void updateGraphUI(JSONObject graphResponse) {

//        TextView graphText = findViewById(R.id.graphData);
////        graphText.setText(graphResponse.toString());
//        graphText.setText("");
    }

    private void showDialogLoadingLogin(Dialog dialog){
        dialog.setContentView(R.layout.loading_signin_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void dissmisDialogLoading(Dialog dialog ){
        dialog.dismiss();
    }



}
