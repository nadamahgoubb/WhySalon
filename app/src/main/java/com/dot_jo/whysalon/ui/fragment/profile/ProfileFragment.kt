package com.dot_jo.whysalon.ui.fragment.profile


import android.graphics.Paint
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.response.Client
import com.dot_jo.whysalon.databinding.FragmentProfileBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.util.FileManager
import com.dot_jo.whysalon.util.PermissionManager
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.loadImage
import com.dot_jo.whysalon.util.observe
import com.dot_jo.whysalon.util.requestAppPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import javax.inject.Inject

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

    private fun onClick() {
        binding.btnDeletAccount.setOnClickListener {
            findNavController().navigate(R.id.deleteFragment)
        }
        binding.tvChangePass.setOnClickListener {
            findNavController().navigate(R.id.changePasswordFragment)
        }
        binding.ivEditPic.setOnClickListener {
            pickImage()
        }
    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.setTitle(resources.getString(R.string.profile))
        parent.showNotifactionFragment(false)
  //      parent.binding.ivIconNotifaction.isVisible= false
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.ivProfile.loadImage(resources.getDrawable(R.drawable.user_tst), isCircular = true)
        binding.tvChangePass.setPaintFlags(binding.tvChangePass.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
    }


    @Inject
    lateinit var permissionManager: PermissionManager


    private val imagePermissionLauncherResult = requestAppPermissions { allIsGranted, _ ->
        if (allIsGranted) {
            FileManager.pickOneImage(this, selectImageFromGalleryResult)
        } else
            showToast(getString(R.string.not_all_permissions_accepted))
    }

    private fun pickImage() {
        if (permissionManager.hasAllFilePickerPermissions()) {
            FileManager.pickOneImage(this, selectImageFromGalleryResult)
        } else {
            imagePermissionLauncherResult?.launch(permissionManager.getAllImagePermissions())
        }
    }

    var image: File? = null

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                FileManager.from(requireActivity(), it)?.let { file ->
                    image = file

                    binding.ivProfile.loadImage(file, isCircular = true)
                    mViewModel.editProfile(file)
                }
            }
        }

}
