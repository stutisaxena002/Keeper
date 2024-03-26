package com.example.keeper

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates


class HomeFragment : Fragment(){
    //variables initialised
    private lateinit var t2: TextView
    private lateinit var c1t2: TextView
    private lateinit var c2t2: TextView
    private lateinit var c3t2: TextView
    private lateinit var c4: CardView
    private lateinit var c5: CardView
    private var inHandTotal by Delegates.notNull<Int>()
    private var inDigitalTotal by Delegates.notNull<Int>()
    private var inTotalTotal by Delegates.notNull<Int>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //xml layout
        val fragmentView=inflater.inflate(com.example.keeper.R.layout.home_fragment, container, false)

        //fields granted
        t2 = fragmentView.findViewById(com.example.keeper.R.id.t2)
        c1t2 = fragmentView.findViewById(com.example.keeper.R.id.c1t2)
        c2t2 = fragmentView.findViewById(com.example.keeper.R.id.c2t2)
        c3t2 = fragmentView.findViewById(com.example.keeper.R.id.c3t2)
        c4 = fragmentView.findViewById(com.example.keeper.R.id.c4)
        c5 = fragmentView.findViewById(com.example.keeper.R.id.c5)

        //data provided by firebase
        val cuser = Firebase.auth.currentUser?.uid
        val db = Firebase.firestore
        db.collection("Users").document(cuser.toString()).get().addOnSuccessListener {
            t2.text = it.get("name").toString()
            c1t2.text=it.get("inHand").toString()
            c2t2.text=it.get("digital").toString()
            c3t2.text= (Integer.valueOf(c1t2.text.toString()) + Integer.valueOf(c2t2.text.toString())).toString()
            //calculation variables
            inHandTotal=Integer.valueOf(c1t2.text.toString())
            inDigitalTotal=Integer.valueOf(c2t2.text.toString())
            inTotalTotal=inDigitalTotal+inDigitalTotal
        }

        //dialog box for credit entry
        c4.setOnClickListener(View.OnClickListener {
            showCreditBox()
        })

        //dialog box for credit entry
        c5.setOnClickListener{
            showDebitBox()
        }
        return fragmentView
    }

    //credit dialog box functioning
    private fun showCreditBox(){

        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.layout, null)

        //AlertDialogBuilder
        val messageBoxBuilder = AlertDialog.Builder(activity).setView(messageBoxView)

        //show dialog
        val  messageBoxInstance = messageBoxBuilder.show()

        //fields credit dialog
        var amountCredit: EditText =messageBoxView.findViewById(R.id.amountCredit)
        var titleCredit: EditText =messageBoxView.findViewById(R.id.titleCredit)
        var ownerCredit: EditText =messageBoxView.findViewById(R.id.ownerCredit)
        var btnCredit: Button =messageBoxView.findViewById(R.id.btnCredit)
        val inhandSwitch: Switch =messageBoxView.findViewById(R.id.inhandSwitch)
        val digitalSwitch: Switch =messageBoxView.findViewById(R.id.digitalSwitch)
        var motionSwitch: String? =null

        //if cash
        if (inhandSwitch.isChecked){
            digitalSwitch.isClickable=false
            digitalSwitch.isEnabled=false
            motionSwitch="inhand"
            //inHandTotal += Integer.valueOf(amountCredit.text.toString())
            //inTotalTotal+= Integer.valueOf(amountCredit.text.toString())
        }

        //if online
        if (digitalSwitch.isChecked){
            inhandSwitch.isClickable=false
            inhandSwitch.isEnabled=false
            motionSwitch="digital"
            // inDigitalTotal += Integer.valueOf(amountCredit.text.toString())
            //inTotalTotal += Integer.valueOf(amountCredit.text.toString())
        }

        //data added in firebase for credit
        btnCredit.setOnClickListener{ 
            val db = Firebase.firestore
            val cuser = Firebase.auth.currentUser?.uid
            val mapUser = hashMapOf(
                "amount" to amountCredit.text.toString(),
                "title" to titleCredit.text.toString(),
                "owner" to ownerCredit.text.toString(),
                "userId" to cuser,
                "mode" to motionSwitch.toString(),
            )
            if (cuser != null) {
                db.collection("Credits").add(mapUser)
                .addOnSuccessListener { Toast.makeText(context,"DocumentSnapshot successfully written!", Toast.LENGTH_SHORT).show()}
                .addOnFailureListener { Toast.makeText(context,"failure data entry", Toast.LENGTH_SHORT).show() }

            //close dialog upon transaction
            messageBoxInstance.dismiss()
            }
        }

        //close dialog upon clicking outside
        messageBoxView.setOnClickListener(){
            messageBoxInstance.dismiss()
        }
    }

    //functioning of debit dialog
    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    private fun showDebitBox(){

        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.layout_debit, null)

        //AlertDialogBuilder
        val messageBoxBuilder = AlertDialog.Builder(activity).setView(messageBoxView)

        //show dialog
        val  messageBoxInstance = messageBoxBuilder.show()

        //variables for debit dialog
        val amountDebit: EditText =messageBoxView.findViewById(R.id.amountDebit)
        val titleDebit: EditText =messageBoxView.findViewById(R.id.titleDebit)
        val ownerDebit: EditText =messageBoxView.findViewById(R.id.ownerDebit)
        val btnDebit: Button =messageBoxView.findViewById(R.id.btnDebit)
        val inhandSwitchDebit: Switch =messageBoxView.findViewById(R.id.inhandSwitchDebit)
        val digitalSwitchDebit: Switch =messageBoxView.findViewById(R.id.digitalSwitchDebit)
        var motionSwitch: String? =null

        //if cash
        if (inhandSwitchDebit.isChecked){
            digitalSwitchDebit.isClickable=false
            digitalSwitchDebit.isEnabled=false
            motionSwitch="inhand"
            //inHandTotal -= Integer.valueOf(amountDebit.text.toString())
            //inTotalTotal -= Integer.valueOf(amountDebit.text.toString())
        }

        //if online
        if (digitalSwitchDebit.isChecked){
            inhandSwitchDebit.isClickable=false
            inhandSwitchDebit.isEnabled=false
            motionSwitch="digital"
            //inDigitalTotal -= Integer.valueOf(amountDebit.text.toString())
            //inTotalTotal -= Integer.valueOf(amountDebit.text.toString())
        }

        //data into firebase for debit
        btnDebit.setOnClickListener{
            val db = Firebase.firestore
            val cuser = Firebase.auth.currentUser?.uid
            val mapUser = hashMapOf(
                "amount" to amountDebit.text.toString(),
                "title" to titleDebit.text.toString(),
                "owner" to ownerDebit.text.toString(),
                "userId" to cuser,
                "mode" to motionSwitch.toString(),
            )
            if (cuser != null) {
                db.collection("Debits").add(mapUser)
                    .addOnSuccessListener { Toast.makeText(context,"DocumentSnapshot successfully written!", Toast.LENGTH_SHORT).show(); }
                    .addOnFailureListener {Toast.makeText(context,"failure data entry", Toast.LENGTH_SHORT).show(); }

                //close dialog online confirm transaction
                messageBoxInstance.dismiss()
            }
        }

        //close dialog upon click outside
        messageBoxView.setOnClickListener(){
            messageBoxInstance.dismiss()
        }
    }


}