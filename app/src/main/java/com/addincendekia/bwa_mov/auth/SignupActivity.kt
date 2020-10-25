package com.addincendekia.bwa_mov.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.models.User
import com.addincendekia.bwa_mov.utils.UserPreferences
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_signup.*
import java.io.IOException

class SignupActivity : AppCompatActivity() {

    private lateinit var userPref: UserPreferences
    private lateinit var fieldUsername: String
    private lateinit var fieldPassword: String
    private lateinit var fieldNama: String
    private lateinit var fieldEmail: String

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        userPref = UserPreferences(this)
        if (!userPref.getValue("username").equals("")) {
            field_username.setText(userPref.getValue("username"))
//            field_password.setText(userPref.getValue("password"))
            field_nama.setText(userPref.getValue("nama"))
            field_email.setText(userPref.getValue("email"))
        }

        btn_next.setOnClickListener {
            fieldUsername = field_username.text.toString()
            fieldPassword = field_password.text.toString()
            fieldNama = field_nama.text.toString()
            fieldEmail = field_email.text.toString()

            if(!_validationField()) return@setOnClickListener

            btn_next.setText("loading...")
            btn_next.apply { isEnabled = false }

            var newUser = User()
            newUser.username = fieldUsername
            newUser.password = fieldPassword
            newUser.nama = fieldNama
            newUser.email = fieldEmail
            newUser.saldo = "0"

            if (userPref.getValue("username").equals("")) {
                _requestSignup(newUser)
            }else{
                btn_next.setText(R.string.signup_action_next)
                btn_next.apply { isEnabled = true }
                startActivity(Intent(this@SignupActivity, SignupPhotoActivity::class.java))
            }
        }
        btn_back.setOnClickListener{
            finish()
        }
    }

    private fun _requestSignup(newUser: User) {
        var initfbListener = 0
        var fbDB = FirebaseDatabase.getInstance()
        var fbDBUserRef = fbDB.getReference("User")

        fbDBUserRef.child(fieldUsername).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(err: DatabaseError) {
                btn_next.setText(R.string.signup_action_next)
                Toast.makeText(this@SignupActivity, err.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(data: DataSnapshot) {
                var user = data.getValue(User::class.java)

                if (initfbListener <= 0 && user != null) {
                    btn_next.setText(R.string.signup_action_next)
                    btn_next.apply { isEnabled = true }

                    //Log.d("[signup-failed]", "username sudah digunakan user lain")
                    Toast.makeText(this@SignupActivity, "username sudah digunakan user lain", Toast.LENGTH_LONG).show()
                    return
                }

                fbDBUserRef.child(fieldUsername).setValue(newUser)
                initfbListener++

                btn_next.setText(R.string.signup_action_next)
                btn_next.apply { isEnabled = true }

                //.d("[signup-success]", "registrasi success, username dan password dapat digunakan")
                Toast.makeText(this@SignupActivity, "registrasi success, username dan password dapat digunakan", Toast.LENGTH_LONG).show()

                if(_storeUserData(newUser)) {
                    startActivity(Intent(this@SignupActivity, SignupPhotoActivity::class.java))
                }else {
                    Toast.makeText(this@SignupActivity, "terjadi kesalahan dalam menyimpan data registrasi", Toast.LENGTH_LONG).show()
                    return
                }
            }

        })
    }

    private fun _storeUserData(user: User?): Boolean {
        try {
            userPref.setValue("username", user?.username.toString())
            userPref.setValue("nama", user?.nama.toString())
            userPref.setValue("email", user?.email.toString())
            userPref.setValue("url", user?.url.toString())
            userPref.setValue("saldo", user?.saldo.toString())
            userPref.setValue("is_login", "1")

            return true
        }catch (e: IOException){
            return false
        }
    }

    private fun _validationField(): Boolean {
        var isError = false

        if(fieldUsername.equals("")) {
            isError = true
            field_username.error = "username cannot be null"
            field_username.requestFocus()
        }

        if(fieldPassword.equals("")) {
            isError = true
            field_password.error = "password cannot be null"
            field_password.requestFocus()
        }

        if(fieldNama.equals("")) {
            isError = true
            field_nama.error = "nama cannot be null"
            field_nama.requestFocus()
        }

        if(fieldEmail.equals("")) {
            isError = true
            field_email.error = "email cannot be null"
            field_email.requestFocus()
        }

        return !isError
    }
}
