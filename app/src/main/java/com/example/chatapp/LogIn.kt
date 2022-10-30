package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LogIn : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogIn: Button
    private lateinit var btnSignUp: Button
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        mAuth=FirebaseAuth.getInstance()
        edtEmail = findViewById<EditText>(R.id.edt_email)
        edtPassword = findViewById<EditText>(R.id.edt_pass)
        btnLogIn = findViewById<Button>(R.id.btn_login)
        btnSignUp = findViewById<Button>(R.id.btn_Sup)

        btnSignUp.setOnClickListener {
            val intent= Intent(this,SignUp::class.java)
            startActivity(intent)

        }

btnLogIn.setOnClickListener {
    val email=edtEmail.text.toString()
    val pass=edtPassword.text.toString()
    login(email,pass);

}

    }
    private fun login(email:String, pass: String){
//logic of login user
        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent= Intent(this@LogIn, MainActivity::class.java)
                   finish()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(this@LogIn, "Username Password wrong.",Toast.LENGTH_SHORT).show()

                }
            }
    }
}