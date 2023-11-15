package app.fukushima.haruka.dreamcompass.TodayToDo

import com.google.firebase.firestore.DocumentId
import java.util.*

data class ListWeeklyData(
    @DocumentId
    val id: String = "",
    val title: String = "",
)
