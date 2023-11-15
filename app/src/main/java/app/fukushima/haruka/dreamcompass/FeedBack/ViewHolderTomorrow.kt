package app.fukushima.haruka.dreamcompass.FeedBack

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.fukushima.haruka.dreamcompass.TodayToDo.ToDo
import app.fukushima.haruka.dreamcompass.databinding.ItemDataCellWeekBinding

class ViewHolderTomorrow(private val binding: ItemDataCellWeekBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(task: ToDo) {
        binding.itemTextView.text = task.name

        if (task.status) {
            binding.checkWeek.isChecked = false

        }
//        else{
//            binding.checkTomorrow.isChecked = true
//        }
    }


}
private val diffUtilItemCallback = object : DiffUtil.ItemCallback<ToDo>() {
    override fun areContentsTheSame(oldItem: ToDo, newItem: ToDo): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: ToDo, newItem: ToDo): Boolean {
        return oldItem.name == newItem.name
    }
}
