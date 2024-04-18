package uk.ac.tees.mad.w9642833.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class StartingDestinationViewModel: ViewModel() {

    var _startingDestination = MutableStateFlow<Destinations>(Destinations.PreAuth)
        private set

    fun setsStartingDestination(
        startingDestinations: Destinations
    ){
        _startingDestination.value = startingDestinations
    }
}