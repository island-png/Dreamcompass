package app.fukushima.haruka.dreamcompass.TodayToDo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val progressLiveData = MutableLiveData<Float>()
    val getSelectedTasks: MutableLiveData<List<ToDo>> = MutableLiveData()
    val selectedTasks: MutableLiveData<List<ToDo>> = MutableLiveData()
    val setSelectedTasks: MutableLiveData<List<ToDo>> = MutableLiveData()



    // LiveDataのgetterメソッド
    fun getProgressLiveData(): LiveData<Float> {
        return progressLiveData
    }

    // 進捗をLiveDataに設定するメソッド
    fun updateProgress(progress: Float) {
        progressLiveData.value = progress
        Log.d("計算結果model", progress.toString())

    }
}