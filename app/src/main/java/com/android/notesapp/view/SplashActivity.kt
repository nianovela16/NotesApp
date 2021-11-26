package com.android.notesapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.notesapp.R
import com.android.notesapp.util.Constants

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val cntPerm = askPermission()

        if(cntPerm == 0) {
            Handler(Looper.myLooper()!!).postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
            }, 3000)
        }
    }


    private fun askPermission(): Int {
        val permissions = ArrayList<String>()
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if(permissions.size > 0)
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(),
                Constants.REQUEST_PERMISSION_CODE)

        return permissions.size
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            Constants.REQUEST_PERMISSION_CODE -> {
                if(grantResults.isNotEmpty()) {
                    var toMain = true
                    for(gr in grantResults) {
                        if(gr == PackageManager.PERMISSION_DENIED)
                            toMain = false
                    }
                    if(toMain)
                        startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            else -> {

            }
        }
    }
}