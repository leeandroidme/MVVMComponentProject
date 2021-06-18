package com.newland.register.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.newland.register.db.entity.History

/**
 * @author: leellun
 * @data: 17/6/2021.
 *
 */
@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: History)

    @Query("SELECT * FROM t_history WHERE user_id=:user_id")
    suspend fun queryHistorys(user_id: String): History
}