package app.fukushima.haruka.dreamcompass.EnterGoal

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import app.fukushima.haruka.dreamcompass.Goal.Goals
import app.fukushima.haruka.dreamcompass.R
import app.fukushima.haruka.dreamcompass.databinding.EnterFinallyGoalBinding

import app.fukushima.haruka.dreamcompass.databinding.EnterMidtermGoalBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EnterMidtermGoalFragment :Fragment(R.layout.enter_midterm_goal) {
    private var _binding: EnterMidtermGoalBinding? = null
    private val binding get() = _binding!!
    private lateinit var editText: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.enter_midterm_goal, container, false)
        editText = view.findViewById(R.id.editwhatgoal)

        val saveButton = view.findViewById<Button>(R.id.buttonOKMid)

        saveButton.setOnClickListener {
            val finallyGoalText = editText.text.toString()
            // Firestore に保存する処理
            saveFinallyGoalToFirestore(finallyGoalText)
        }

        return view


    }

    private fun saveFinallyGoalToFirestore(goalText: String) {
        val pref: SharedPreferences =
            requireActivity().getSharedPreferences("USER_ID", Context.MODE_PRIVATE)
        val userId = pref.getString("USER_ID", "hoge") ?: "hoge"

        val db = FirebaseFirestore.getInstance()
        //Firebase UI を入れる事で使用可能に
        val goalData = hashMapOf("content" to goalText)

        db.collection("users").document(userId).collection("midterm goal")
            .document("current") // 特定のIDを指定して上書き
            .set(goalData)
            .addOnSuccessListener {
                Log.d("Firestore", "Goal updated successfully")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error updating goal", it)
            }
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = EnterMidtermGoalBinding.bind(view)


        val pref: SharedPreferences =
            requireActivity().getSharedPreferences("USER_ID", Context.MODE_PRIVATE)
        val userId = pref.getString("USER_ID", "hoge") ?: "hoge"

        binding.buttonOKMid.setOnClickListener {

            val midtermgoal = Goals(
                content = binding.editwhatgoal.text.toString(),
                whatToDo = binding.editwhatdo.text.toString()

            )
            val db = Firebase.firestore
            db.collection("users").document(userId).collection("midterm goal")
                .add(midtermgoal)
                .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                    Log.d(
                        ContentValues.TAG,
                        "DocumentSnapshot added with ID: " + documentReference.id
                    )
                })
                .addOnFailureListener(OnFailureListener { e ->
                    Log.w(
                        ContentValues.TAG,
                        "Error adding document",
                        e
                    )
                })

            findNavController().navigate(R.id.action_enterMidtermGoalFragment_to_GoalFragment)
        }
    }



}