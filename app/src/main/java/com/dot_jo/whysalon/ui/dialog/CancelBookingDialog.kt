package com.dot_jo.whysalon.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
 import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.databinding.DialogCancelBookingBinding
import com.dot_jo.whysalon.ui.interfaces.ConfirmCancelBookingListener


class CancelBookingDialog( val listener : ConfirmCancelBookingListener
 ) : DialogFragment(R.layout.dialog_cancel_booking) {

     private lateinit var binding: DialogCancelBookingBinding

    companion object {
        fun newInstance(
       listener:       ConfirmCancelBookingListener   ): CancelBookingDialog {
            val args = Bundle()
            val f = CancelBookingDialog(
           listener  )
            f.arguments = args
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        if (dialog?.isShowing == false) {
            binding = DialogCancelBookingBinding.inflate(inflater)
            return binding.root
        } else return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog!!.window?.setLayout(width, height)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window?.setGravity(Gravity.CENTER)
        setLayoutMarginTop(binding.root, 100f)


        onClick()

    }

    fun setLayoutMarginTop(view: View, bottom: Float) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin = bottom.toInt()
        view.layoutParams = layoutParams
    }

    private fun onClick() {

        binding.btnIgnore.setOnClickListener {
            dismiss()
        }
        binding.btnContinue.setOnClickListener {
            dismiss()
         listener.onConfirmBooking()
        }

    }



}