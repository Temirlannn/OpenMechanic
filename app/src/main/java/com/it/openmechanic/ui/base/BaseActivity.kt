package com.it.openmechanic.ui.base

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.it.openmechanic.R
import com.it.openmechanic.utils.ConnectionStateMonitor
import com.it.openmechanic.utils.SHARED_PREFERENCES_NAME
import com.it.openmechanic.utils.isUIAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity<VB : ViewBinding>(val bindingFactory: (LayoutInflater) -> VB) :
    AppCompatActivity(), ConnectionStateMonitor.OnNetworkAvailableCallbacks, CoroutineScope {

    private val pd: ProgressDialog by lazy {
        ProgressDialog(this).apply {
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            )
            setMessage(context.getString(R.string.loading))
            setCancelable(false)
        }
    }
    val binding: VB by lazy { bindingFactory(layoutInflater) }

    protected val connectionStateMonitor: ConnectionStateMonitor by lazy {
        ConnectionStateMonitor(this, this)
    }

    protected val sharedPrefs: SharedPreferences by lazy {
        getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    }

    private val job by lazy { Job() }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    abstract fun setupView()

    abstract fun bindViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        bindViewModel()
    }

    override fun onPositive() {
        TODO("Not yet implemented")
    }

    override fun onNegative() {
        TODO("Not yet implemented")
    }

    fun showProgressBar() {
        launch {
            if (!isUIAvailable()) {
                return@launch
            }
            pd.show()
        }
    }

    fun hideProgressBar() {
        launch {
            if (!isUIAvailable()) {
                return@launch
            }
            try {
                if (pd.isShowing) {
                    pd.dismiss()
                }
            } catch (e: Exception) {
            }
        }
    }
}
