package com.it.openmechanic.ui.activities.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.it.openmechanic.R
import com.it.openmechanic.ui.activities.auth.NameInputActivity
import com.it.openmechanic.utils.newIntent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return newIntent<MainActivity>(context)
        }
    }
}