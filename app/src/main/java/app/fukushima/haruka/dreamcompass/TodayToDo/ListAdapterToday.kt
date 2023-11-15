package app.fukushima.haruka.dreamcompass.TodayToDo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import app.fukushima.haruka.dreamcompass.R
import app.fukushima.haruka.dreamcompass.WeeklyToDo.ViewHolderWeekly
import app.fukushima.haruka.dreamcompass.databinding.ItemDetaCellTodayBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ListAdapterToday(options: FirestoreRecyclerOptions<ToDo>):
    FirestoreRecyclerAdapter<ToDo, ViewHolderToday>(options){
    var checkBoxClickListener:CheckBoxClickListener? = null

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val checkBox: CheckBox = itemView.findViewById(R.id.checkToday)


        init {
            checkBox.setOnCheckedChangeListener{_, isChecked ->
                checkBoxClickListener?.onCheckBoxClicked(adapterPosition, isChecked)

            }
        }
    }

    //private val TodoList: MutableList<ToDo> = mutableListOf()
    private lateinit var binding: ItemDetaCellTodayBinding


    // ViewHolderの作成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderToday {
        binding = ItemDetaCellTodayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderToday(binding)

    }

    // ViewHolderの設定
    override fun onBindViewHolder(holder: ViewHolderToday, position: Int,model:ToDo) {
//        val todo = TodoList[position]
//        holder.binding.itemTextView.text = todo.name
        holder.bind(model)
//        Log.d("todaytodos", model.toString())



    }


}
interface CheckBoxClickListener {
    fun onCheckBoxClicked(position: Int,isChecked: Boolean)
}