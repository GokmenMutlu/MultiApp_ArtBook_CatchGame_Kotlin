package example1.app.artApp.util


import java.text.SimpleDateFormat
import java.util.Locale

object UtilityFunctions {

     fun dateToLong(dateString: String) : Long? {
        val selectedDate: String = dateString
        if(selectedDate.isNotEmpty()) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return dateFormat.parse(selectedDate)?.time
        }
        return -1 // Boş gelme ihtimali olmadığı için ekstra durum yazmadık. Çünkü tarih tıklanıp seçilebiliyor sadece.
    }

}
