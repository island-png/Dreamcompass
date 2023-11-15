package app.fukushima.haruka.dreamcompass.welcome
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import app.fukushima.haruka.dreamcompass.EnterGoal.EnterFinallyGoalFragment
import app.fukushima.haruka.dreamcompass.R
import com.google.firebase.firestore.FirebaseFirestore

class WelcomeFragment : Fragment() {

    private val AUTO_TRANSITION_DELAY = 3000L // 3秒
    private val PREFS_NAME = "MyPrefs"
    private val PREF_FIRST_LAUNCH = "firstLaunch"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        Log.d("ra","ff")
        val navController = Navigation.findNavController(requireActivity(), R.id.fragment_container)

        Handler().postDelayed({
            // 3秒後にGoalInputFragmentに自動遷移
            //Navigation.findNavController(view).navigate(R.id.action_WelcomeFragment_to_EnterFinallyGoalFragment)
            navController.navigate(R.id.action_WelcomeFragment_to_EnterFinallyGoalFragment)

        }, AUTO_TRANSITION_DELAY)

        return view

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isFirstLaunch = isFirstLaunch()
        //val contentViewContainerId =R.id.fragment_container

        if (isFirstLaunch) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EnterFinallyGoalFragment())
                .commit()
            setFirstLaunch(false) // 初回起動フラグを更新
        }
    }

    private fun isFirstLaunch(): Boolean {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        return sharedPreferences.getBoolean(PREF_FIRST_LAUNCH, true)
    }

    private fun setFirstLaunch(value: Boolean) {
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(PREF_FIRST_LAUNCH, value)
        editor.apply()
    }
}

class GoalInputFragment : Fragment(R.layout.enter_finally_goal) {

    private lateinit var db: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.enter_finally_goal, container, false)

        val editwhatgoal = view.findViewById<EditText>(R.id.editwhatgoal)
        val buttonOK = view.findViewById<Button>(R.id.buttonOK)

        db = FirebaseFirestore.getInstance()
        buttonOK.setOnClickListener {
            val goal = editwhatgoal.text.toString()
            val bundle = Bundle().apply {
                putString("goal", goal)
            }
            saveGoalToFirestore(goal)
          //  findNavController().navigate(R.id.action_goalInputFragment_to_goalFragment, bundle)

        }

        return view
    }

    private fun saveGoalToFirestore(goal: String) {
        // Firestoreへの保存ロジックを実装
        // Firestoreにデータを保存
        val goalData = hashMapOf(
            "goal" to goal
            // 他の必要なデータがあれば追加
        )
        val pref: SharedPreferences =
            requireActivity().getSharedPreferences("USER_ID", Context.MODE_PRIVATE)
        //pref.edit().putString("USER_ID","VlVM0300U1ak4P78IsLK")//8/30追加
        var userId = pref.getString("USER_ID", "hoge") ?: "hoge"

        db.collection("users").document(userId).collection("finally goal")
            .add(goalData)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding document", e)
            }
    }
}
