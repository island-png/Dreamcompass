package app.fukushima.haruka.dreamcompass.Detail

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.fukushima.haruka.dreamcompass.R
import app.fukushima.haruka.dreamcompass.TodayToDo.ToDo
import app.fukushima.haruka.dreamcompass.databinding.FragmentTodoDetailBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class TodoDetailFragment : Fragment(R.layout.fragment_todo_detail),
    OnDateSelectedListener {

    private var _binding: FragmentTodoDetailBinding? = null
    private val binding get() = _binding!!

    private val spinnerSituationItems = arrayOf("Not Done", "In Progress", "Completed")
    private val spinnerPriorityItems = arrayOf("☆", "☆☆", "☆☆☆")

    lateinit var selecteddate: String



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentTodoDetailBinding.bind(view)

        // TODO: SharedPreferences はアプリ内で共通のものを1つ使うので、名前は "TODO_STORE" のような「Todo を保存するための場所」という意味にすると良さそう！
        val pref: SharedPreferences =
            requireActivity().getSharedPreferences("USER_ID", Context.MODE_PRIVATE)
        val userId = pref.getString("USER_ID", "hoge") ?: "hoge"

        val situationAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerSituationItems

        )
        situationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSituation.adapter = situationAdapter
        var situation = ""
        binding.spinnerSituation.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                //　アイテムが選択された時
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?, position: Int, id: Long
                ) {
                    val spinnerParent = parent as Spinner
                    val item = spinnerParent.selectedItem as String
                    situation = item
                    Log.d("学習状況", "button_situation clicked")

                }

                //　アイテムが選択されなかった
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //
                }
            }


        val priorityAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerPriorityItems
        )
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPriority.adapter = priorityAdapter
        var priority = ""
        binding.spinnerPriority.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                //　アイテムが選択された時
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?, position: Int, id: Long
                ) {
                    val spinnerParent = parent as Spinner
                    val item = spinnerParent.selectedItem as String
                    // View Binding
                    priority = item
                    Log.d("優先度", "button_situation clicked")

                }

                //　アイテムが選択されなかった
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //
                }
            }

//        val today = LocalDate.now()
//// 今日の日付から1日を加えた日付を計算
//        val tomorrow = today.plusDays(1)


        binding.buttonOk.setOnClickListener {
            val priorityNum = binding.spinnerPriority.selectedItem.toString().length

            val todo = ToDo(
                name = binding.editText1.text.toString(),
                priority = priorityNum,
                situation = binding.spinnerSituation.selectedItemId.toInt(),
                memo = binding.editMemo.text.toString(),
                duedate = selecteddate,
                //plandate = tomorrow
                status = false

            )


// Add a new document with a generated ID
            val db = Firebase.firestore
            db.collection("users").document(userId).collection("todos")
                .add(todo)
                .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                    Log.d(
                        TAG,
                        "DocumentSnapshot added with ID: " + documentReference.id
                    )
                })
                .addOnFailureListener(OnFailureListener { e ->
                    Log.w(
                        TAG,
                        "Error adding document",
                        e
                    )
                })



            findNavController().navigate(R.id.action_toDoDetailFragment_to_WeeklyTodoFragment)
        }


        //現在日付を取得
        val current = LocalDate.now()
        //表示する際のフォーマットを決める
        val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日(E)")
        //現在日付をフォーマットに合わせる
        val dateFormat = current.format(formatter)

        //キーボードを表示させない
        binding.datePickButton.setRawInputType(InputType.TYPE_NULL)
        //EditTextに現在日付を表示
        binding.datePickButton.setText(dateFormat.toString())

        /**
         * 日付の変更
         * 値：選択された日付
         * 表示：yyyy年MM月dd日(E)
         */

        binding.datePickButton.setOnClickListener {
            val datePicker = DatePickerFragment()
            //FragmentManagerを取得する
            val fragmentManager = requireActivity().supportFragmentManager
            //DatePickerを表示

            datePicker.setTargetFragment(this, 0)
            datePicker.show(parentFragmentManager, "DatePickerDialog")
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDateSelected(year: Int, month: Int, day: Int) {
        val selectedDate: String =
            year.toString() + "/" + (month + 1).toString() + "/" + day.toString()
        binding.datePickButton.setText(selectedDate)
        selecteddate = selectedDate
        Log.d("date", selectedDate)
    }
}

class DatePickerFragment() : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    private var listener: OnDateSelectedListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //Calender()を取得
        val calendar: Calendar = Calendar.getInstance()
        //年を示すフィールドの値を取得
        val year = calendar.get(Calendar.YEAR)
        //月を示すフィールドの値を取得
        val month = calendar.get(Calendar.MONTH)
        //日を示すフィールドの値を取得
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        //指定された日付でDatePickerを作成して返す
        return DatePickerDialog(
            requireContext(),
            this,
            year,
            month,
            day
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        Log.d("onAttach", "onAttachが呼ばれた")
        listener = targetFragment as? OnDateSelectedListener
        if (listener == null) {
            throw ClassCastException("$targetFragment must implement OnDateSelectedListener")
        }
    }

    //日付選択がされた時の処理
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        listener!!.onDateSelected(year, month, dayOfMonth)

    }

}

interface OnDateSelectedListener {
    fun onDateSelected(year: Int, month: Int, day: Int)
}

