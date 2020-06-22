// Copyright (c) Microsoft Corporation.
// All rights reserved.
//
// This code is licensed under the MIT License.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files(the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions :
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package com.azuresamples.msalandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import eventbox.R;

public class MainActivity extends AppCompatActivity
       {

    enum AppFragment {
        SingleAccount,
        MultipleAccount,
        B2C
    }

    private AppFragment mCurrentFragment;

    private ConstraintLayout mContentMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callGraphButton = (Button) findViewById(R.id.callGraph);
        signOutButton = (Button) findViewById(R.id.clearCache);

//        callGraphButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                onCallGraphClicked();
//            }
//        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSignOutClicked();
            }
        });

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
            onCallGraphClicked();
        }else{
            if(out.equals("out")){
                out = "";
                sampleApp.acquireToken(getActivity(), SCOPES, getAuthInteractiveCallback());
                onSignOutClicked();
            }
        }
    }
//
//    @Override
//    public boolean onNavigationItemSelected(final MenuItem item) {
//        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) { }
//
//            @Override
//            public void onDrawerOpened(@NonNull View drawerView) { }
//
//            @Override
//            public void onDrawerClosed(@NonNull View drawerView) {
//                // Handle navigation view item clicks here.
//                int id = item.getItemId();
//
//                if (id == R.id.nav_single_account) {
//                    setCurrentFragment(AppFragment.SingleAccount);
//                }
//
//                if (id == R.id.nav_multiple_account) {
//                    setCurrentFragment(AppFragment.MultipleAccount);
//                }
//
//                if (id == R.id.nav_b2c) {
//                    setCurrentFragment(AppFragment.B2C);
//                }
//
//                drawer.removeDrawerListener(this);
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) { }
//        });
//
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//    private void setCurrentFragment(final AppFragment newFragment){
//        if (newFragment == mCurrentFragment) {
//            return;
//        }
//
//        mCurrentFragment = newFragment;
//        setHeaderString(mCurrentFragment);
//        displayFragment(mCurrentFragment);
//    }
//
//    private void setHeaderString(final AppFragment fragment){
//        switch (fragment) {
//            case SingleAccount:
//                getSupportActionBar().setTitle("Single Account Mode");
//                return;
//
//            case MultipleAccount:
//                getSupportActionBar().setTitle("Multiple Account Mode");
//                return;
//
//            case B2C:
//                getSupportActionBar().setTitle("B2C Mode");
//                return;
//        }
//    }
//
//    private void displayFragment(final AppFragment fragment){
//        switch (fragment) {
//            case SingleAccount:
//                attachFragment(new SingleAccountModeFragment());
//                return;
//
//            case MultipleAccount:
//                attachFragment(new MultipleAccountModeFragment());
//                return;
//
//            case B2C:
//                attachFragment(new B2CModeFragment());
//                return;
//        }
//    }
//
//    private void attachFragment(final Fragment fragment) {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                .replace(mContentMain.getId(),fragment)
//                .commit();
//    }

    final static String SCOPES [] = {"https://graph.microsoft.com/User.Read"};
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";

    /* UI & Debugging Variables */
    private static final String TAG = MainActivity.class.getSimpleName();
    Button callGraphButton;
    Button signOutButton;

    /* Azure AD Variables */
    private PublicClientApplication sampleApp;
    private IAuthenticationResult authResult;

    /* Set the UI for successful token acquisition data */
    private void updateSuccessUI() {

        callGraphButton.setVisibility(View.INVISIBLE);
        signOutButton.setVisibility(View.VISIBLE);
        findViewById(R.id.welcome).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.welcome)).setText("Welcome, " +
                authResult.getAccount().getUsername());
        findViewById(R.id.graphData).setVisibility(View.VISIBLE);
        Toast.makeText(this, authResult.getAccount().getUsername(), Toast.LENGTH_SHORT).show();
    }

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
                updateSuccessUI();
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
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                intent.putExtra("mail", authResult.getAccount().getUsername());
                intent.putExtra("account", String.valueOf(sampleApp));
                startActivity(intent);

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
                Intent intentOut = new Intent(MainActivity.this, SigninActivity.class);
//                updateSignedOutUI();
                startActivity(intentOut);
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
        TextView graphText = findViewById(R.id.graphData);
//        graphText.setText(graphResponse.toString());
        graphText.setText("");
    }
}


