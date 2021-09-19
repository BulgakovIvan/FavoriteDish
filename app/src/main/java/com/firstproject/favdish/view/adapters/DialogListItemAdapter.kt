package com.firstproject.favdish.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firstproject.favdish.databinding.ItemCustomListBinding

class DialogListItemAdapter(
    private val activity: Activity,
    private val list: List<String>,
    private val selection: String
) : RecyclerView.Adapter<DialogListItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCustomListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvText.text = item
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: ItemCustomListBinding) : RecyclerView.ViewHolder(view.root) {
        val tvText = view.tvText
    }
}