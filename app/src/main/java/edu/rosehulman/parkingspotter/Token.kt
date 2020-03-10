package edu.rosehulman.parkingspotter

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

class Token
constructor(var uid : String = " ", var userEmail : String = " "){
    @get: Exclude
    var id = ""
    companion object{
        fun fromSnapshot(snapshot: DocumentSnapshot):Token{
            val token = snapshot.toObject(Token::class.java)!!
            token.id = snapshot.id
            return token
        }
    }

}

