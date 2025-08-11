package tj.ikrom.cinemahall.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tj.ikrom.cinemahall.R
import tj.ikrom.cinemahall.databinding.FragmentSeatsBinding
import tj.ikrom.cinemahall.ui.viewmodel.SeatsViewModel

@AndroidEntryPoint
class SeatsFragment : Fragment() {

    private var _binding: FragmentSeatsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SeatsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.seats.collect { response ->
                    binding.seatsText.text = response?.mapHeight.toString() ?: "Нет данных"
                }
            }
        }

        viewModel.loadSeats()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
