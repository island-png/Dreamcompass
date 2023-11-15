package app.fukushima.haruka.dreamcompass.TodayToDo

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.util.Half.toFloat
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.fukushima.haruka.dreamcompass.R
import app.fukushima.haruka.dreamcompass.databinding.FragmentTodayTodoBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlin.math.floor

class TodayTodoFragment : Fragment(R.layout.fragment_today_todo) {

    private var _binding: FragmentTodayTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewAdapter: FirestoreRecyclerAdapter<ToDo, ViewHolderToday>
  //  private var isDataLoaded = false // データが読み込まれたかどうかを追跡するフラグ

    private lateinit var viewModel: SharedViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentTodayTodoBinding.bind(view)

        val pref: SharedPreferences = requireActivity().getSharedPreferences("USER_ID", Context.MODE_PRIVATE)
        val userId = pref.getString("USER_ID", "hoge") ?: "hoge"

        val db = FirebaseFirestore.getInstance()
        val query = db.collection("users").document(userId).collection("todos").whereEqualTo("status", true)
        val options = FirestoreRecyclerOptions.Builder<ToDo>().setQuery(query, ToDo::class.java).build()
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        //val selectedTasks = arguments?.getParcelableArrayList<ToDo>("selectedTasks")

        // ViewModelから選択したタスクのリストを取得
        val selectedTasksLiveData: LiveData<List<ToDo>> = viewModel.selectedTasks


        if (selectedTasksLiveData != null) {
            // selectedItems を RecyclerView に表示する処理を実装
            val adapter = ListAdapterToday(options)
            binding.homeRecyclerView.adapter = adapter
        }


        viewAdapter = ListAdapterToday(options)

        binding.homeRecyclerView.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
        viewModel.getProgressLiveData().observe(viewLifecycleOwner, { progress ->
            // 進捗が変更されたときに行う処理をここに追加
            // たとえば、CustomCircularProgressBar を更新する
            val progressBar = binding.ProgressBar
            progressBar.setProgress(progress)
        })



        // TODO: button2 だと何をするボタンか分かりにくくなってしまうので、何をするボタンか説明となるような名前をつけよう！
        binding.buttonCenter.setOnClickListener {
            findNavController().navigate(R.id.action_TodayTodoFragment_to_GoalFragment)
        }
        // TODO: button3 も同じく名前を考えよう！
        binding.buttonLeft.setOnClickListener {
            findNavController().navigate(R.id.action_TodayTodoFragment_to_WeeklyTodoFragment)
        }
        binding.enterFeedback.setOnClickListener {
            findNavController().navigate(R.id.action_TodayTodoFragment_to_tomorrowTodoFragment)
        }
        binding.homeRecyclerWeeklyEventView.layoutManager = LinearLayoutManager(context)

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        viewAdapter!!.startListening()
//        initFirestoreAdapter()
        //calculateWeeklyAchievement()
    }
//
    override fun onStop() {
        super.onStop()

        viewAdapter.stopListening()

    }
}
class CustomCircularProgressBar(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val progressPaint = Paint()
    private var progressPercentage =0f


    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomCircularProgressBar)
        progressPercentage = typedArray.getFloat(R.styleable.CustomCircularProgressBar_progress, progressPercentage)
        typedArray.recycle()
        Log.d("計算結果", progressPercentage.toString())

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width / 2f) - 10 // 半径


// 計算結果を設定
        val calculationResult = progressPercentage
        //progressBar.setProgress(calculationResult)
        val backgroundColor = ContextCompat.getColor(context, R.color.lightblue)

// 円グラフの色を取得
        val progressBarColor = ContextCompat.getColor(context, R.color.progressBarColor)



        // 背景円を描画
        progressPaint.color = backgroundColor
        canvas.drawCircle(centerX, centerY, radius, progressPaint)

        // 円グラフを描画
        progressPaint.color = progressBarColor
        val sweepAngle = 360 * (progressPercentage / 100)
        canvas.drawArc(10f, 10f, width.toFloat() - 10f, height.toFloat() - 10f, -90f, sweepAngle, true, progressPaint)


    }


    fun setProgress(calculationResult: Float) {
        progressPercentage = calculationResult
        postInvalidate()
        //Log.d("graph","v")
    }


}
