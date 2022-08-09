package com.it.openmechanic.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.textfield.TextInputEditText
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

private var toast: Toast? = null

fun FragmentActivity.toast(text: String) {
    toast(this, text)
}

fun Fragment.toast(text: String) {
    toast(requireContext(), text)
}

fun Activity.toast(text: String) {
    toast(this, text)
}

fun String.phoneNumberFormat(region: String = "KG", util: PhoneNumberUtil): String {
    return util.format(util.parse(this, region), PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
}

fun String.isPhoneNumberValid(region: String = "KG", util: PhoneNumberUtil): Boolean {
    val phoneNumberFormatter = util.parse(this, region)
    return util.isValidNumber(phoneNumberFormatter)
}


private fun toast(context: Context, text: String) {
    toast?.cancel()
    toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
    toast?.show()
}

fun Activity.hideKeyboard() {
    val imm: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view: View? = currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    val imm: InputMethodManager =
        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    // Find the currently focused view, so we can grab the correct window token from it.
    var view: View? = requireActivity().currentFocus
    // If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(requireActivity())
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
/**
 * View extensions
 */
fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.visible(visible: Boolean, withGone: Boolean = true) {
    visibility = when {
        visible -> View.VISIBLE
        withGone -> View.GONE
        else -> View.INVISIBLE
    }
}

fun Context.isNetworkAvailable(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities = manager?.activeNetwork ?: return false
        val active = manager.getNetworkCapabilities(capabilities) ?: return false
        return when {
            active.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            active.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            active.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        return when (manager?.activeNetworkInfo?.type) {
            ConnectivityManager.TYPE_WIFI,
            ConnectivityManager.TYPE_MOBILE,
            ConnectivityManager.TYPE_ETHERNET -> manager.activeNetworkInfo?.isConnected == true
            else -> false
        }
    }
}

fun Context.enableNotifications() {
    Intent().apply {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            }
            else -> {
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtra("app_package", packageName)
                putExtra("app_uid", applicationInfo?.uid)
            }
        }
    }.also { startActivity(it) }
}



fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}
