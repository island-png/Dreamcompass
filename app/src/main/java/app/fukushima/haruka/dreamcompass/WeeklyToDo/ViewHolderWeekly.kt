package app.fukushima.haruka.dreamcompass.WeeklyToDo

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.fukushima.haruka.dreamcompass.TodayToDo.ToDo
import app.fukushima.haruka.dreamcompass.databinding.ItemDataCellWeekBinding


class ViewHolderWeekly(
    private val binding: ItemDataCellWeekBinding


) : RecyclerView.ViewHolder(binding.root) {
    val checkBox = binding.checkWeek
    fun bind(task: ToDo) {
        binding.itemTextView.text = task.name

      //checkBox.setOnCheckedChangeListener { _, isChecked ->


//        binding.dateTextView.text =
//            SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPANESE).format(task.createdAt)

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