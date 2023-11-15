package app.fukushima.haruka.dreamcompass.EnterGoal

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import app.fukushima.haruka.dreamcompass.R
import com.google.firebase.firestore.FirebaseFirestore

data class EnterFinallyGoal(val name: String){

////    private lateinit var editwhatgoal: EditText
////    private lateinit var buttonOK: Button
//    private val db = FirebaseFirestore.getInstance()
//    val pref: SharedPreferences = requireActivity().getSharedPreferences("USER_ID", Context.MODE_PRIVATE)
//
//    val userId = pref.getString("USER_ID","hoge")?:"hoge"
//
//    val query = db.collection("users").document(userId).collection("finally goal")
//
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.enter_finally_goal, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        editwhatgoal = view.findViewById(R.id.editwhatgoal)
//        buttonOK = view.findViewById(R.id.buttonOK)
//
//        buttonOK.setOnClickListener {
//            saveFinalGoalToFirestore()
//        }
//    }

}