package app.fukushima.haruka.dreamcompass

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref: SharedPreferences = getSharedPreferences("USER_ID", Context.MODE_PRIVATE)
        userId = pref.getString("USER_ID", null)
        if (userId == null ) {
            val randomUserId = generateRandomUserId()
            val editor = pref.edit()
            editor.putString("USER_ID", randomUserId)
            editor.commit()
            userId = randomUserId

        }
        //val finalUserId = pref.getString("USER_ID", "hoge")

        Log.d("UserIdCheck", userId.toString())

        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        signInAnonymously()

    }

    private fun generateRandomUserId(): String {
        // ランダムなユーザーIDを生成するためのロジックを使用
        return UUID.randomUUID().toString()
    }
    private fun signInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("success", "currentUser = ${auth.currentUser?.uid.toString()}")
                } else {
                    Log.d("error", task.exception.toString())
                }
            }
    }
    public override fun onStart() {
        super.onStart()

    }

}