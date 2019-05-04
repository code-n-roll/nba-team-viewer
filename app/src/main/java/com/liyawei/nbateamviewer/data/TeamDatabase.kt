package com.liyawei.nbateamviewer.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.liyawei.nbateamviewer.model.Team

@Dao
interface TeamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeam(team: Team)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeams(teams: List<Team>)

    @Query("select * from Team")
    fun loadTeams(): LiveData<List<Team>>
}

@Database(entities = [Team::class], version = 1, exportSchema = false)
abstract class TeamDatabase : RoomDatabase() {
    abstract val teamDao: TeamDao
}

private lateinit var INSTANCE: TeamDatabase

/**
 * Instantiate a database from a context.
 */
fun getDatabase(context: Context): TeamDatabase {
    synchronized(TeamDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    TeamDatabase::class.java,
                    "teams_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}