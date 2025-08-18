package tj.ikrom.cinemahall.domain.repositories

import kotlinx.coroutines.flow.Flow
import tj.ikrom.cinemahall.data.database.entity.HistoryEntity
import tj.ikrom.cinemahall.data.network.model.SeatsResponse

interface SeatsRep {

    suspend fun getSeats() : Flow<SeatsResponse?>

    suspend fun insertHistory(historyEntity: HistoryEntity)

    fun getAllHistory(): Flow<List<HistoryEntity>>

    suspend fun deleteHistory(historyEntity: List<HistoryEntity>)

}