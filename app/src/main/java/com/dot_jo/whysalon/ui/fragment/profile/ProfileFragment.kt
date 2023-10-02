package com.dot_jo.whysalon.ui.fragment.profile


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.data.response.Client
import com.dot_jo.whysalon.databinding.FragmentProfileBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.util.FileManager
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.loadImage
import com.dot_jo.whysalon.util.observe
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

@AndroidEntryPoint

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private lateinit var parent: MainActivity
    private val mViewModel: ProfileViewModel by viewModels()
    override fun onFragmentReady() {
        setupUi()
        onClick()
        mViewModel.apply {
            mViewModel.showProfile()
            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.showProfile()
            binding.swiperefreshHome.isRefreshing = false
        }
    }

    private fun handleViewState(action: ProfileAction) {
        when (action) {
            is ProfileAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is ProfileAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstDialog)
                } else {
                    showToast(action.message)
                }
                showProgress(false)

            }
   is ProfileAction.ShowFailureUpdatingImage -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstDialog)
                } else {
                    showToast(action.message)
                    mViewModel.showProfile()
                }
                showProgress(false)

            }

            is ProfileAction.ShowProfile -> {
                loadData(action.data.client)
            }

            else -> {

            }
        }
    }

    private fun loadData(client: Client?) {
        client?.let {
            binding.lytData.isVisible= true
            binding.ivProfile.loadImage(client.image, isCircular = true,placeHolderOnFsImage = R.drawable.empty_user_)
            binding.tvName.setText(client.name)
            binding.tvEmail.setText(client.email)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.setToolbarTitle(resources.getString(R.string.profile))
        parent.showNotifactionFragment(false)
        //      parent.binding.ivIconNotifaction.isVisible= false
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
        if(PrefsHelper.getUserData()?.client?.social == true){
            binding.lytChangepass.isVisible= true
            binding.tvChangePass.setPaintFlags(binding.tvChangePass.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        }
    }

    private fun onClick() {
        binding.btnDeletAccount.setOnClickListener {
            findNavController().navigate(R.id.deleteFragment)
        }
        binding.tvChangePass.setOnClickListener {
            findNavController().navigate(R.id.changePasswordFragment)
        }
        binding.ivEditPic.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }



// For this example, launch the photo picker and let the user choose images
// and videos. If you want the user to select a specific type of media file,
// use the overloaded versions of launch(), as shown in the section about how
// to select a single media item.


    var image: File? = null
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            CropImage.activity(uri)
                .start(requireContext(), this)
        } else {
         }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                //  val resultUri: Uri = result.uri
                //      image = File(getRealPathFromURI(parent, resultUri))
                result.uri?.let {
                    FileManager.from(requireActivity(), it)?.let { file ->
                                 image = file

                                binding.ivProfile.loadImage(file, isCircular = true)
                                mViewModel.editProfile(file)
                    }
                }  }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            //  val error = result.
            showToast(   data?.extras.toString())
        }
    }
}



