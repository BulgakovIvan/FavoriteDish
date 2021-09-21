package com.firstproject.favdish.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstproject.favdish.R
import com.firstproject.favdish.databinding.DialogCustomListBinding
import com.firstproject.favdish.model.DialogCustomListModel
import com.firstproject.favdish.view.adapters.DialogListItemAdapter
import com.firstproject.favdish.viewmodels.AddUpdateViewModel

class DialogCustomList : DialogFragment() {
    private val addUpdateViewModel: AddUpdateViewModel by activityViewModels()

    private var _binding: DialogCustomListBinding? = null
    private val binding get() = _binding!!

    private var parameters: DialogCustomListModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parameters = arguments?.getParcelable(DIALOG_PARAMETERS)
        if (parameters == null) {
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner_dialog)
        _binding = DialogCustomListBinding.inflate(inflater, container, false)
//            inflater.inflate(R.layout.dialog_custom_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDialogTitle.text = parameters!!.title

        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())

        val adapter = DialogListItemAdapter(this, addUpdateViewModel,
            parameters!!.itemList, parameters!!.fieldType)
        binding.rvList.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
//        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val DIALOG_PARAMETERS = "dialog_parameters"

//        fun newInstance(params: DialogCustomListModel) : DialogCustomList {
//            val args = Bundle()
//            val fragment = DialogCustomList()
//            args.putParcelable(DIALOG_PARAMETERS, params)
//            fragment.arguments = args
//            return fragment
//        }

        fun newInstance(params: DialogCustomListModel) =
            DialogCustomList().apply {
                arguments = Bundle().apply {
                    putParcelable(DIALOG_PARAMETERS, params)
                }
            }
    }
}