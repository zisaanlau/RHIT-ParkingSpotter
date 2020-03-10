package edu.rosehulman.parkingspotter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_post.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_UID = "uid"


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private val tokenRef = FirebaseFirestore.getInstance().collection("Tokens")
    private var listener: OnFragmentInteractionListener? = null
    private var uid : String = ""
    val auth = FirebaseAuth.getInstance()
    init{

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString(ARG_UID)!!
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_home, container, false)
        var getbutton:Button = view.findViewById(R.id.get);
        getbutton.setOnClickListener{
            listener!!.onFragmentInteraction(1);
        }
        var postbutton:Button = view.findViewById(R.id.post);
        postbutton.setOnClickListener{
            listener!!.onFragmentInteraction(2);
        }
        var transferbutton:Button = view.findViewById(R.id.transfer)
        transferbutton.setOnClickListener{
            listener!!.onFragmentInteraction(3);
        }

        view.log_out.setOnClickListener {
            val builder = AlertDialog.Builder(this.context!!)
            builder.setMessage("Are you sure you want to log out?")
            builder.setPositiveButton("Yes") { dialog, which ->
                listener!!.onFragmentInteraction(-1)
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                false
            }
            builder.create().show()
        }

        tokenRef.whereEqualTo("uid",uid).get().addOnCompleteListener{ task ->
            if(task.isSuccessful){
                view.home_token.setText("My current tokens: ".plus(task.result!!.size().toString()))
            }
        }

        return view;
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(flag: Int);
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(uid: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_UID, uid)
                }
            }
    }


}
