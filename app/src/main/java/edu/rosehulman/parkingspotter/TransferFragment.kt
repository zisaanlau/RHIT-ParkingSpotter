package edu.rosehulman.parkingspotter

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_transfer.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_UID = "uid"
private const val ARG_EMAIL = "email"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [transferFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [transferFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransferFragment : Fragment() {
    val auth = FirebaseAuth.getInstance()
    private var listener: OnFragmentInteractionListener? = null
    private var uid: String? = null
    private var tokenRef = FirebaseFirestore.getInstance().collection("Tokens")
    private var tokenList = ArrayList<Token>()

    private var userRef = FirebaseFirestore.getInstance().collection("Users")
    private var userList = ArrayList<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString(ARG_UID)
        }
        tokenRef
            .whereEqualTo("uid", uid)
            .addSnapshotListener { snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
                if (exception != null) {
                }
                for (docChange in snapshot!!.documentChanges) {
                    val token = Token.fromSnapshot(docChange.document)
                    when (docChange.type) {
                        DocumentChange.Type.ADDED -> {
                            tokenList.add(0, token)
                        }
                        DocumentChange.Type.REMOVED -> {
                            tokenList.removeAt(0)
                        }
                    }
                }
            }
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transfer, container, false)

        var receiverEmail = view.receiver_email
        var rereceiverEmail = view.rereceiver_email
        var numToken = view.transfer_token_num


        view.findViewById<Button>(R.id.transfer_button).setOnClickListener {
            if (receiverEmail.text.toString() == "" || numToken.text.toString() == "") {
                Toast.makeText(
                    this.context,
                    "Please enter Receiver Name/Token num!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (rereceiverEmail.text.toString() != receiverEmail.text.toString()) {
                Toast.makeText(
                    this.context,
                    "Receiver Emails don't match!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                transfer(receiverEmail.text.toString(), numToken.text.toString())
            }

        }

        return view
    }

    fun transfer(receiverEmail: String, numToken: String) {
        var count = 0
        if (numToken.toInt() > tokenList.size) {
            Toast.makeText(
                this.context,
                "You don't have enough tokens to transfer!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (!userList.any { it ->
                it.userEmail == receiverEmail
            }) {
            Toast.makeText(this.context, "User does not exist!", Toast.LENGTH_SHORT).show()
        } else {
            val user: User? = userList.find { it.userEmail == receiverEmail }

            while (count != numToken.toInt()) {
                tokenRef.document(tokenList[count].id).set(Token(user!!.uid, receiverEmail))
                count++
            }

            listener!!.onFragmentInteraction(4)
        }

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

        fun onFragmentInteraction(flag: Int)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment transferFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(uid: String, email: String) =
            TransferFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_UID, uid)
                    putString(ARG_EMAIL, email)
                }
            }
    }
}
