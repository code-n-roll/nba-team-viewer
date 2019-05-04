package com.liyawei.nbateamviewer.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.liyawei.nbateamviewer.model.Team
import com.liyawei.nbateamviewer.network.NetworkClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TeamViewModel : ViewModel(), CoroutineScope {

    private val viewModelJob = Job()
    private lateinit var teams: MutableLiveData<List<Team>>
    private lateinit var isLoading: MutableLiveData<Boolean>
    private lateinit var showError: MutableLiveData<Boolean>

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + viewModelJob

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getTeams(): LiveData<List<Team>> {
        if (!this::teams.isInitialized) {
            teams = MutableLiveData()
            loadTeams()
        }
        return teams
    }

    fun getIsLoading(): LiveData<Boolean> {
        if (!this::isLoading.isInitialized) {
            isLoading = MutableLiveData()
        }
        return isLoading
    }

    fun shouldShowError(): LiveData<Boolean> {
        if (!this::showError.isInitialized) {
            showError = MutableLiveData()
        }
        return showError
    }

    private fun loadTeams() {
        launch {
            try {
                isLoading.value = true
                val result = NetworkClient.getTeams(Dispatchers.IO)
                result.await()?.let {
                    showError.value = false
                    teams.value = it
                }
            } catch (e: Throwable) {
                showError.value = true
            } finally {
                isLoading.value = false
            }
        }
    }
}