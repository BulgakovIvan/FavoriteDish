package com.firstproject.favdish.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstproject.favdish.R
import com.firstproject.favdish.databinding.DialogCustomListBinding
import com.firstproject.favdish.view.adapters.DialogListItemAdapter

class DialogCustomList(
    private val title: String,
    private val itemList: List<String>,
    private val selection: String = ""
) : DialogFragment() {

    private var _binding: DialogCustomListBinding? = null
    private val binding get() = _binding!!

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
        binding.tvDialogTitle.text = title

        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = DialogListItemAdapter(requireActivity(), itemList, selection)
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
}