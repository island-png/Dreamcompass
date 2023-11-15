package app.fukushima.haruka.dreamcompass.TodayToDo

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import java.util.*

data class ToDo (val name:String = "",
                 val duedate:String= "",
                 val priority:Int=0,
                 val situation:Int=0,
                 val memo:String ="",
                 val status:Boolean = false,
                 val userId:String = ""
):Parcelable{
    constructor(parcel: Parcel) : this(
    parcel.readString() ?: "",
    parcel.readString() ?: "",
    parcel.readInt(),
    parcel.readInt(),
    parcel.readString() ?: "",
    parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",

)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(duedate)
        parcel.writeInt(priority)
        parcel.writeInt(situation)
        parcel.writeString(memo)
        parcel.writeByte(if (status) 1 else 0)
        parcel.writeString(userId)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ToDo> {
        override fun createFromParcel(parcel: Parcel): ToDo {
            return ToDo(parcel)
        }

        override fun newArray(size: Int): Array<ToDo?> {
            return arrayOfNulls(size)
        }
    }
}








//data class ToDo (val name:String, val category:String, val priority:Int, val status:Boolean)