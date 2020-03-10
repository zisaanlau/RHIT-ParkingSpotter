package edu.rosehulman.parkingspotter

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

class Spot

constructor(var column: String = " " , var row: String = " ", var uid : String = " "){
    @get: Exclude
    var id = ""

    companion object{
        fun fromSnapshot(snapshot: DocumentSnapshot):Spot{
            val spot = snapshot.toObject(Spot::class.java)!!
            spot.id = snapshot.id
            return spot
        }
    }

}

