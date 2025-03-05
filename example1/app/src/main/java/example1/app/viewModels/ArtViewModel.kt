package example1.app.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import example1.app.artApp.model.ArtModel
import example1.app.artApp.room.ArtDao
import example1.app.artApp.room.ArtDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtViewModel(application: Application) : AndroidViewModel(application) {

    private val artDao: ArtDao = ArtDatabase.getInstance(application).artDao()

    val allArts : LiveData<List<ArtModel>> = artDao.getAll()

    private val _artModel = MutableLiveData<ArtModel>()
    val artModel: LiveData<ArtModel> get() = _artModel

    private val _searchResults = MutableLiveData<List<ArtModel>>()
    val searchResults: LiveData<List<ArtModel>> get() = _searchResults

    fun insertArt(art: ArtModel) {
        viewModelScope.launch(Dispatchers.IO) {
            artDao.insertArt(art)
        }
    }

    fun deleteArt(art: ArtModel) {
        viewModelScope.launch(Dispatchers.IO){
            artDao.deleteArt(art)
        }
    }

    fun updateArt(art: ArtModel) {
        viewModelScope.launch(Dispatchers.IO) {
            artDao.updateArt(art)
        }
    }

    fun setArtModel(art: ArtModel) { // for Update
        _artModel.value = art
    }

   fun searchArt(searchArtLocationName: String)  {
       viewModelScope.launch(Dispatchers.IO) {
           val results = artDao.searchLocation("%$searchArtLocationName%")
           _searchResults.postValue(results)
       }
    }



}