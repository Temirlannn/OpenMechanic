package com.it.openmechanic.ui.activities.auth

import android.content.Context
import android.content.Intent
import com.it.openmechanic.data.model.Profile
import com.it.openmechanic.data.model.Response.*
import com.it.openmechanic.databinding.ActivityNameInputBinding
import com.it.openmechanic.ui.activities.main.MainActivity
import com.it.openmechanic.ui.base.BaseActivity
import com.it.openmechanic.ui.profile.ProfileViewModel
import com.it.openmechanic.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.regex.Pattern

class NameInputActivity :
    BaseActivity<ActivityNameInputBinding>(ActivityNameInputBinding::inflate) {


    private val profileVM: ProfileViewModel by viewModel()

    private val regex by lazy {
        Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]")
    }

    override fun setupView() = with(binding) {

        nameInput.afterTextChanged {
            btnAuth.isEnabled = it.length > 2

            if (regex.matcher(nameInput.text?.toString() ?: "").find()) {
                errorText.show()
            } else errorText.hide()
        }

        btnAuth.setOnClickListener {
            if (regex.matcher(
                    nameInput.text?.toString()
                        ?: ""
                ).find() || nameInput.text.isNullOrBlank()
            ) {
                errorText.show()
                return@setOnClickListener
            } else errorText.hide()


            val fullName = String.format("%s", nameInput.text?.toString() ?: "")

            val profile = Profile(userName = fullName)
            profileVM.addProfile(profile)
        }
    }

    override fun bindViewModel() {

        profileVM.addProfileResponse.observe(this) {
            when (it) {
                is Success -> {
                    hideProgressBar()
                    startActivity(MainActivity.newInstance(this))
                }
                is Error -> {
                    hideProgressBar()
                    toast(it.message)
                }
                is Loading -> showProgressBar()
            }
        }
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return newIntent<NameInputActivity>(context)
        }
    }
}