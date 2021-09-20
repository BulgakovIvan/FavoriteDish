package com.firstproject.favdish.view.fragments

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.bumptech.glide.Glide
import com.firstproject.favdish.R
import com.firstproject.favdish.databinding.AddUpdateFragmentBinding
import com.firstproject.favdish.databinding.DialogCustomImageSelectionBinding
import com.firstproject.favdish.utils.saveImage
import com.firstproject.favdish.view.activities.CameraXApp
import com.firstproject.favdish.view.dialogs.showRationalDialogForPermissions
import com.firstproject.favdish.viewmodels.AddUpdateViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener

class AddUpdateFragment : Fragment(), LifecycleObserver {

    private var _binding: AddUpdateFragmentBinding? = null
    private val binding get() = _binding!!

    private val addUpdateViewModel: AddUpdateViewModel by viewModels()
    var changeActivity: ChangeActivity? = null

    val startCameraXApp = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.getStringExtra(IMAGE_URI)
            addUpdateViewModel.setImageUri(Uri.parse(uri))

            binding.ivAddDishImage.setImageDrawable(
                ContextCompat.getDrawable(requireActivity(), R.drawable.ic_vector_edit))
        }
    }

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
        uri.let {
            addUpdateViewModel.setImageUri(it)
            saveImage(requireActivity(), it)

            binding.ivAddDishImage.setImageDrawable(
                ContextCompat.getDrawable(requireActivity(), R.drawable.ic_vector_edit))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreatedActivity(){
        activity?.lifecycle?.removeObserver(this)

        changeActivity = context as ChangeActivity
//        Log.e(TAG, "set context")

        changeActivity?.hideMenu(false)
//        Log.e(TAG, "hide menu")

//        val fstate = lifecycle.currentState.name
//        val state = activity?.lifecycle?.currentState?.name
//        Log.e(TAG, "lifecycle fun on, state: $state - $fstate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddUpdateFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivAddDishImage.setOnClickListener {
            customImageSelectionDialog()
        }

        addUpdateViewModel.imageUri.observe(viewLifecycleOwner) {
            Glide.with(this)
                    .load(it)
                    .centerCrop()
                    .into(binding.ivDishImage)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        changeActivity?.hideMenu(true)
    }

    private fun customImageSelectionDialog() {
        val dialog = Dialog(requireActivity())
        val binding = DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {
            Dexter.withContext(requireActivity()).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            startCameraXApp.launch(
                                Intent(requireActivity(), CameraXApp::class.java)
                            )
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions(requireActivity())
                }

            }).onSameThread().check()

            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {
            Dexter.withContext(requireActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        getContent.launch("image/*")
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        Toast.makeText(
                            requireActivity(),
                            "You have denied the storage permission to select image.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest,
                        token: PermissionToken
                    ) {
                        showRationalDialogForPermissions(requireActivity())
                    }
                })
                .onSameThread()
                .check()

            dialog.dismiss()
        }

        dialog.show()
    }

    interface ChangeActivity {
        fun hideMenu(isVisible: Boolean)
    }

    companion object {
        fun newInstance() = AddUpdateFragment()
        const val IMAGE_URI = "IMAGE_URI"
    }
}