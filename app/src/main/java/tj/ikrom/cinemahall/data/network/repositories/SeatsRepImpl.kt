package tj.ikrom.cinemahall.data.network.repositories

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import tj.ikrom.cinemahall.data.network.api.Api
import tj.ikrom.cinemahall.data.network.model.SeatsResponse
import tj.ikrom.cinemahall.domain.repositories.SeatsRep
import javax.inject.Inject

class SeatsRepImpl @Inject constructor(
    private val api: Api,
) : SeatsRep {
    override suspend fun getSeats(): Flow<SeatsResponse> = flow<SeatsResponse> {

    }.flowOn(IO)
}