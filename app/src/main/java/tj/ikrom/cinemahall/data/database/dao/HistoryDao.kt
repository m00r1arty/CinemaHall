package tj.ikrom.cinemahall.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tj.ikrom.cinemahall.data.database.entity.HistoryEntity

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(salomEntity: HistoryEntity)

    @Delete
    fun deleteHistory(historyEntity: List<HistoryEntity>)

    @Query("SELECT * FROM history;")
    fun getAllHistory(): List<HistoryEntity>
}