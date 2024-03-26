package com.example.keeper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class login : AppCompatActivity() {

    //initialise variable
    private lateinit var emailLogin: EditText
    private lateinit var passwordLogin: EditText
    private lateinit var loginSignup: TextView
    private lateinit var login: Button
    private lateinit var forgotPassword: TextView
    private lateinit var auth: FirebaseAuth

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        //values to fields
        emailLogin=findViewById(R.id.loginEmail)
        passwordLogin=findViewById(R.id.loginPassword)
        loginSignup=findViewById(R.id.loginSignup)
        login=findViewById(R.id.btnLogin)
        forgotPassword=findViewById(R.id.forgotPassword)
        auth = Firebase.auth

        //checking in firebase for login
        login.setOnClickListener{

            //check is fields are empty
            if ((emailLogin.text.toString().isEmpty())||(passwordLogin.text.toString().isEmpty()))
            {
                Toast.makeText(baseContext,"Enter Credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //validate users for login
            else
            {
                auth.signInWithEmailAndPassword(emailLogin.text.toString(),passwordLogin.text.toString()).addOnCompleteListener(this)
                {task->
                    if (task.isSuccessful)
                    {
                        Toast.makeText(baseContext,"Login successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,Home::class.java)
                        startActivity(intent)
                    }

                    //if not able to match password
                    else
                    {
                        Toast.makeText(baseContext, "Wrong Credentials", Toast.LENGTH_SHORT,).show()
                    }
                }

            }

        }

        //forgot password module
        forgotPassword.setOnClickListener{

            //check if entered field is not empty
            if ((emailLogin.text.toString().isEmpty())){
                Toast.makeText(baseContext,"Enter Email", Toast.LENGTH_SHORT).show()
            }

            //send mail for retrieving password
            else{
            auth.sendPasswordResetEmail(emailLogin.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext,"Mail Sent", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(baseContext,"Invalid Mail", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //link for signup
        loginSignup.setOnClickListener {
            val intent = Intent(this,signup::class.java)
            startActivity(intent)
        }
    }
}