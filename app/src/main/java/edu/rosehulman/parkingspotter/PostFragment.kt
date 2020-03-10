package edu.rosehulman.parkingspotter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_post.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_UID = "uid"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PostFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostFragment : Fragment(){

    private var listener: OnFragmentInteractionListener? = null
    private var tokenList = ArrayList<Token>()

    private var uid : String? = null
    private val tokenRef = FirebaseFirestore.getInstance().collection("Tokens")

    init{
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString(ARG_UID)!!
        }

    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_post,container,false)
//        view.post_map.setImageResource(R.drawable.map)

        var selectButton = view.findViewById<Button>(R.id.selectLotButton)

        selectButton.setOnClickListener{
            val builder = androidx.appcompat.app.AlertDialog.Builder(this.context!!)
            builder.setItems(
                resources.getStringArray(R.array.parklot_array))
            { _, which ->
                when (which) {
                    0 -> {
                        "Speed Main Lot"
                        listener!!.onFragmentInteraction(11)
                    }
                    1 -> {
                        "Percopo Small Lot"
                        listener!!.onFragmentInteraction(12)
                    }
                    2 -> {
                        "Percopo Main Lot"
                        listener!!.onFragmentInteraction(13)
                    }
                    3 -> {
                        "Cook Lot"
                        listener!!.onFragmentInteraction(14)
                    }
                    4 -> {
                        "SRC West Lot"
                        listener!!.onFragmentInteraction(15)
                    }
                    5 -> {
                        "Lower Moench Lot"
                        listener!!.onFragmentInteraction(16)
                    }
                    else -> {
                        "None"
                    }
                }
            }
            builder.create().show()
        }


        tokenRef.whereEqualTo("uid",uid).get().addOnCompleteListener{ task ->
            if(task.isSuccessful){
                view.post_token.setText("My current tokens: ".plus(task.result!!.size().toString()))
            }
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
        fun onFragmentInteraction(flag: Int)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PostFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(uid: String) =
            PostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_UID, uid)
                }
            }
    }
}
