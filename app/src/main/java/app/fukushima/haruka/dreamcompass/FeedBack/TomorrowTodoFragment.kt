package app.fukushima.haruka.dreamcompass.FeedBack

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.fukushima.haruka.dreamcompass.R
import app.fukushima.haruka.dreamcompass.TodayToDo.*
import app.fukushima.haruka.dreamcompass.databinding.FragmentSelectTomorrowTodoBinding
import app.fukushima.haruka.dreamcompass.databinding.FragmentTodaysReviewBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class TomorrowTodoFragment : Fragment(R.layout.fragment_select_tomorrow_todo) ,CheckBoxClickListener {
    private var _binding: FragmentSelectTomorrowTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewAdapter: FirestoreRecyclerAdapter<ToDo, ViewHolderTomorrow>
    private var selectedTaskName: String = ""
    private var selectedTasks: MutableList<ToDo> = mutableListOf()

    private lateinit var viewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSelectTomorrowTodoBinding.bind(view)

        val pref: SharedPreferences =
            requireActivity().getSharedPreferences("USER_ID", Context.MODE_PRIVATE)
        val userId = pref.getString("USER_ID", "hoge") ?: "hoge"
        val db = FirebaseFirestore.getInstance()
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)


        // Firestore のデータを取得するクエリを作成
        val weeklyquery =
            db.collection("users").document(userId).collection("todos")
        // .whereEqualTo("status", true)
        //.whereEqualTo("duedate", "tomorrow")
        // FirestoreRecyclerOptions を設定
        val options = FirestoreRecyclerOptions.Builder<ToDo>()
            .setQuery(weeklyquery, ToDo::class.java)
            .build()


        // FirestoreRecyclerAdapter を初期化
        viewAdapter = ListAdapterTomorrow(options)

        binding.homeRecyclerView.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.buttonOk.setOnClickListener(){
            val bundle = Bundle()
            bundle.putParcelableArrayList("selectedItems", ArrayList(selectedTasks))
            val todayTodoFragment = TodayTodoFragment()
            todayTodoFragment.arguments = bundle
            Log.d("selacetedItem", selectedTaskName)

            // 選択されたタスクをクリア
            //selectedTasks.clear()
            findNavController().navigate(R.id.action_tomorrowTodoFragmemt_to_todayTodoFragment)
        }
    }



    override fun onStart() {
        super.onStart()
        //viewAdapter!!.startListening()
        if (::viewAdapter.isInitialized) {
            viewAdapter.startListening()
        }
    }

    override fun onStop() {
        super.onStop()

        if (viewAdapter != null) {
            viewAdapter!!.stopListening()
        }
    }

    override fun onCheckBoxClicked(position: Int, isChecked: Boolean) {

        //viewAdapter.setOnItemClickListener(object : ListAdapterTomorrow.OnItemClickListener {
            val item = viewAdapter.getItem(position)

            if (isChecked) {
                // チェックボックスがチェックされた場合、selectedTaskNameに追加
                selectedTasks.add(item)
                selectedTaskName = item.name
            } else {
                // チェックボックスがオフになった場合、selectedTaskNameから削除
                selectedTaskName = ""
            }
            Log.d("selected", selectedTaskName)
        }
    }

