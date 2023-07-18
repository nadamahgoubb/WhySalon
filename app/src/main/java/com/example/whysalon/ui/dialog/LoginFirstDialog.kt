package com.example.whysalon.ui.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
 import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.whysalon.R
import com.example.whysalon.data.PrefsHelper
import com.example.whysalon.databinding.DialogLoginFirstBinding
import com.example.whysalon.databinding.DialogSucessOrderBinding
import com.example.whysalon.ui.activity.AuthActivity
import com.example.whysalon.util.Constants


class LoginFirstDialog(
 ) : DialogFragment(R.layout.dialog_login_first) {

     private lateinit var binding: DialogLoginFirstBinding

    companion object {
        fun newInstance(
         ): LoginFirstDialog {
            val args = Bundle()
            val f = LoginFirstDialog(
             )
            f.arguments = args
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        if (dialog?.isShowing == false) {
            binding = DialogLoginFirstBinding.inflate(inflater)
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
        binding.btnLogin.setOnClickListener {

            PrefsHelper.clear()
            var intent = Intent(activity, AuthActivity::class.java)
            intent.putExtra(Constants.Start, Constants.login)
            startActivity(intent)
            activity?.finish()
            dismiss()
        }

    }



}