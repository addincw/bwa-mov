package com.addincendekia.bwa_mov.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.addincendekia.bwa_mov.main.MainActivity
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.models.User
import com.addincendekia.bwa_mov.utils.UserPreferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_signin.*
import java.io.IOException


class SigninActivity : AppCompatActivity() {

    private lateinit var userPref: UserPreferences
    private lateinit var fieldUsername: String
    private lateinit var fieldPassword: String

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        userPref = UserPreferences(this)

        if(userPref.getValue("is_login").equals("1")) {
            finishAffinity()
            startActivity(Intent(this@SigninActivity, MainActivity::class.java))
        }

        btn_signin.setOnClickListener {
            fieldUsername = field_username.text.toString()
            fieldPassword = field_password.text.toString()
            
           
            if(_validationField()) {
                btn_signin.apply {
                    text = "loading..."
                    isEnabled = false
                }
                _requestSignin(fieldUsername, fieldPassword)
            }
        }
        btn_signup.setOnClickListener {
            startActivity(Intent(this@SigninActivity, SignupActivity::class.java))
        }
    }

    private fun _requestSignin(fieldUsername: String, fieldPassword: String) {
        var fbDB = FirebaseDatabase.getInstance()
        var fbDBUserRef = fbDB.getReference("User")

        fbDBUserRef.child(fieldUsername).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(err: DatabaseError) {
                btn_signin.setText(R.string.signin_action_signin)
                Toast.makeText(this@SigninActivity, err.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(data: DataSnapshot) {
                var errMessage = ""

                var user = data.getValue(User::class.java)

                if (user == null) {
                    errMessage = "user not found"
                }else if(!user.password.equals(fieldPassword)) {
                    errMessage = "password does'nt match"
                }

                if (errMessage != "") {
                    btn_signin.setText(R.string.signin_action_signin)
                    btn_signin.apply { isEnabled = true }
                    Toast.makeText(this@SigninActivity, errMessage, Toast.LENGTH_LONG).show()
                    return
                }

                btn_signin.setText(R.string.signin_action_signin)
                btn_signin.apply { isEnabled = true }

                Toast.makeText(this@SigninActivity, "login success", Toast.LENGTH_LONG).show()

                if(_storeUserData(user)) {
                    finishAffinity()
                    startActivity(Intent(this@SigninActivity, MainActivity::class.java))
                }else {
                    Toast.makeText(this@SigninActivity, "terjadi kesalahan dalam menyimpan data login", Toast.LENGTH_LONG).show()
                    return
                }
            }

        })
    }

    private fun _storeUserData(user: User?): Boolean {
        try {
            userPref.setValue("username", user?.username.toString())
            userPref.setValue("password", user?.password.toString())
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
        
        return !isError
    }
}
