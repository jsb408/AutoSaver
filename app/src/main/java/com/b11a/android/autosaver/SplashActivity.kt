package com.b11a.android.autosaver

import android.Manifest.permission.*
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.b11a.android.autosaver.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkPermission(ACCESS_FINE_LOCATION, SEND_SMS, CALL_PHONE)
    }

    private fun checkPermission(vararg permissions: String) {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.containsValue(false)) finish()
            else {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(
                        Intent(this,
                            if(kPrefs(this).getBoolean("autoLogin", false)
                                && kPrefs(this).getString("userToken", "")?.isNotEmpty() == true) {
                                MainActivity::class.java
                            } else {
                                LoginActivity::class.java
                            })
                    )
                    finish()
                }, 1500)
            }
        }.launch(permissions)
    }
}