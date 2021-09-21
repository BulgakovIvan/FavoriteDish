package com.firstproject.favdish.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.firstproject.favdish.databinding.ItemCustomListBinding
import com.firstproject.favdish.utils.FieldType
import com.firstproject.favdish.viewmodels.AddUpdateViewModel

class DialogListItemAdapter(
    private val fragment: DialogFragment,
    private val viewModel: ViewModel,
    private val list: List<String>,
    private val fieldType: FieldType
) : RecyclerView.Adapter<DialogListItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCustomListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvText.text = item

        holder.itemView.setOnClickListener {
            if (viewModel is AddUpdateViewModel) {
                when (fieldType) {
                    FieldType.DISH_TYPE -> {
                        viewModel.setType(item)
                        fragment.dismiss()
                    }
                    FieldType.DISH_CATEGORY -> {
                        viewModel.setCategory(item)
                        fragment.dismiss()
                    }
                    FieldType.DISH_COOKING_TIME -> {
                        viewModel.setCookingTime(item)
                        fragment.dismiss()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: ItemCustomListBinding) : RecyclerView.ViewHolder(view.root) {
        val tvText = view.tvText
    }
}