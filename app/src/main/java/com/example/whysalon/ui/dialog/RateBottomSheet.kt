package com.example.whysalon.ui.dialog

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
import com.example.whysalon.R
import com.example.whysalon.data.response.TimeResponse
import com.example.whysalon.databinding.DialogBootomRateBinding
import com.example.whysalon.ui.activity.MainActivity
import com.example.whysalon.ui.adapter.RateReasonsAdapter
import com.example.whysalon.ui.interfaces.RatingResaonsClickListener
import com.example.whysalon.util.ext.init
import com.example.whysalon.util.ext.loadImage
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RateBottomSheet(
) : BottomSheetDialogFragment(),  RatingResaonsClickListener {

    lateinit var adapter: RateReasonsAdapter
    private lateinit var binding: DialogBootomRateBinding
    private lateinit var parent: MainActivity
    var rate = 0 // not rated yet
    var state = 0 // not allowed_to_submit  // 1// allow to submit

    companion object {
        fun newInstance(
        ): RateBottomSheet {
            val args = Bundle()
            val f = RateBottomSheet()
            f.arguments = args
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DialogBootomRateBinding.inflate(inflater)
        parent = requireActivity() as MainActivity
        initAdapter()
        loadData()
        binding.tvskip.setOnClickListener {
            dismiss()
        }
        binding.btnDone.setOnClickListener {
            if(state==1){
                dismiss()
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
                    binding.tv6.isVisible = true
state=1
                } else {
                    binding.btnDone.background = resources.getDrawable(R.drawable.bg_btn_gray_dark)

                    binding.rvServices.isVisible = true
                 loadData()
                    binding.tv7.isVisible = true
                    binding.tv6.isVisible = false
                    state=0
                }
            }


        binding.ivProfile.loadImage(resources.getDrawable(R.drawable.user_tst), isCircular = true)
        return binding.root
    }

    private fun loadData() {
     var list = listOf(
            TimeResponse(id = 0, time = "First Case"),
            TimeResponse(id = 1, time = "Secccccccc Case"),
            TimeResponse(id = 2, time = "Third Case"),
            TimeResponse(id = 3, time = "Fourth Case"),
            TimeResponse(id = 4, time = "Fifth Case"),
            TimeResponse(id = 5, time = "Sixthhhhhhhhhhhhhhh Case")
        ) as MutableList<TimeResponse>
        adapter.list= list
        adapter.notifyDataSetChanged()
    }

    private fun initAdapter() {
        adapter = RateReasonsAdapter(this)
        binding.rvServices.init(requireContext(), adapter, 3, column = 3)

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

    override fun onRatingResaonsListener(b: ArrayList<TimeResponse>) {
        if (b.size > 0 && rate < 3) {
            binding.btnDone.background =
                resources.getDrawable(R.drawable.bg_btn_gray_dark)
            state=0

        } else {

            binding.btnDone.background =
                resources.getDrawable(R.drawable.bg_btn_black_white_border)
            state=1


        }
    }


}
