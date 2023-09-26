package com.dot_jo.whysalon.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
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
     var rate = 0 // not rated yet
    var state = 0 // not allowed_to_submit  // 1// allow to submit
    private var orderId by Delegates.notNull<String>()
    private var barberId by Delegates.notNull<String>()
    private var barber_image by Delegates.notNull<String>()
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        setLayoutMargin (binding.root, 50)


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DialogBootomRateBinding.inflate(inflater)
         orderId = arguments?.getString(Constants.ORDER_ID).toString()
        barberId = arguments?.getString(Constants.BARBER_ID).toString()
        barber_image = arguments?.getString(Constants.BARBER).toString()
      binding.ivProfile.loadImage(barber_image, isCircular = true)
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.tvskip.setOnClickListener {
            dismiss()
        }
        binding.btnDone.setOnClickListener {
             binding.progressBar.isVisible= true
                 mViewModel.rateBarber(
                    binding.rating.rating.toString(),
                    binding.notesEt.text.toString(),
                    barberId,
                    orderId
                )

        }
        binding.rating.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                 rate = rating.toInt()

            }


         return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleViewState(action: HomeAction) {
        when (action) {
            is HomeAction.Rate -> {
                binding.progressBar.isVisible= false
                Toast.makeText(requireContext(), action.data.scalar.toString(), Toast.LENGTH_SHORT)
                    .show()
                dismiss()
            }

            is HomeAction.ShowFailureMsg -> action.message?.let {
                binding.progressBar.isVisible= false
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

}
