package example1.app.artApp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.gokmenmutlu.artbookkotlin.R
import com.gokmenmutlu.artbookkotlin.databinding.ArtlistRowBinding
import example1.app.artApp.artBookView.ArtAddFragment
import example1.app.artApp.model.ArtModel
import example1.app.artApp.util.ArtInterface
import java.text.SimpleDateFormat
import java.util.Locale

class ArtMainAdapter(private val artInterface: ArtInterface) : RecyclerView.Adapter<ArtMainAdapter.AdapterViewHolder>() {

    private var artList: MutableList<ArtModel> = mutableListOf()


    // ViewHolder sınıfı
    class AdapterViewHolder(private val _binding: ArtlistRowBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding
    }

    // ViewHolder oluşturma
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val binding = ArtlistRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterViewHolder(binding)
    }

    // Item sayısını döndürme
    override fun getItemCount(): Int {
        return artList.size
    }

    // Veriyi bind etme
    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val artModel = artList[position]
        holder.binding.nameText.text = artModel.location
        holder.binding.dateText.text = artModel.date?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
        } ?: "Unknown"

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("oldArtModel", artModel)

            Navigation.findNavController(it).navigate(R.id.action_artBookFragment_to_artAddFragment, args = bundle)
        }
        holder.itemView.setOnLongClickListener {
            artInterface.onDeleteArt(artModel) // Long click olduğunda item'ı geri gönder
            true  // True döndürerek uzun tıklamanın engellenmemesini sağlıyoruz
        }
    }

    // Adapter veri listesini güncelleme metodu
    fun setArtList(newList: List<ArtModel>) {
        artList.clear()
        artList.addAll(newList)

        notifyDataSetChanged() // Veriyi yenile -> Diffutil daha verimli -> Diffutil more efficient
                                // example uygulaması olduğu için bu şekilde kullanıldı.
    }


}