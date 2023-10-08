package com.dot_jo.whysalon.ui.dialog

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RatingBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.response.Client
 import com.dot_jo.whysalon.databinding.DialogBootomEditProfileBinding
import com.dot_jo.whysalon.ui.fragment.profile.ProfileAction
import com.dot_jo.whysalon.ui.fragment.profile.ProfileViewModel
import com.dot_jo.whysalon.util.formatDate
import com.dot_jo.whysalon.util.getMonthNameFromDate
import com.dot_jo.whysalon.util.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

interface EditProfileDialogClickLisenter {
    fun onEditClickListener()

}
@AndroidEntryPoint
class EditProfileDialog(
val client: Client, val listener: EditProfileDialogClickLisenter
) : BottomSheetDialogFragment() ,
    CountryCodePicker.OnCountryChangeListener {

    private lateinit var binding: DialogBootomEditProfileBinding
    private var countryCode = "+966"
    private var date_of_birth = ""

    var state = 0
    private var otp = ""
    var sendingCount = 0
    private val mViewModel: ProfileViewModel by viewModels()

    companion object {
        fun newInstance(
            client: Client,
            listener: EditProfileDialogClickLisenter
        ): EditProfileDialog {
            val args = Bundle()
            val f = EditProfileDialog(client,listener)
            f.arguments = args
            return f
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

       //setLayoutMargin (binding.root, 50)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DialogBootomEditProfileBinding.inflate(inflater)
        binding.countryCodePicker.setOnCountryChangeListener (this)
        showData()
countryCode= client.country_code.toString()

        binding.btnSignup.setOnClickListener {
             binding.progressBar.isVisible= true
                 mViewModel.isVaildEditProfile(
                    binding.etName.text.toString(),
                    binding.etEmail.text.toString(),
                countryCode, 
                     binding.etPhone.text.toString(),
                    date_of_birth  
                  
                )

        }
        binding.tvResend.setOnClickListener {
            sendingCount = 1
            mViewModel.editProfileParam?.email?.let { it1 ->
                mViewModel.checkEmailAfterRegisteration(
                    it1
                )
                binding.progressBar.isVisible= true
            }
        }
     /*   binding.cardback.setOnClickListener {
            if (state == 0) {
                requireActivity().onBackPressed()
            } else {
                state0()
            }

        }
       */
        binding.btnDone.setOnClickListener {
            if (binding.etOtp.otp == otp) {
                mViewModel.emailVerified = mViewModel.editProfileParam?.email.toString()
                mViewModel.editProfileParam?.let {
                        it1 -> mViewModel.editProfileData(it1  )
                    binding.progressBar.isVisible= true
                        }
                binding.lytLogin.isVisible = true
                binding.lytOtp.isVisible = false
            } else {
                 Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.wrong_otp),
                    Toast.LENGTH_SHORT
                ).show()       }

        }
        binding.etBirthdate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(requireActivity(),R.style.DialogTheme, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                var date =     LocalDate.of(year, monthOfYear+1, dayOfMonth)
                date.formatDate("yyyy-MM-dd", Locale.getDefault())
                date_of_birth = date.toString()     // Display Selected date in textbox
                binding.etBirthdate.setText(
                    "" + dayOfMonth + " " + (getMonthNameFromDate(date.toString()))?.substring(0,3)
                            + ", " + year)
            }, year, month, day)
            dpd.datePicker.maxDate = System.currentTimeMillis()
            dpd.show()
        }
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }
        }
         return binding.root
    }

    private fun showData() {
        binding.etBirthdate.setText(client.date_of_birth)
        binding.etName.setText(client.name)
        binding.etEmail.setText(client.email)
        binding.etPhone.setText(client.phone)
        date_of_birth= client.date_of_birth.toString()
        try {
            var country_code= client.country_code?.substring(1, client.country_code!!.length).toString()
            country_code?.toInt()?.let { binding.countryCodePicker.setCountryForPhoneCode(it) }

        }catch (e:Exception){

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleViewState(action: ProfileAction) {
        when (action) {

            is ProfileAction.ShowFailureMsg -> action.message?.let {
                binding.progressBar.isVisible = false
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstDialog)
                } else {
                    Toast.makeText(requireContext(), action.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            is ProfileAction.EmailCheckedAfterRegister -> {
                binding.progressBar.isVisible = false
                if (action.data.exists == true) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.email_already_exist),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    otp = action.data.otp_code.toString()
                    if (sendingCount == 0) state1()
                }
            }
is ProfileAction.ShowProfileUpdates ->{
  listener.onEditClickListener()
    dismiss()

}
            else -> {

            }
        }
    }
        fun state0() {
            sendingCount = 0
            binding.lytLogin.isVisible = true
            binding.lytOtp.isVisible = false
            state = 0
        }

        fun state1() {

        binding.lytLogin.isVisible = false
        binding.lytOtp.isVisible = true
        binding.tvEmailSendTo.text =
            resources.getString(R.string.enter_the_email_we_send_to) + mViewModel.editProfileParam?.email
        state = 1
    }
    fun setLayoutMargin(view: View, bottom: Int) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
         layoutParams.marginEnd = bottom
        layoutParams.marginStart =bottom
        view.layoutParams = layoutParams
    }

    @SuppressLint("CutPasteId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
    override fun onCountrySelected() {
        countryCode = "+" + binding.countryCodePicker.selectedCountryCode
    }

}
