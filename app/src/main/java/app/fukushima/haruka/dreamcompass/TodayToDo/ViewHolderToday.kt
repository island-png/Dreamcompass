package app.fukushima.haruka.dreamcompass.TodayToDo

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.fukushima.haruka.dreamcompass.databinding.ItemDetaCellTodayBinding

class ViewHolderToday(private val binding: ItemDetaCellTodayBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(task: ToDo) {
        binding.itemTextView.text = task.name

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
