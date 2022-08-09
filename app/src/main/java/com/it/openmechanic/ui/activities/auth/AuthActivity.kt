package com.it.openmechanic.ui.activities.auth

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.it.openmechanic.R
import com.it.openmechanic.databinding.ActivityAuthBinding
import com.it.openmechanic.ui.activities.main.MainActivity
import com.it.openmechanic.ui.base.BaseActivity
import com.it.openmechanic.utils.*
import com.santalu.maskara.Mask
import com.santalu.maskara.MaskChangedListener
import com.santalu.maskara.MaskStyle
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : BaseActivity<ActivityAuthBinding>(ActivityAuthBinding::inflate) {

    private val phoneNumberUtils by lazy {
        PhoneNumberUtil.createInstance(this)
    }

    private val authVM: AuthViewModel by viewModel()

    override fun setupView() = with(binding) {
        setUpPhoneMask()
        setCodeMask()

        btnAuth.setOnClickListener {
            if (!authVM.isCodeSend) {
                val phone = getPhone()
                if (phone.isNotEmpty()) {
                    authVM.verifyPhoneNumber(phone, this@AuthActivity)
                }
            } else {
                authVM.confirmCode(codeInput.text.toString().filterNot { it.isWhitespace() })
            }
        }

        sendSmsAgain.setOnClickListener {
            val phone = getPhone()
            if (phone.isNotEmpty()) {
                authVM.resendVerificationCode(phone, this@AuthActivity)
            }
        }

        incorrectNumber.setOnClickListener {
            smsCodeGroup.hide()
            authVM.reset()
            login.show()
            enterPhoneTittle.text = getString(R.string.enter_phone)
        }
    }

    override fun bindViewModel() = with(binding) {

        authVM.codeSendObservable.observe(this@AuthActivity) {
            login.hide()
            smsCodeGroup.show()
            sendSmsAgain.isEnabled = false
            enterPhoneTittle.text = getString(R.string.enter_code)
        }

        authVM.smsErrorObservable.observe(this@AuthActivity) {
            it?.let {
                toast(it)
            }
        }

        authVM.progress.observe(this@AuthActivity) {
            setProgressVisible(it)
        }

        sendSmsAgain.setOnClickListener {
            val phone = getPhone()
            if (phone.isNotEmpty()) {
                authVM.resendVerificationCode(phone, this@AuthActivity)
            }
        }

        authVM.proceedRegistration.observe(this@AuthActivity) {
            startActivity(
                NameInputActivity.newInstance(this@AuthActivity).putExtra("id", it.id)
            )
        }

        authVM.secondsObservable.observe(this@AuthActivity) {
            sendSmsAgain.text = String.format(getString(R.string.send_sms_again_quest), it)
        }

        authVM.resendEnabledObservable.observe(this@AuthActivity) {
            with(sendSmsAgain) {
                if (it) {
                    isEnabled = true
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                    text = getString(R.string.send_sms_again)
                } else {
                    isEnabled = false
                    setTextColor(ContextCompat.getColor(context, R.color.grey_opacity))
                    text = getString(R.string.send_sms_again_quest)
                }
            }
        }

        authVM.isSuccessLogin.observe(this@AuthActivity) { success ->
            val (isSuccess, errorMessage) = success
            when {
                isSuccess -> startActivity(MainActivity.newInstance(applicationContext))
                errorMessage != null -> toast(errorMessage)
            }
        }
    }

    private fun setUpPhoneMask() = with(binding) {
        val mask = Mask(
            value = "+996 (___) __ __ __",
            character = '_',
            style = MaskStyle.PERSISTENT
        )

        val listener = MaskChangedListener(mask)

        login.addTextChangedListener(listener)
        login.requestFocus()
        login.setText(getString(R.string.kg_country_phone_code))

        login.addTextChangedListener {
            btnAuth.isEnabled = it?.length != 0
        }
    }

    private fun setCodeMask() = with(binding) {
        val mask = Mask(
            value = "__ __ __",
            character = '_',
            style = MaskStyle.PERSISTENT
        )
        val listener = MaskChangedListener(mask)

        codeInput.addTextChangedListener(listener)

        codeInput.addTextChangedListener {
            btnAuth.isEnabled = it?.length != 0
        }
    }

    private fun getPhone(): String = with(binding) {
        val phoneNumber =
            login.text.toString().phoneNumberFormat("KG", phoneNumberUtils)
        return if (phoneNumber.isPhoneNumberValid("KG", phoneNumberUtils)) {
            phoneNumber.replace(" ", "").replace("-", "")
        } else {
            Toast.makeText(
                applicationContext,
                getString(R.string.incorrect_format),
                Toast.LENGTH_SHORT
            ).show()
            ""
        }
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return newIntent<AuthActivity>(context)
        }
    }
}