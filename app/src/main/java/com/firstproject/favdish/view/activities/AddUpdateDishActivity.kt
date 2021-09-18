package com.firstproject.favdish.view.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.firstproject.favdish.R
import com.firstproject.favdish.databinding.ActivityAddUpdateDishBinding
import com.firstproject.favdish.databinding.DialogCustomImageSelectionBinding

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddUpdateDishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        binding.ivAddDishImage.setOnClickListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarAddUpdateDish)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddUpdateDish.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun customImageSelectionDialog() {
        val dialog = Dialog(this)
        val binding = DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {

            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {

            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_add_dish_image -> {
                    customImageSelectionDialog()
                    return
                }
            }
        }
    }
}

