package com.example.keeper

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Objects

class signup : AppCompatActivity() {

    //initialise variables
    private lateinit var signupEmail: EditText
    private lateinit var signupPassword: EditText
    private lateinit var signupName: EditText
    private lateinit var signupInHand: EditText
    private lateinit var signupDigital: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var btnSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signup)

        //variables to fields
        signupEmail=findViewById(R.id.signupEmail)
        signupPassword=findViewById(R.id.signupPassword)
        signupName=findViewById(R.id.signupName)
        signupInHand=findViewById(R.id.signupInhand)
        signupDigital=findViewById(R.id.signupDigitalamount)
        btnSignup=findViewById(R.id.btnSignup)
        auth = Firebase.auth
        val db = Firebase.firestore

        //enter details into firebase for sign up
        btnSignup.setOnClickListener{

            //check if the text any field is empty
            if ((signupEmail.text.toString().isEmpty())||(signupPassword.text.toString().isEmpty())||(signupName.text.toString().isEmpty()))
            {
                Toast.makeText(baseContext,"Enter Credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //add details into firebase
            else
            {
                auth.createUserWithEmailAndPassword(signupEmail.text.toString(),signupPassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val cuser = Firebase.auth.currentUser?.uid
                            Toast.makeText(baseContext,"createUserWithEmail:success", Toast.LENGTH_SHORT).show()
                            val mapUser = hashMapOf(
                                "email" to signupEmail.text.toString(),
                                "password" to signupPassword.text.toString(),
                                "name" to signupName.text.toString(),
                                "inHand" to signupInHand.text.toString(),
                                "digital" to signupDigital.text.toString(),
                            )
                            if (cuser != null)
                            {
                            Toast.makeText(baseContext,cuser, Toast.LENGTH_SHORT).show()
                            db.collection("Users").document(cuser).set(mapUser)
                                .addOnSuccessListener { Toast.makeText(baseContext,"DocumentSnapshot successfully written!", Toast.LENGTH_SHORT).show()}
                                .addOnFailureListener { Toast.makeText(baseContext,"failure data entry", Toast.LENGTH_SHORT).show() }
                            }

                            //move from signup to homepage
                            val intent = Intent(this@signup,Home::class.java)
                            cuser?.let {
                                val uid = it
                                Toast.makeText(baseContext, uid, Toast.LENGTH_SHORT).show()
                                intent.putExtra("uid", uid)
                                startActivity(intent)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }

                        //not able to register user
                        }
                        else
                        {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}