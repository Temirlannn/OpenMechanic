package com.it.openmechanic.ui.activities.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.it.openmechanic.R
import com.it.openmechanic.databinding.ActivityMainBinding
import com.it.openmechanic.ui.base.BaseActivity
import com.it.openmechanic.ui.profile.ProfileViewModel
import com.it.openmechanic.utils.newIntent


class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()
    private val profileVM: ProfileViewModel by viewModels()

    override fun setupView() {
        profileVM.getProfile()
    }

    override fun bindViewModel() {
        viewModel.progress.observe(this) {
            if (it) showProgressBar() else hideProgressBar()
        }
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return newIntent<MainActivity>(context)
        }
    }
}