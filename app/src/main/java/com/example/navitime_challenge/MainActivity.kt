package com.example.navitime_challenge

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import androidx.work.Data
import androidx.work.Logger
import com.example.navitime_challenge.BuildConfig.APPLICATION_ID
import com.example.navitime_challenge.adapter.ViewPagerAdapter
import com.example.navitime_challenge.databinding.ActivityMainBinding
import com.example.navitime_challenge.ui.FragmentHome
import com.example.navitime_challenge.ui.FragmentOrderList
import com.example.navitime_challenge.ui.FragmentOrderMap
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
import com.google.android.material.tabs.TabLayout

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.navitime_challenge.ui.FragmentCalendarTest
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener


import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.concurrent.TimeUnit




class MainActivity : AppCompatActivity(), OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val TAG = "MainActivity"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    private lateinit var binding: ActivityMainBinding

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: ViewPagerAdapter


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var location: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        tabLayout = binding.tabLayout
        viewPager = binding.pager
        adapter = ViewPagerAdapter(supportFragmentManager, this)

        // Add fragments
        adapter.addFragment(FragmentHome(), "Home")
        adapter.addFragment(FragmentOrderList(), "OrderList")
        //adapter.addFragment(FragmentOrderMap(), "OrderMap")
        adapter.addFragment(FragmentCalendarTest(), "CalendarTest")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(pager)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        location = Location("dummyProvider")
        location.latitude = 35.4848
        location.longitude = 139.6196

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("212813157070-mvk14p3ueslbfcqa9sho4q8dplov27m4.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        buttonServiceStart.setOnClickListener {
            //startWorker()
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, REQUEST_PERMISSIONS_REQUEST_CODE)

        }

    }

    override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }


    private fun startWorker() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = "通知のタイトル的情報を設定"
        val id = "notification_work"
        val notifyDescription = "この通知の詳細情報を設定します"

        if (manager.getNotificationChannel(id) == null) {
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            channel.apply {
                description = notifyDescription
            }
            manager.createNotificationChannel(channel)
        }

        val inputData = Data.Builder().putString("title", "シフト提案_test1").putString("message","提案シフトがあります。確認してください。").build()
        val Worker = OneTimeWorkRequestBuilder<LocalNotificationWorker>()
            .setInitialDelay(3, TimeUnit.SECONDS)
            .setInputData(inputData)
            .build()
        WorkManager.getInstance().enqueue(Worker)
        val inputData2 = Data.Builder().putString("title", "シフト提案_test2").putString("message","提案シフトがあります。確認してください。").build()
        val Worker2 = OneTimeWorkRequestBuilder<LocalNotificationWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .setInputData(inputData2)
            .build()
        WorkManager.getInstance().enqueue(Worker2)

    }


    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     *
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    location = task.result!!
                } else {
                    Log.w(TAG, "getLastLocation:exception", task.exception)
                    showSnackbar(R.string.no_location_detected)
                }
            }
    }

    private fun showSnackbar(
        snackStrId: Int,
        actionStrId: Int = 0,
        listener: View.OnClickListener? = null
    ) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), getString(snackStrId),
            LENGTH_INDEFINITE)
        if (actionStrId != 0 && listener != null) {
            snackbar.setAction(getString(actionStrId), listener)
        }
        snackbar.show()
    }

    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions() =
        ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION)) {
            // Provide an additional rationale to the user. This would happen if the user denied the
            // request previously, but didn't check the "Don't ask again" checkbox.
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar(R.string.permission_rationale, android.R.string.ok, View.OnClickListener {
                // Request permission
                startLocationPermissionRequest()
            })

        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                grantResults.isEmpty() -> Log.i(TAG, "User interaction was cancelled.")

                // Permission granted.
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> getLastLocation()

                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                else -> {
                    showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", APPLICATION_ID, null)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                        })
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            val status = result.status
            Log.w(TAG,"result:"+result)
            Log.w(TAG, "isSuccess:"+result.isSuccess)
            Log.w(TAG, "Status:"+status)
            //handleSignInResult(result)
            if (result.isSuccess) {
                try {
                    val account: GoogleSignInAccount = result.signInAccount as GoogleSignInAccount
                    val idToken = account.idToken
                    Log.w(TAG,"id_token"+idToken)
                } catch (e: ApiException) {
                    Log.e("error",e.toString())
                }
            }
        }
    }

    /*
    private fun handleSignInResult(result : GoogleSignInResult) {
      Log.d(TAG, "handleSignInResult:" + result.isSuccess());
      if (result.isSuccess()) {
          // Signed in successfully, show authenticated UI.
          val acct : GoogleSignInAccount? = result.getSignInAccount();
      } else {
          // Signed out, show unauthenticated UI.
      }
  }
     */

}
