package com.liyawei.nbateamviewer.data

import androidx.lifecycle.LiveData
import com.liyawei.nbateamviewer.model.Team
import com.liyawei.nbateamviewer.network.INetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class DataRepository(private val network: INetworkClient, private val teamDao: TeamDao) {

    open val teams: LiveData<List<Team>> by lazy {
        teamDao.loadTeams()
    }

     open suspend fun refreshTeams() {
        val result = network.getTeams(Dispatchers.IO).await()
        withContext(Dispatchers.IO) {
            try {
                result?.let {
                    teamDao.insertTeams(it)
                }
            } catch (e: Exception) {
                throw e
            }

        }
    }
}