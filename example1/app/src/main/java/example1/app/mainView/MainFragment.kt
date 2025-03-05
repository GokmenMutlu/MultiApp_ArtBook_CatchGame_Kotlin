package example1.app.mainView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.gokmenmutlu.artbookkotlin.R
import com.gokmenmutlu.artbookkotlin.databinding.MainFragmentBinding
import example1.app.artApp.room.ArtDatabase


class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val view = binding.root



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        binding.buttonGame.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_gameFragment)
        }
        binding.buttonArt.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_artBookFragment3)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}