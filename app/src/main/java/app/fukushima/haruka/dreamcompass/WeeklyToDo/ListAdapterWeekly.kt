package app.fukushima.haruka.dreamcompass.WeeklyToDo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import app.fukushima.haruka.dreamcompass.TodayToDo.ToDo
import app.fukushima.haruka.dreamcompass.databinding.ItemDataCellWeekBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


// 独自のAdapter
class ListAdapterWeekly(options: FirestoreRecyclerOptions<ToDo>) :
    FirestoreRecyclerAdapter<ToDo, ViewHolderWeekly>(options) {
    //val checkBox: CheckBox = binding.checkTomorrow // ここで checkBox プロパティを定義

    private lateinit var binding: ItemDataCellWeekBinding

    // チェックボックスの状態変化を検出するリスナー
    private var onCheckedChangeListener: ((documentId: String, isChecked: Boolean) -> Unit)? = null

    fun setOnCheckedChangeListener(listener: (documentId: String, isChecked: Boolean) -> Unit) {
        onCheckedChangeListener = listener
    }



    // ViewHolderの作成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderWeekly {
        binding =
            ItemDataCellWeekBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderWeekly(binding)

    }

    // ViewHolderの設定
    override fun onBindViewHolder(holder: ViewHolderWeekly, position: Int, model: ToDo) {
        holder.bind(model)
        holder.checkBox.isChecked = model.status

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            val documentId = snapshots.getSnapshot(position).id

            onCheckedChangeListener?.invoke(documentId, isChecked)
        }


        Log.d("todos", model.toString())
    }

}