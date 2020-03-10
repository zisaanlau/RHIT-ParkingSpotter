package edu.rosehulman.parkingspotter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.parking_lot_fragment.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_UID = "uid"
private const val ARG_EMAIL = "email"
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ConfirmFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ConfirmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfirmFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var uid: String? = null
    private var email: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private val tokenRef = FirebaseFirestore.getInstance().collection("Tokens")
    private var tokenList = ArrayList<Token>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString(ARG_UID)
            email = it.getString(ARG_EMAIL)
        }

        tokenRef.addSnapshotListener{ snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
            if(exception != null){
            }
            for (docChange in snapshot!!.documentChanges){
                val token = Token.fromSnapshot(docChange.document)
                when(docChange.type){
                    DocumentChange.Type.ADDED -> {
                        tokenList.add(0,token)
                    }
                }
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_confirm, container, false)

        val confirmButton: Button = view.findViewById(R.id.confirm_slot)
        confirmButton.setOnClickListener{
            listener!!.onFragmentInteraction(4);

        }

        val denyButton: Button = view.findViewById(R.id.deny_slot)
        denyButton.setOnClickListener{
            FirebaseFirestore.getInstance().collection("Tokens").add(Token(uid!!, email!!))

            listener!!.onFragmentInteraction(4);

        }

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(flag: Int) {
        listener?.onFragmentInteraction(flag)
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(flag:Int)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConfirmFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(uid: String, email:String) =
                ConfirmFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_UID, uid)
                        putString(ARG_EMAIL, email)
                    }
                }
    }
}
