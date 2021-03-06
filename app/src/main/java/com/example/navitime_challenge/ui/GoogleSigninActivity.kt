package com.example.navitime_challenge.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.navitime_challenge.R
import com.example.navitime_challenge.database.getGoogleAuthDatabase
import com.example.navitime_challenge.databinding.ActivitySigninBinding
import com.example.navitime_challenge.domain.GoogleAuthPayload
import com.example.navitime_challenge.repository.GoogleAuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
class GoogleSignInActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySigninBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var repository: GoogleAuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_signin
        )

        // Button listeners
        binding.signInButton.setOnClickListener(this)
        binding.signOutButton.setOnClickListener(this)
        binding.disconnectButton.setOnClickListener(this)

        repository = GoogleAuthRepository(getGoogleAuthDatabase(application))

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestServerAuthCode(getString(R.string.default_web_client_id))
            .requestScopes(Scope("https://www.googleapis.com/auth/calendar.events.readonly"))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI(null)
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                getAccessToken(account!!.serverAuthCode)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Timber.w("Google sign in failed", e)
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Timber.d("firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.i("signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user, acct.serverAuthCode)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w("signInWithCredential:failure", task.exception)
                    Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
    // [END auth_with_google]

    // [START signin]
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

    private fun signOut() {
        // Firebase sign out
        auth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun revokeAccess() {
        // Firebase sign out
        auth.signOut()

        // Google revoke access
        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun updateUI(user: FirebaseUser?, authCode: String? = null) {
        if (user != null) {
            status.text = getString(R.string.google_status_fmt, user.email)
            detail.text = getString(R.string.firebase_status_fmt, user.uid)

            signInButton.visibility = View.GONE
            signOutAndDisconnect.visibility = View.VISIBLE

            user.getIdToken(true).addOnSuccessListener {
                val idToken = it.token
                Timber.d(authCode)
                Timber.d(idToken)

                val intent = Intent(application, MainActivity::class.java)
                intent.putExtra(ID_TOKEN, idToken)
                intent.putExtra(AUTHCODE, authCode)
                startActivity(intent)
            }


        } else {
            status.setText(R.string.signed_out)
            detail.text = null

            signInButton.visibility = View.VISIBLE
            signOutAndDisconnect.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.signInButton -> signIn()
            R.id.signOutButton -> signOut()
            R.id.disconnectButton -> revokeAccess()
        }
    }

    private fun getAccessToken(authCode: String?) {
        coroutineScope.launch {
            val p = GoogleAuthPayload(
                clientId = getString(R.string.default_web_client_id),
                clientSecret = "YAtyWz_fZlJsV34WZC6L9DMh",
                grantType = "authorization_code",
                code = authCode
            )
            repository.getAccessToken(p)
            Timber.d("Success: Get Google API Access Token")
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        const val ID_TOKEN = "com.example.navitime_challenge.ui.TEXTDATA"
        const val AUTHCODE = "com.example.navitime_challenge.ui.TEXTDATA"
    }
}
