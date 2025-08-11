package tj.ikrom.cinemahall.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tj.ikrom.cinemahall.data.network.model.SeatsResponse
import tj.ikrom.cinemahall.domain.repositories.SeatsRep
import javax.inject.Inject

@HiltViewModel
class SeatsViewModel @Inject constructor(
    private val seatsRep: SeatsRep,
): ViewModel() {
    private val _seats = MutableStateFlow<SeatsResponse?>(null)
    val seats: StateFlow<SeatsResponse?> = _seats

    fun loadSeats() {
        viewModelScope.launch {
            seatsRep.getSeats().collect { response ->
                _seats.value = response
            }
        }
    }
}