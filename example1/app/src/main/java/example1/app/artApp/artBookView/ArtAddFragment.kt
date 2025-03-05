package example1.app.artApp.artBookView

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gokmenmutlu.artbookkotlin.R
import com.gokmenmutlu.artbookkotlin.databinding.ArtAddFragmentBinding
import example1.app.artApp.model.ArtModel
import example1.app.artApp.util.ArtInterface
import example1.app.artApp.util.ImageUtils
import example1.app.artApp.util.UtilityFunctions
import example1.app.viewModels.ArtViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ArtAddFragment : Fragment(), ArtInterface {

    private var _binding: ArtAddFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var permissionLauncher: ActivityResultLauncher<String> // izinler String oldugu icin.
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private var tempImage : Drawable? = null
    private val artViewModel: ArtViewModel by viewModels()

    private var selectedByteArray: ByteArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       registerLauncher()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = ArtAddFragmentBinding.inflate(inflater, container,false)
        val view = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val oldArtModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable<ArtModel>("oldArtModel", ArtModel::class.java)
        } else {
            arguments?.getParcelable("oldArtModel") // Deprecated !!!
        }
        oldArtModel?.let {
            artViewModel.setArtModel(it)
            getOldArtModel()
        }

        selectImageFromGallery()    // Permission
        tempImage = binding.imageViewAddFromGallery.drawable

        binding.editTextDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.buttonSave.setOnClickListener{
            onSaveArt()
        }
        binding.buttonUpdate.setOnClickListener{
            oldArtModel?.let {
                onUpdateArt(it)
            }
        }
        binding.buttonDelete.setOnClickListener{
            oldArtModel?.let {
                onDeleteArt(it)
            }
        }
        binding.imageViewZoomIn.setOnClickListener {
            oldArtModel?.let {
                val bundle = Bundle()
                val navController = findNavController()
                bundle.putByteArray("imageByteArray",it.imageByteArray)
                navController.navigate(R.id.action_artAddFragment_to_fullScreenFragment,bundle)
            }
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


    private fun selectImageFromGallery() {

        binding.imageViewAddFromGallery.setOnClickListener {
            checkPermissions()
        }
    }
    private fun checkPermissions() {
       val  galleryPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if(galleryPermission == PackageManager.PERMISSION_GRANTED) {
            openGallery()   // izin verildi.
        }
        else {

            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_MEDIA_IMAGES)) {
              showRationaleDialog()
            }
        else {
                requestPermission()
            }

        }


    }


    private fun requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }

    private fun showRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Requires access to your gallery to select images")
            .setPositiveButton("OK") { _, _ ->
                requestPermission()     // -> Rationale mesajı göründükten sonra OK a basarsa tekrar izin iste.
            }
            .setNegativeButton("Cancel",null)
            .create()
            .show()
    }

    private fun openGallery() {

        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        activityResultLauncher.launch(galleryIntent)

    }

    private fun registerLauncher() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                isGranted ->
            if(isGranted) {
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
                openGallery()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == AppCompatActivity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {

                    selectedByteArray = ImageUtils.getImageByteArray(it,requireContext(),300)

                  binding.imageViewAddFromGallery.setImageURI(it)
                }
            }
        }
    }

    private fun showDatePickerDialog() {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Seçilen tarihi EditText'e yaz
                val date = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
                binding.editTextDate.setText(date)  // selectedMonth+1 because Month start 0 in DatePickerDialog
            },
            year,
            month,
            day )
        datePickerDialog.show()

    }

    private fun getOldArtModel() {

            // ViewModel'deki artModel değiştikçe UI'yi güncelle
            artViewModel.artModel.observe(viewLifecycleOwner, Observer { model ->
                model?.let {
                    binding.editTextName.setText(model.location)
                    binding.editTextDate.setText(it.date?.let { date ->
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
                    } ?: "Unknown")
                    binding.buttonSave.visibility = View.INVISIBLE
                    binding.buttonUpdate.visibility = View.VISIBLE
                    binding.buttonDelete.visibility = View.VISIBLE
                    binding.imageViewZoomIn.visibility = View.VISIBLE

                    val image = binding.imageViewAddFromGallery
                    image.isClickable = false

                        ImageUtils.setImageFromByteArray(it.imageByteArray,image)

                    model.note?.let { note ->
                        binding.editTextTextNote.setText(note)
                    }


                }
            })
        }

    private fun goMenuNavController() {
        val navController = findNavController()
        navController.apply {
            navigate(R.id.action_artAddFragment_to_artBookFragment)
            popBackStack(R.id.artAddFragment, true)
        }
    }

    private fun onSaveArt() {
        val locationName : String? =  binding.editTextName.text.toString().takeIf { it.isNotBlank() }

        if(binding.imageViewAddFromGallery.drawable != null &&
            binding.imageViewAddFromGallery.drawable != tempImage &&
            !locationName.isNullOrEmpty() && !(binding.editTextDate.text.isNullOrEmpty()) && selectedByteArray != null) {
            // BOŞ DEGIL ISE KAYDET. Veritabanı DAO ya ve RecyclerView da çağırıp göster.

            val note: String? = binding.editTextTextNote.text.toString().takeIf { it.isNotBlank() }
            val date = UtilityFunctions.dateToLong(binding.editTextDate.text.toString())

            val saveArt = ArtModel(locationName, selectedByteArray, date = date, note = note)

            artViewModel.insertArt(saveArt)

            Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()

            goMenuNavController()

        } else {
            Toast.makeText(requireContext(), "Please Enter Correctly", Toast.LENGTH_LONG).show()
        }
    }

    private fun onUpdateArt(art: ArtModel) {
        art.location = binding.editTextName.text.toString()
        val date = UtilityFunctions.dateToLong(binding.editTextDate.text.toString())
        art.date = date
        art.note = binding.editTextTextNote.text.toString()

        artViewModel.updateArt(art)
        Toast.makeText(requireContext(), "Updated.", Toast.LENGTH_SHORT).show()

        goMenuNavController()
    }

    override fun onDeleteArt(art: ArtModel) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete")
        builder.setMessage("Do you want to delete this item?")

        builder.setPositiveButton("Yes") { _, _ ->

            artViewModel.deleteArt(art)
            Toast.makeText(requireContext(), "Deleted.", Toast.LENGTH_SHORT).show()

            goMenuNavController()
        }

        builder.setNegativeButton("No", null)
        builder.show()
    }


}



