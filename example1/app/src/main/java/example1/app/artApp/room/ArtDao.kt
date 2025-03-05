package example1.app.artApp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import example1.app.artApp.model.ArtModel

@Dao
interface ArtDao {

    @Query("SELECT * FROM art_table ORDER BY date DESC") // En yeni tarihten başlayıp eski tarihe göre sırala
    fun getAll(): LiveData<List<ArtModel>>

    @Query("SELECT * FROM art_table WHERE location LIKE :location")
    suspend fun searchLocation(location: String) : List<ArtModel>

    @Insert
    suspend fun insertArt(art: ArtModel)

    @Delete
    suspend fun deleteArt(art: ArtModel)

    @Update
    suspend fun updateArt(art: ArtModel)

}