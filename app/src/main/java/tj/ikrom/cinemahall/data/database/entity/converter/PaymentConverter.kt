package tj.ikrom.cinemahall.data.database.entity.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tj.ikrom.cinemahall.data.network.model.Seat

class PaymentConverter {

    @TypeConverter
    fun fromString(value: String): List<Seat> {
        val listType = object : TypeToken<List<Seat>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Seat>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}