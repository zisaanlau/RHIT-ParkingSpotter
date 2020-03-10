package edu.rosehulman.parkingspotter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_login_page.*
import kotlinx.android.synthetic.main.activity_register_page.*

class RegisterPage : AppCompatActivity() {
    lateinit var authListener: FirebaseAuth.AuthStateListener
    val auth = FirebaseAuth.getInstance()
    private var userRef = FirebaseFirestore.getInstance().collection("Users")
    private var userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)
        supportActionBar!!.hide()

        userRef
            .addSnapshotListener { snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
                if (exception != null) {
                }
                for (docChange in snapshot!!.documentChanges) {
                    val user = User.fromSnapshot(docChange.document)
                    when (docChange.type) {
                        DocumentChange.Type.ADDED -> {
                            userList.add(0, user)
//                        notifyItemInserted(0)
                        }

                    }
                }
            }

        var button: Button = findViewById(R.id.registeruser)
        button.setOnClickListener() {
            val username = registerusername.text;
            val password = registerpassword.text;
            val repassword = reregisterpassword.text
            if (password.toString() == "" || username.toString() == "") {
                Toast.makeText(
                    this,
                    "Please enter Receiver Name/Token num!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (repassword.toString() != password.toString()) {
                Toast.makeText(
                    this,
                    "Passwords don't match!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                register(username.toString(), password.toString())
            }

        }
    }

    fun register(username: String, password: String) {
        auth.createUserWithEmailAndPassword(username, password).addOnSuccessListener { authResult ->
            userRef.add(User(authResult.user!!.uid, authResult.user!!.email!!))
                .addOnCompleteListener {
                    val myIntent = Intent(this, ContentHolder::class.java)
                    myIntent.putExtra("uid", authResult.user!!.uid);
                    myIntent.putExtra("email", authResult.user!!.email);
                    startActivity(myIntent)
                }

        }.addOnFailureListener {
            Toast.makeText(this, "Register error!", Toast.LENGTH_SHORT).show()
        }
    }
}
