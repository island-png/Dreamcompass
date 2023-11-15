package app.fukushima.haruka.dreamcompass.FeedBack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.fukushima.haruka.dreamcompass.R
import app.fukushima.haruka.dreamcompass.TodayToDo.ToDo
import app.fukushima.haruka.dreamcompass.TodayToDo.ViewHolderToday
import app.fukushima.haruka.dreamcompass.databinding.ItemDataCellWeekBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class ListAdapterTomorrow(options: FirestoreRecyclerOptions<ToDo>):
    FirestoreRecyclerAdapter<ToDo, ViewHolderTomorrow>(options) {

    //private val TodoList: MutableList<ToDo> = mutableListOf()
    private lateinit var binding: ItemDataCellWeekBinding


//    interface CheckmarkClickListener {
//        fun onItemClick(view: View, position: Int)
//    }
//

    // ViewHolderの作成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTomorrow {
        binding =
            ItemDataCellWeekBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderTomorrow(binding)

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_data_cell_week, parent, false)


//        view.setOnClickListener{
//            binding.RecyclerView?.let{
//                itemClickListener.onItemClick(view,it.)
//            }
//        }


    }

    // ViewHolderの設定
    override fun onBindViewHolder(holder: ViewHolderTomorrow, position: Int, model: ToDo) {
//        val todo = TodoList[position]
//        holder.binding.itemTextView.text = todo.name
        holder.bind(model)
//        Log.d("todaytodos", model.toString())
//        holder..setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                // チェックボックスが選択された場合、選択されたタスクリストに追加
//                selectedTasks.add(model)
//            } else {
//                // チェックボックスが選択解除された場合、選択されたタスクリストから削除
//                selectedTasks.remove(model)
//            }
//        }
//
//
//    }


    }
}