package example1.app.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel

class GameViewModel() : ViewModel() {

    fun saveHighScore(context:Context ,finalScore : Int) {

        val sharedPreferences = context.getSharedPreferences("game_sharedPreferences", Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt("high_score", finalScore).apply()

    }

    fun getHighScore(context: Context): Int {

        val sharedPreferences = context.getSharedPreferences("game_sharedPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("high_score", 0)

    }

}