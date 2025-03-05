package example1.app.artApp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "art_table")
data class ArtModel(
    var location: String,
    var imageByteArray: ByteArray?,
    var date: Long?,
    var note: String? = null,
    @PrimaryKey(autoGenerate = true) var uid: Int = 0
) :Parcelable
