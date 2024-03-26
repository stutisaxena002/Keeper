package com.example.keeper

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        mAuth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({

            //val i: Intent = if (mAuth!!.currentUser != null) {
            // Intent(this@MainActivity, Home::class.java)
            //} else {
            val i=Intent(this@MainActivity, login::class.java)
            //} // the current activity will get finished.

            startActivity(i)
            finish()

        }, 3000)
    }
}