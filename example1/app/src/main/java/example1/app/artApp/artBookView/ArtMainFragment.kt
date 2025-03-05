package example1.app.artApp.artBookView

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gokmenmutlu.artbookkotlin.R
import com.gokmenmutlu.artbookkotlin.databinding.ArtMainFragmentBinding
import example1.app.artApp.adapter.ArtMainAdapter
import example1.app.artApp.model.ArtModel
import example1.app.artApp.util.ArtInterface
import example1.app.viewModels.ArtViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ArtMainFragment : Fragment(), ArtInterface {

    private var _binding: ArtMainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var artMainAdapter: ArtMainAdapter
    private val artViewModel: ArtViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArtMainFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        artMainAdapter = ArtMainAdapter(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = artMainAdapter

        // Art verilerini gözlemlemek için LiveData kullanıyoruz
        artViewModel.allArts.observe(viewLifecycleOwner, Observer { artList ->
            artMainAdapter.setArtList(artList) // Adapter'ı güncelle
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        // Menü ve arama fonksiyonu
        setupMenu(requireActivity(), navController)

        binding.fabAdd.setOnClickListener {
            navController.navigate(R.id.action_artBookFragment_to_artAddFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupMenu(requireActivity: FragmentActivity, navController: NavController) {
        val menuHost: MenuHost = requireActivity
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_art, menu)
                val searchItem = menu.findItem(R.id.itemSearch)
                val searchView = searchItem?.actionView as SearchView

                // SearchView için query text listener ekleme
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        query?.let {
                            performSearch(it) // Kullanıcı "Enter" tuşuna basarsa arama yapılır
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        // Arama metni değiştiğinde debounce işlemi için Job ekleyelim
                        lifecycleScope.launch {
                            delay(500) // 500 ms gecikme
                            newText?.let {
                                performSearch(it) // Kullanıcı her yazdığında arama yapılır
                            }
                        }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.itemArtAdd -> {
                        navController.navigate(R.id.action_artBookFragment_to_artAddFragment)
                        true
                    }
                    R.id.itemHomePage -> {
                        navController.navigate(R.id.action_artBookFragment_to_mainFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner) // Menü yalnızca fragment aktifken geçerli olacak
    }

    private fun performSearch(query: String) {
        // 'viewLifecycleOwner' kullanımı sadece 'view' mevcutken geçerlidir
        view?.let {
            // Arama işlemini ViewModel ile yapıyoruz
            artViewModel.searchArt(query)

            // Arama sonuçlarını gözlemliyoruz
            artViewModel.searchResults.observe(viewLifecycleOwner) { filteredList ->
                // Adapter'ı güncelliyoruz
                artMainAdapter.setArtList(filteredList)
            }
        }
    }

    override fun onDeleteArt(art: ArtModel) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete")
        builder.setMessage("Do you want to delete this item? (${art.location})")
        builder.setPositiveButton("Yes") { _, _ ->
            artViewModel.deleteArt(art)
            Toast.makeText(requireContext(), "Deleted.", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }
}
