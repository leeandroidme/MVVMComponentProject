package com.newland.register.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.newland.register.db.dao.HistoryDao
import com.newland.register.db.entity.History

/**
 * @author: leellun
 * @data: 17/6/2021.
 *
 */
@Database(entities = [History::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        private val DB_NAME = "tb_history.db"

        @Volatile
        private var INSTANCE: HistoryDatabase? = null
        public fun getInstance(context: Context): HistoryDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context): HistoryDatabase {
            return Room.databaseBuilder(context, HistoryDatabase::class.java, DB_NAME).build();
        }
    }
}