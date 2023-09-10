package com.dot_jo.whysalon.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.databinding.DialogBootomRateBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.fragment.home.HomeAction
import com.dot_jo.whysalon.ui.fragment.home.HomeViewModel
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.loadImage
import com.dot_jo.whysalon.util.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class RateBottomSheet(
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBootomRateBinding
    private lateinit var parent: MainActivity
    var rate = 0 // not rated yet
    var state = 0 // not allowed_to_submit  // 1// allow to submit
    private var orderId by Delegates.notNull<String>()
    private var barberId by Delegates.notNull<String>()
    private val mViewModel: HomeViewModel by viewModels()

    companion object {
        fun newInstance(
        ): RateBottomSheet {
            val args = Bundle()
            val f = RateBottomSheet()
            f.arguments = args
            return f
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DialogBootomRateBinding.inflate(inflater)
        parent = requireActivity() as MainActivity
        orderId = arguments?.getString(Constants.ORDER_ID).toString()
        barberId = arguments?.getString(Constants.BARBER_ID).toString()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.tvskip.setOnClickListener {
            dismiss()
        }
        binding.btnDone.setOnClickListener {
            if (state == 1) {
                mViewModel.rateBarber(
                    binding.rating.rating.toString(),
                    binding.notesEt.text.toString(),
                    barberId,
                    orderId
                )
            }
        }
        binding.rating.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                Toast.makeText(requireContext(), rating.toString(), Toast.LENGTH_SHORT).show()
                rate = rating.toInt()
                if (rating > 2) {
                    binding.btnDone.background =
                        resources.getDrawable(R.drawable.bg_btn_black_white_border)
                    binding.rvServices.isVisible = false
                    binding.tv7.isVisible = false
                    binding.tv6.isVisible = false
                    state = 1
                } else {
                    binding.btnDone.background = resources.getDrawable(R.drawable.bg_btn_gray_dark)

                    binding.rvServices.isVisible = false
                    binding.tv7.isVisible = false
                    binding.tv6.isVisible = false
                    state = 0
                }
            }


        binding.ivProfile.loadImage(resources.getDrawable(R.drawable.user_tst), isCircular = true)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleViewState(action: HomeAction) {
        when (action) {
            is HomeAction.Rate -> {
                Toast.makeText(requireContext(), action.data.scalar.toString(), Toast.LENGTH_SHORT)
                    .show()
                dismiss()
            }

            is HomeAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstDialog)
                } else {
                    Toast.makeText(requireContext(), action.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            else -> {

            }
        }
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

}
