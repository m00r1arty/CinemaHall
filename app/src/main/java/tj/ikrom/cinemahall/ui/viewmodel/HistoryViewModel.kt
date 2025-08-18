package tj.ikrom.cinemahall.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tj.ikrom.cinemahall.data.database.entity.HistoryEntity
import tj.ikrom.cinemahall.domain.repositories.SeatsRep
import javax.inject.Inject


@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val seatsRep: SeatsRep
) : ViewModel() {

    private val _allHistories = MutableStateFlow<List<HistoryEntity>>(emptyList())
    val allHistories: StateFlow<List<HistoryEntity>> = _allHistories

    init {
        viewModelScope.launch {
            seatsRep.getAllHistory()
                .collect { payments ->
                    _allHistories.value = payments
                }
        }
    }

    fun deletePayment(historyEntity: List<HistoryEntity>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                seatsRep.deleteHistory(historyEntity)
            }
        }
    }
}
