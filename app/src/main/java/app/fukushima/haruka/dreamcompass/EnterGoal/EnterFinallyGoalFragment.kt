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
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.fragment.findNavController
import app.fukushima.haruka.dreamcompass.Goal.GoalFragment
import app.fukushima.haruka.dreamcompass.Goal.Goals
import app.fukushima.haruka.dreamcompass.R
import app.fukushima.haruka.dreamcompass.TodayToDo.ToDo
import app.fukushima.haruka.dreamcompass.welcome.WelcomeFragment
import app.fukushima.haruka.dreamcompass.databinding.EnterFinallyGoalBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EnterFinallyGoalFragment : Fragment(R.layout.enter_finally_goal) {

    private lateinit var editText: EditText
    private var _binding: EnterFinallyGoalBinding? = null
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.enter_finally_goal, container, false)
        editText = view.findViewById(R.id.editwhatgoal)

        val saveButton = view.findViewById<Button>(R.id.buttonOK)

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

        db.collection("users").document(userId).collection("finally goal")
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
        //super.onViewCreated(view, savedInstanceState)
        _binding = EnterFinallyGoalBinding.bind(view)

        val pref: SharedPreferences =
            requireActivity().getSharedPreferences("USER_ID", Context.MODE_PRIVATE)
        val userId = pref.getString("USER_ID", "hoge") ?: "hoge"
        binding.buttonOK.setOnClickListener {
            val finallygoal = Goals(
                content = binding.editwhatgoal.text.toString(),
                whatToDo = binding.editwhatdo.text.toString()

                )
            val db = Firebase.firestore

            val goalCollection = db.collection("users").document(userId).collection("finally goal")

            goalCollection.get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        // すべての古いゴールを削除
                        goalCollection.document(document.id).delete()
                            .addOnSuccessListener {
                                Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted.")
                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Error deleting document", e)
                            }
                    }

                    // 新しいゴールを追加
                    goalCollection.add(finallygoal)
                        .addOnSuccessListener { documentReference ->
                            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                        }
                }


// Add a new document with a generated ID
            db.collection("users").document(userId).collection("finally goal")
                .add(finallygoal)
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



            findNavController().navigate(R.id.action_enterFinallyGoalFragment_to_GoalFragment)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


