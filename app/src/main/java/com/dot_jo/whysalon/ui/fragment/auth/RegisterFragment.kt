package com.dot_jo.whysalon.ui.fragment.auth

import android.graphics.Paint
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.databinding.FragmentRegisterBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.showActivity
import com.dot_jo.whysalon.util.observe
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(), CountryCodePicker.OnCountryChangeListener {

private var  countryCode="+962"
    private val mViewModel: AuthViewModel by viewModels()
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private fun onclick() {
        binding.tvSignin.setPaintFlags(binding.tvSignin.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        binding.tvSignin.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.btnContineAsGuest.setOnClickListener {
            mViewModel.continueAsGyest()
        }
        binding.btnGoogle.setOnClickListener {
            signInWithGoogle()
        }
        binding.btnSignup.setOnClickListener {
            mViewModel.isVaildRegisteration(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                countryCode, binding.etPhone.text.toString(),
                binding.etPassword.text.toString(),
                binding.etPasswordComfirm.text.toString()
            )
        }
    }

    override fun onFragmentReady() {
        onclick()
        googleInit()
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

            is AuthAction.RegisterSucess -> {
                showProgress(false)
                PrefsHelper.saveUserData(action.data)

                PrefsHelper.saveToken(action.data.client?.token)
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

    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        signInWithGoogleLauncher.launch(signInIntent)
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            val fullName = account.displayName?.split(" ")
            mViewModel.isVaildRegisteration(
                if (fullName.isNullOrEmpty()) "" else fullName[0],
                account.email!!,
countryCode,
                "2222222",
                account.id!!,
                account.id!!,
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
    override fun onCountrySelected() {
        countryCode ="+"+ binding.countryCodePicker.selectedCountryCode

    }
}