package app.fukushima.haruka.dreamcompass.welcome

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import app.fukushima.haruka.dreamcompass.R
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class WelcomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)


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

        auth = FirebaseAuth.getInstance()
        signInAnonymously()

        if (savedInstanceState == null) {
//
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
            val navController = navHostFragment.navController
        }
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
