package app.fukushima.haruka.dreamcompass.WeeklyToDo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.fukushima.haruka.dreamcompass.R
import app.fukushima.haruka.dreamcompass.TodayToDo.SharedViewModel
import app.fukushima.haruka.dreamcompass.TodayToDo.ToDo
import app.fukushima.haruka.dreamcompass.WeeklyToDo.ListAdapterWeekly
import app.fukushima.haruka.dreamcompass.WeeklyToDo.ViewHolderWeekly
import app.fukushima.haruka.dreamcompass.databinding.FragmentWeeklyTodoBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class WeeklyTodoFragment : Fragment(R.layout.fragment_weekly_todo) {

    private var _binding: FragmentWeeklyTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewAdapter: FirestoreRecyclerAdapter<ToDo, ViewHolderWeekly>
    private val viewModel: SharedViewModel by viewModels()
    private var checkedTaskCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentWeeklyTodoBinding.bind(view)

        val pref: SharedPreferences = requireActivity().getSharedPreferences("USER_ID", Context.MODE_PRIVATE)
        val userId = pref.getString("USER_ID", "hoge") ?: "hoge"

        val db = FirebaseFirestore.getInstance()
        val query = db.collection("users").document(userId)
            .collection("todos")
            .orderBy("priority", Query.Direction.DESCENDING)
            .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
        val options = FirestoreRecyclerOptions.Builder<ToDo>().setQuery(query, ToDo::class.java).build()
        val localviewAdapter = ListAdapterWeekly(options)

        localviewAdapter.setOnCheckedChangeListener { documentId, isChecked ->
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("users").document(userId).collection("todos").document(documentId)
            val updateData = hashMapOf<String, Any>(
                "status" to isChecked // チェックボックスの状態に応じてtrueまたはfalseを設定

            )


            docRef.update(updateData)
                .addOnSuccessListener {
                    // 更新が成功した場合の処理をここに追加
                }
                .addOnFailureListener { exception ->
                    // 更新が失敗した場合の処理をここに追加
                }
            if (isChecked) {
                //docRef.delete()
                checkedTaskCount++
            } else {
                checkedTaskCount--
            }

            calculateAndUpdateProgress()
        }

        viewAdapter = localviewAdapter

        binding.homeRecyclerView.apply {
            adapter = localviewAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        binding.buttonRight.setOnClickListener {
            findNavController().navigate(R.id.action_WeeklyTodoFragment_to_TodayTodoFragment)
        }

        binding.buttonCenter.setOnClickListener {
            findNavController().navigate(R.id.action_WeeklyTodoFragment_to_GoalFragment)
            Log.d("goalll", "tt")
        }

        binding.buttonNewitem.setOnClickListener {
            findNavController().navigate(R.id.action_WeeklyTodoFragment_to_todoDetailFragment)
        }

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(context)

        calculateAndUpdateProgress()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun calculateAndUpdateProgress() {
        val db = FirebaseFirestore.getInstance()
        val pref: SharedPreferences = requireActivity().getSharedPreferences("USER_ID", Context.MODE_PRIVATE)
        val userId = pref.getString("USER_ID", "hoge") ?: "hoge"

        val query = db.collection("users").document(userId)
            .collection("todos")
            .orderBy("priority", Query.Direction.DESCENDING)
            .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                val totalTasks = querySnapshot.size()
                val completedTasks = querySnapshot.documents.count { it["status"] == true }

                val progressPercentage = if (totalTasks > 0) {
                    (completedTasks.toFloat() / totalTasks.toFloat()) * 100
                } else {
                    0f
                }

                Log.d("Debug", "Total Tasks: $totalTasks, Completed Tasks: $completedTasks, Progress Percentage: $progressPercentage")

                val viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
                viewModel.updateProgress(progressPercentage)



            }
            .addOnFailureListener { exception ->
                Log.e("Error", "Firestore query failed: $exception")
            }
    }

    override fun onStart() {
        super.onStart()
        viewAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()

        if (viewAdapter != null) {
            viewAdapter!!.stopListening()
        }
    }
}
