package edu.rosehulman.parkingspotter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

class ContentHolder : AppCompatActivity(), HomeFragment.OnFragmentInteractionListener,
    PostFragment.OnFragmentInteractionListener, GetFragment.OnFragmentInteractionListener, ParkingLotFragment.OnFragmentInteractionListener,
        TransferFragment.OnFragmentInteractionListener,
        ConfirmFragment.OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener
{
    override fun onMapZoom(map: Int) {
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,
            android.R.anim.fade_out)
        ft.replace(R.id.main_content, MapFragment.newInstance(map)).addToBackStack("map")
        ft.commit()
        ft.addToBackStack("map");
    }

    lateinit var uid : String
    lateinit var email : String
    val auth = FirebaseAuth.getInstance()

    override fun onFragmentInteraction(flag: Int) {
        if(flag == -1) {
                auth.signOut()
                val myIntent = Intent(this, MainActivity::class.java)
                startActivity(myIntent)
        }
        else if(flag == 1) {
            val ft = supportFragmentManager.beginTransaction()
            ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,
                android.R.anim.fade_out)
            ft.replace(R.id.main_content, PostFragment.newInstance(uid)).addToBackStack(null)
            ft.commit()
            ft.addToBackStack(null);
        }else if(flag == 2){
            val ft = supportFragmentManager.beginTransaction()
            ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,
                android.R.anim.fade_out)
            ft.replace(R.id.main_content, GetFragment.newInstance(uid)).addToBackStack(null)
            ft.commit()
            ft.addToBackStack(null);
        }
        else if(flag == 3){
            val ft = supportFragmentManager.beginTransaction()
            ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,
                android.R.anim.fade_out)
            ft.replace(R.id.main_content, TransferFragment.newInstance(uid, email)).addToBackStack(null)
            ft.commit()
            ft.addToBackStack(null);
        }
        else if(flag == 4){
            val ft = supportFragmentManager.beginTransaction()
            ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,
                android.R.anim.fade_out)
            ft.replace(R.id.main_content, HomeFragment.newInstance(uid)).addToBackStack(null)
            ft.commit()
            ft.addToBackStack(null);
        }else if(flag == 5){
            val ft = supportFragmentManager.beginTransaction()
            ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,
                    android.R.anim.fade_out)
            ft.replace(R.id.main_content, ConfirmFragment.newInstance(uid,email)).addToBackStack("confirm")
            ft.commit()
            ft.addToBackStack("confirm");
        }

        else{
            val ft = supportFragmentManager.beginTransaction()
            ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,
                android.R.anim.fade_out)
            ft.replace(R.id.main_content, ParkingLotFragment.newInstance(uid, email, flag)).addToBackStack(null)
            ft.commit()
            ft.addToBackStack(null);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_holder)
        uid = getIntent().getStringExtra("uid")
        email = getIntent().getStringExtra("email")
        supportActionBar!!.hide()

        if(uid != null){
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.main_content, HomeFragment.newInstance(uid))
            ft.commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {

            else -> super.onOptionsItemSelected(item)
        }
    }

}
