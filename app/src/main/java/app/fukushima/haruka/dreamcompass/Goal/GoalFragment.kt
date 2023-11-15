package app.fukushima.haruka.dreamcompass.Goal

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import app.fukushima.haruka.dreamcompass.R
import app.fukushima.haruka.dreamcompass.databinding.FragmentGoalBinding
import com.google.firebase.firestore.FirebaseFirestore

class GoalFragment : Fragment(R.layout.fragment_goal) {

    private var _binding: FragmentGoalBinding? = null
    private val binding get() = _binding!!


    //Firebase UI を入れる事で使用可能に
    //private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_goal, container, false)
        val finallygoalTextView = view.findViewById<TextView>(R.id.finallygoal)
        val midtermgoalTextView = view.findViewById<TextView>(R.id.midtermGoal)

        val pref: SharedPreferences = requireActivity().getSharedPreferences("USER_ID", Context.MODE_PRIVATE)
        val userId = pref.getString("USER_ID","hoge")?:"hoge"
        val db = FirebaseFirestore.getInstance()
        val query = db.collection("users").document(userId).collection("finally goal")


        db.collection("users").document(userId).collection("finally goal")
            .get()
            .addOnSuccessListener { result ->
//
                for (document in result) {
                    val goal = document.getString("content")
                    finallygoalTextView.text = goal
                }

                Log.e("finally goal", "finally")


            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting documents: ", e)
            }



        db.collection("users").document(userId).collection("midterm goal")
            .get()
            .addOnSuccessListener { result ->
//
                for (document in result) {
                    val goal = document.getString("content")
                    midtermgoalTextView.text = goal
                    Log.d("Midterm Goal", "Midterm Goal: $goal") // ログに表示

                }
            }
            .addOnFailureListener { e ->
               // Log.e("Firestore", "Error getting documents: ", e)
            }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentGoalBinding.bind(view)



        // TODO: button1 だと何をするボタンか分かりにくくなってしまうので、何をするボタンか説明となるような名前をつけよう！
        binding.buttonRight.setOnClickListener {
            findNavController().navigate(R.id.action_GoalFragment_to_TodayTodoFragment)
        }
        // TODO: button3 も同じく名前を考えよう！
        binding.buttonLeft.setOnClickListener {
            findNavController().navigate(R.id.action_GoalFragment_to_WeeklyTodoFragment)
        }
        binding.buttonUpdateFinally.setOnClickListener {
            findNavController().navigate(R.id.action_GoalFragment_to_enterFinallyGoalFragment)
        }
        binding.buttonUpdatemidterm.setOnClickListener {
            findNavController().navigate(R.id.action_GoalFragment_to_enterMidtermGoalFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}