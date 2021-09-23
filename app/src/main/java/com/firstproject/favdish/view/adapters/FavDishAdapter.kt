package com.firstproject.favdish.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firstproject.favdish.databinding.ItemDishLayoutBinding
import com.firstproject.favdish.model.entities.FavDish

// TODO: 22.09.2021 change to list adapter, with databind adapter???
class FavDishAdapter()
    :RecyclerView.Adapter<FavDishAdapter.ViewHolder>(){

    private var dishes: List<FavDish> = listOf()
    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.parent = parent
        return ViewHolder(
            ItemDishLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]
        Glide.with(parent)
            .load(dish.image)
            .into(holder.ivDishImage)
        holder.tvTitle.text = dish.title
    }

    override fun getItemCount(): Int = dishes.size

    @SuppressLint("NotifyDataSetChanged")
    fun dishesList(list: List<FavDish>) {
        dishes = list
        notifyDataSetChanged()
    }

    class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
    }
}