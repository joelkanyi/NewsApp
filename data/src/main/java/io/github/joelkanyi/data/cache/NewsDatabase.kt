/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NewsEntity::class],
    version = 1,
    exportSchema = true
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
