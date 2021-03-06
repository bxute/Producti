/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.ui.activity;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bxute.producti.R;
import com.bxute.producti.preference.PreferenceHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
  static final int REQUEST_ACCOUNT_PICKER = 1000;
  static final int REQUEST_AUTHORIZATION = 1001;
  static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
  static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
  private static final String PREF_ACCOUNT_NAME = "accountName";
  private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS};

  GoogleAccountCredential mCredential;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    Button configureBtn = findViewById(R.id.sign_in_button);

    mCredential = GoogleAccountCredential.usingOAuth2(
     getApplicationContext(), Arrays.asList(SCOPES))
     .setBackOff(new ExponentialBackOff());

    configureBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getResultsFromApi();
      }
    });
  }

  /**
   * Attempt to call the API, after verifying that all the preconditions are
   * satisfied. The preconditions are: Google Play Services installed, an
   * account was selected and the device currently has online access. If any
   * of the preconditions are not satisfied, the app will prompt the user as
   * appropriate.
   */
  private void getResultsFromApi() {
    if (!isGooglePlayServicesAvailable()) {
      acquireGooglePlayServices();
    } else if (mCredential.getSelectedAccountName() == null) {
      chooseAccount();
    } else if (!isDeviceOnline()) {
      Toast.makeText(this, "Network error!", Toast.LENGTH_LONG).show();
    } else {
      new MakeRequestTask(mCredential).execute();
    }
  }

  /**
   * Attempts to set the account used with the API credentials. If an account
   * name was previously saved it will use that one; otherwise an account
   * picker dialog will be shown to the user. Note that the setting the
   * account to use with the credentials object requires the app to have the
   * GET_ACCOUNTS permission, which is requested here if it is not already
   * present. The AfterPermissionGranted annotation indicates that this
   * function will be rerun automatically whenever the GET_ACCOUNTS permission
   * is granted.
   */
  @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
  private void chooseAccount() {
    if (EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS)) {
      String accountName = PreferenceHelper.getInstance().getAccountName();
      if (accountName != null) {
        mCredential.setSelectedAccountName(accountName);
        getResultsFromApi();
      } else {
        // Start a dialog from which the user can choose an account
        startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
      }
    } else {
      // Request the GET_ACCOUNTS permission via a user dialog
      EasyPermissions.requestPermissions(this,
       "This app needs to access your Google account (via Contacts).",
       REQUEST_PERMISSION_GET_ACCOUNTS,
       Manifest.permission.GET_ACCOUNTS);
    }
  }

  /**
   * Called when an activity launched here (specifically, AccountPicker
   * and authorization) exits, giving you the requestCode you started it with,
   * the resultCode it returned, and any additional data from it.
   *
   * @param requestCode code indicating which activity result is incoming.
   * @param resultCode  code indicating the result of the incoming
   *                    activity result.
   * @param data        Intent (containing result data) returned by incoming
   *                    activity result.
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case REQUEST_GOOGLE_PLAY_SERVICES:
        if (resultCode != RESULT_OK) {
        } else {
          getResultsFromApi();
        }
        break;
      case REQUEST_ACCOUNT_PICKER:
        if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
          String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
          if (accountName != null) {
            PreferenceHelper.getInstance().setAccountName(accountName);
            mCredential.setSelectedAccountName(accountName);
            getResultsFromApi();
          }
        }
        break;
      case REQUEST_AUTHORIZATION:
        if (resultCode == RESULT_OK) {
          getResultsFromApi();
        }
        break;
    }
  }

  /**
   * Respond to requests for permissions at runtime for API 23 and above.
   *
   * @param requestCode  The request code passed in
   *                     requestPermissions(android.app.Activity, String, int, String[])
   * @param permissions  The requested permissions. Never null.
   * @param grantResults The grant results for the corresponding permissions
   *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
   */
  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }


  /**
   * Checks whether the device currently has a network connection.
   *
   * @return true if the device has a network connection, false otherwise.
   */
  private boolean isDeviceOnline() {
    ConnectivityManager connMgr =
     (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    return (networkInfo != null && networkInfo.isConnected());
  }

  /**
   * Check that Google Play services APK is installed and up to date.
   *
   * @return true if Google Play Services is available and up to
   * date on this device; false otherwise.
   */
  private boolean isGooglePlayServicesAvailable() {
    GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
    final int connectionStatusCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
    return connectionStatusCode == ConnectionResult.SUCCESS;
  }

  /**
   * Attempt to resolve a missing, out-of-date, invalid or disabled Google
   * Play Services installation via a user dialog, if possible.
   */
  private void acquireGooglePlayServices() {
    GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
    final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
    if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
      showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
    }
  }


  /**
   * Display an error dialog showing that Google Play Services is missing
   * or out of date.
   *
   * @param connectionStatusCode code describing the presence (or lack of)
   *                             Google Play Services on this device.
   */
  void showGooglePlayServicesAvailabilityErrorDialog(
   final int connectionStatusCode) {
    GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
    Dialog dialog = apiAvailability.getErrorDialog(
     LoginActivity.this,
     connectionStatusCode,
     REQUEST_GOOGLE_PLAY_SERVICES);
    dialog.show();
  }

  private void openHomePage() {
    Intent intent = new Intent(this, HomeActivity.class);
    startActivity(intent);
    finish();
  }

  @Override
  public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

  }

  @Override
  public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

  }

  /**
   * An asynchronous task that handles the Google Sheets API call.
   * Placing the API calls in their own task ensures the UI stays responsive.
   */
  private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
    private com.google.api.services.sheets.v4.Sheets mService = null;
    private Exception mLastError = null;

    MakeRequestTask(GoogleAccountCredential credential) {
      HttpTransport transport = AndroidHttp.newCompatibleTransport();
      JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
      mService = new com.google.api.services.sheets.v4.Sheets.Builder(
       transport, jsonFactory, credential)
       .setApplicationName("Google Sheets API Android Quickstart")
       .build();
    }

    /**
     * Background task to call Google Sheets API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected List<String> doInBackground(Void... params) {
      try {
        return getDataFromApi();
      }
      catch (Exception e) {
        mLastError = e;
        cancel(true);
        return null;
      }
    }

    private List<String> getDataFromApi() throws IOException {
      String spreadsheetId = "1DK0vMjdAG72uX2IpJbzZjR2F49DuJrvjXf0JqV-J1iI";
      String range = "Sheet1!A4:C10";
      List<String> results = new ArrayList<>();

      ValueRange response = this.mService.spreadsheets().values()
       .get(spreadsheetId, range)
       .execute();

      List<List<Object>> values = response.getValues();
      if (values != null) {
        for (List row : values) {
          if (row.size() > 0) {
            results.add(row.get(0)+"  |"+row.get(1) +"  |"+row.get(2)+"\n");
          }
        }
      }
      return results;
    }

    @Override
    protected void onPreExecute() {
      Log.d("LoginPage", "getting data from sheet...");
    }

    @Override
    protected void onPostExecute(List<String> output) {
      if (output == null || output.size() == 0) {
        Log.d("LoginPage", "empty response");
      } else {
        output.add(0, "Data retrieved using the Google Sheets API:");
        Log.d("LoginPage", TextUtils.join("\n", output));
      }
    }

    @Override
    protected void onCancelled() {
      if (mLastError != null) {
        if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
          showGooglePlayServicesAvailabilityErrorDialog(
           ((GooglePlayServicesAvailabilityIOException) mLastError)
            .getConnectionStatusCode());
        } else if (mLastError instanceof UserRecoverableAuthIOException) {
          startActivityForResult(
           ((UserRecoverableAuthIOException) mLastError).getIntent(),
           LoginActivity.REQUEST_AUTHORIZATION);
        } else {
          Log.d("LoginPage", "The following error occurred:\n"
           + mLastError.getMessage());
        }
      } else {
        Log.d("LoginPage", "Request cancelled.");
      }
    }
  }
}
