package app.fukushima.haruka.dreamcompass.WeeklyEvent

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.fukushima.haruka.dreamcompass.TodayToDo.ViewHolderWeeklyEvent
import app.fukushima.haruka.dreamcompass.TodayToDo.WeeklyEvent
import app.fukushima.haruka.dreamcompass.databinding.ItemDetaCellWeeklyEventBinding

class ListAdapterWeeklyEvent() : RecyclerView.Adapter<ViewHolderWeeklyEvent>(), Parcelable {

    private val TodoList: MutableList<WeeklyEvent> = mutableListOf()

    constructor(parcel: Parcel) : this() {
    }

    // ViewHolderの作成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderWeeklyEvent {
        val binding = ItemDetaCellWeeklyEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderWeeklyEvent(binding)
    }

    // ViewHolderの設定
    override fun onBindViewHolder(holder: ViewHolderWeeklyEvent, position: Int) {
        val todo = TodoList[position]
        holder.binding.itemTextViewE.text = todo.eventName
    }

    // ViewHolderの数の決定
    override fun getItemCount(): Int = TodoList.size

    fun updateList(newList: List<WeeklyEvent>) {
        TodoList.clear()
        TodoList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListAdapterWeeklyEvent> {
        override fun createFromParcel(parcel: Parcel): ListAdapterWeeklyEvent {
            return ListAdapterWeeklyEvent(parcel)
        }

        override fun newArray(size: Int): Array<ListAdapterWeeklyEvent?> {
            return arrayOfNulls(size)
        }
    }


}