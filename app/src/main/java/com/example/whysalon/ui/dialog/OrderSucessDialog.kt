package com.example.whysalon.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
 import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.whysalon.R
import com.example.whysalon.databinding.DialogSucessOrderBinding
import com.example.whysalon.ui.activity.MainActivity
import com.example.whysalon.util.ext.showActivity
import java.util.*



class OrderSucessDialog(
 ) : DialogFragment(R.layout.dialog_sucess_order) {

     private lateinit var binding: DialogSucessOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        if (dialog?.isShowing == false) {
            binding = DialogSucessOrderBinding.inflate(inflater)
            return binding.root
        } else return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog!!.window?.setLayout(width, height)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window?.setGravity(Gravity.TOP)
        dialog!!.setCancelable(false)
        setLayoutMarginTop(binding.root, 200f)

         onClick()

    }

    fun setLayoutMarginTop(view: View, space: Float) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin = space.toInt()
        view.layoutParams = layoutParams
    }

    private fun onClick() {



        binding.btnDone.setOnClickListener {

            dismiss()
            findNavController().navigate(
                R.id.bookingFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
            )
         //   findNavController().navigate(R.id.bookingFragment,pop)
         //   showActivity(MainActivity::class.java, clearAllStack = true)
        }

    }



}