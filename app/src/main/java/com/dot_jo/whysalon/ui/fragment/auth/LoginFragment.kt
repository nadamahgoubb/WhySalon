@file:Suppress("DEPRECATION")

package com.dot_jo.whysalon.ui.fragment.auth

import android.content.Intent
import android.content.IntentSender
import android.graphics.Paint
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.databinding.FragmentLoginBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.showActivity
import com.dot_jo.whysalon.util.observe
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonDisposableHandle.parent


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val mViewModel: AuthViewModel by viewModels()
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onFragmentReady() {
        googleInit()
        onclick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }
        }
    }

    private fun googleInit() {
        // Setup Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun handleViewState(action: AuthAction) {
        when (action) {
            is AuthAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AuthAction.LoginSuccess -> {
                showProgress(false)

                PrefsHelper.saveToken(action.data.client?.token)
                PrefsHelper.saveUserData(action.data)
                gotoHome()

            }

            is AuthAction.ContinueAsGuest -> {
                showProgress(false)
                PrefsHelper.saveToken(action.data.client?.token)
                PrefsHelper.saveUserData(null)
                gotoHome()

            }

            is AuthAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    showToast(it.substring(3, it.length))
                } else {
                    showToast(action.message)
                }
                showProgress(false)

            }

            else -> {

            }
        }
    }

    private fun onclick() {
        binding.tvForgetPassword.setPaintFlags(binding.tvForgetPassword.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.tvSignup.setPaintFlags(binding.tvSignup.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        binding.tvSignup.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
        binding.tvForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.resetPasswordFragment)
        }
        binding.btnLogin.setOnClickListener {
            mViewModel.isVaildLogin(
                binding.etEmail.text.toString(), binding.etPassword.text.toString(), 0
            )
        }
        binding.btnGoogle.setOnClickListener {
            signInWithGoogle()
        }
        binding.btnContineAsGuest.setOnClickListener {
            mViewModel.continueAsGyest()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        signInWithGoogleLauncher.launch(signInIntent)
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            val fullName = account.displayName?.split(" ")
            Log.d(
                "islam",
                "handleSignInResult:${account.id} ${account.email} ${account.displayName}"
            )
            mViewModel.isVaildLogin(
                account.email!!,
                account.id!!,
                0
            )
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("islam", "handleSignInResult: $e")
            showToast(e.message.toString())
        }
    }

    private val signInWithGoogleLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val data = result.data
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.d("islam", "task: ${task.exception}")
            handleGoogleSignInResult(task)
        }

    private fun gotoHome() {
        showActivity(MainActivity::class.java, clearAllStack = true)
    }
}