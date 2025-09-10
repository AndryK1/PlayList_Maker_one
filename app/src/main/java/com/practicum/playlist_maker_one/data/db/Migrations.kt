package com.practicum.playlist_maker_one.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("""
                CREATE TABLE playlist_table (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT NOT NULL,
                    uri TEXT NOT NULL,
                    tracksIds TEXT NOT NULL,
                    tracksCount INTEGER NOT NULL
                )
            """)
        }
    }
}