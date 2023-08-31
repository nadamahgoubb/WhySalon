package com.dot_jo.whysalon.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
 import com.dot_jo.whysalon.util.ext.bindView
import com.dot_jo.whysalon.util.ext.castToActivity
import com.dot_jo.whysalon.util.ext.showAppToast

abstract class BaseFragment<B : ViewDataBinding> : Fragment() {

    abstract fun onFragmentReady()


    private var _binding: B? = null
    open lateinit var binding: B
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindView()
        binding = _binding!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //   setToolbarTitle(mPageTitle())
        onFragmentReady()
    }

    override fun onResume() {
        super.onResume()
        showProgress(false)

    }

    fun showProgress(show: Boolean = true) {
        castToActivity<BaseActivity<*>> {
            it?.baseShowProgress?.set(show)
        }
    }

    fun showToast(message: String?) {
        context?.showAppToast(message)
    }

    override fun onDestroyView() {
        showProgress(false)
         _binding = null
        super.onDestroyView()
    }


}
