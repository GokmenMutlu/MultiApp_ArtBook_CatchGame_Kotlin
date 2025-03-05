package example1.app.artApp.artBookView

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gokmenmutlu.artbookkotlin.R
import com.gokmenmutlu.artbookkotlin.databinding.ArtFullScreenFragmentBinding
import example1.app.artApp.util.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FullScreenFragment : Fragment() {

    private var _binding: ArtFullScreenFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = ArtFullScreenFragmentBinding.inflate(inflater, container, false)
       val view = binding.root

        val imageByteArray = arguments?.getByteArray("imageByteArray")

        ImageUtils.setImageFromByteArray(imageByteArray,binding.fullScreenImageView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeButton.setOnClickListener {
            val navController = findNavController()
            navController.apply {
                navigate(R.id.artBookFragment)
                popBackStack(R.id.artAddFragment, true)
            }
        }
        binding.saveGalleryButton.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                showSaveConfirmationDialog(binding.fullScreenImageView)
            } else {
                Toast.makeText(requireContext(),"Not available yet.",Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveImageToGallery(imageView: ImageView) {
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val contentResolver = requireContext().contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/YourAppFolder")
        }

        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let { uri ->
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            } ?: throw Exception("Failed to get OutputStream.")
        } ?: throw Exception("Failed to insert image to MediaStore.")
    }

    private fun saveImageToGalleryAsync(imageView: ImageView) {
        // Coroutine içinde çalıştır
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    saveImageToGallery(imageView) // Kaydetme işlemi
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Image Saved!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Failed to save image: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showSaveConfirmationDialog(imageView: ImageView) {
        AlertDialog.Builder(requireContext())
            .setTitle("Save Image")
            .setMessage("Do you want to save this image to gallery?")
            .setPositiveButton("Yes") { _, _ ->
                saveImageToGalleryAsync(imageView)  // Kaydetme işlemini çağır
            }
            .setNegativeButton("No", null)
            .create()
            .show()
    }





}