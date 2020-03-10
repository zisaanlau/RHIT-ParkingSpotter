package edu.rosehulman.parkingspotter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_page.*

class LoginPage : AppCompatActivity() {

    lateinit var authListener: FirebaseAuth.AuthStateListener
    val auth = FirebaseAuth.getInstance()
    lateinit var uid : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        supportActionBar!!.hide()
        authListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
            val user = auth.currentUser
            if (user == null) {

            }
            else {
                uid = user.uid
                val myIntent = Intent(this, ContentHolder::class.java)
                myIntent.putExtra("uid", user.uid);
                myIntent.putExtra("email", user.email);
                startActivity(myIntent)
            }
        }

        var button: Button =  findViewById(R.id.register)
        button.setOnClickListener(){
            val myIntent = Intent(this, RegisterPage::class.java)
            startActivity(myIntent)
        }

        var loginButton: Button =  findViewById(R.id.login)

        loginButton.setOnClickListener(){
            val username = username.text;
            val password = password.text;

            login(username.toString(),password.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authListener)

    }

    fun login(username:String, password: String){

        if(username == "" || password == ""){
            Toast.makeText(this,"Please enter username/password!", Toast.LENGTH_SHORT).show()
        }else{
            auth.signInWithEmailAndPassword(username, password).addOnSuccessListener {

                    uid = it.user!!.uid
                    val myIntent = Intent(this, ContentHolder::class.java)
                    myIntent.putExtra("uid", it.user!!.uid);
                    myIntent.putExtra("email", it.user!!.email);
                    startActivity(myIntent)

            }.addOnFailureListener{
                Toast.makeText(this,"Wrong email/password combination!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
