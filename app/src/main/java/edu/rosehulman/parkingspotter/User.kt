package edu.rosehulman.parkingspotter

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

class User
constructor(var uid: String = " " , var userEmail : String = " "){
    @get: Exclude
    var id = ""

    companion object{
        fun fromSnapshot(snapshot: DocumentSnapshot):User{
            val user = snapshot.toObject(User::class.java)!!
            user.id = snapshot.id
            return user
        }
    }

}

