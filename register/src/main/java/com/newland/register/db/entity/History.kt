package com.newland.register.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: leellun
 * @data: 17/6/2021.
 *
 */
@Entity(tableName = "t_history")
data class History(
    @PrimaryKey
    var id: Long,
    var user_id: String,
    var uniqueId: Long = 0,
    var uri: String,
    var title: String,
    var desc: String
)