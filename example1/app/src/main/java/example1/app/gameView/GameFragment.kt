package example1.app.gameView


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gokmenmutlu.artbookkotlin.R
import com.gokmenmutlu.artbookkotlin.databinding.GameFragmentBinding

import example1.app.viewModels.GameViewModel

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameFragment : Fragment() {
    // KOLAY - ORTA - ZOR - MOD YAPILIP DELAY HIZI SEÇİLEN MOD A GÖRE DEĞİŞTİRİLEBİLİR.

    private var _binding: GameFragmentBinding? = null
    private val binding get() = _binding!!
    private var myImageList = arrayListOf<ImageView>()
    private var random = 0
    private var tempRandom = -1
    private var score = 0
    private var highScore = 0
    private var timeLeft = 30.0 // 60 seconds
    private var delay : Long = 1000L // ms
    private var job: Job? = null

    // private val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = "game_preferences") //DataStore ile yapımı icin
    // private val HIGH_SCORE_KEY = intPreferencesKey("high_score")

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = GameFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val highScore = viewModel.getHighScore(requireContext())
        binding.highScoreText.text = "High Score: $highScore"

        myImageList()
        hideImages()
        myImageList[8].visibility = View.VISIBLE  // Center image
        startMovingImages()

    }

    private fun myImageList() {
        myImageList.add(binding.imageView1)
        myImageList.add(binding.imageView2)
        myImageList.add(binding.imageView3)
        myImageList.add(binding.imageView4)
        myImageList.add(binding.imageView5)
        myImageList.add(binding.imageView6)
        myImageList.add(binding.imageView7)
        myImageList.add(binding.imageView8)
        myImageList.add(binding.imageView9)
        myImageList.add(binding.imageView10)
        myImageList.add(binding.imageView11)
        myImageList.add(binding.imageView12)
        myImageList.add(binding.imageView13)
        myImageList.add(binding.imageView14)
        myImageList.add(binding.imageView15)

    }
    private fun hideImages() {

        for (i in myImageList) {
            i.visibility = View.INVISIBLE
            i.isEnabled = true
        }

    }

    private fun startMovingImages() {
      job = lifecycleScope.launch {

            while (timeLeft > 0.0) {

                    delay(delay)
                    timeLeft-=1.0

                hideImages()

                random = (0..14).random()
                if(random == tempRandom) { // ayni yerde üst üste resim olmaması için.
                    random = (0..14).random()
                }
                tempRandom = random

                myImageList[random].visibility = View.VISIBLE
                myImageList[random].isEnabled = true
                scoreIncrease(random)

                binding.leftTimeText.text = "Time: $timeLeft - Score: $score"

            }

          if(score > highScore) {
              viewModel.saveHighScore(requireContext(),score)
          }
          job?.cancel()

          hideImages()
          restartGame()

        }
    }
    private fun scoreIncrease(i : Int) {
        myImageList[i].setOnClickListener{
            score+=1
            if(score%2==0){ // image speed
                delay-=25L
            }
            timeLeft+=0.5 // time plus
            myImageList[i].isEnabled = false
        }

    }

    private fun restartGame() {

        val navController = findNavController()

        val alert = AlertDialog.Builder(requireContext())
        alert.setMessage("Play again?")
        alert.setTitle("Restart?")
        alert.setPositiveButton("Yes") { _, _ ->
            navController.run {
                val currentDestination = currentDestination?.id
                if(currentDestination != null) {
                    popBackStack(currentDestination, true) // güncel fragment i kapatıp yeniden başlatma.
                    navigate(currentDestination)
                }
            }
        }

        alert.setNeutralButton("Menu")  { _, _ ->
            navController.run {
                val currentDestination = currentDestination?.id
                if(currentDestination != null) {
                    popBackStack(currentDestination, true) // güncel fragment i kapatıp yeniden başlatma.
                    navigate(R.id.mainFragment)
                }
            }
        }
        alert.setNegativeButton("No",null)
        alert.create().show()

    }

    /* suspend fun saveHighScore(context: Context, score: Int) {  // save HighScore with DataStore

         context.datastore.edit { preferences ->
             val currentHighScore = preferences[HIGH_SCORE_KEY] ?: 0
             if (score > currentHighScore) {
                 preferences[HIGH_SCORE_KEY] = score
             }
         }

     }

     fun getHighScore(context: Context): Flow<Int> {
         return context.datastore.data.map { preferences ->
             preferences[HIGH_SCORE_KEY] ?: 0
         }
     }
     */




}