package com.astro.vajra.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Room database for chart persistence
 */
@Database(
    entities = [
        ChartEntity::class
    ],
    version = 6,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ChartDatabase : RoomDatabase() {
    abstract fun chartDao(): ChartDao

    companion object {
        @Volatile
        private var INSTANCE: ChartDatabase? = null

        /**
         * Migration from version 1 to 2: Add gender column to charts table
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE charts ADD COLUMN gender TEXT NOT NULL DEFAULT 'PREFER_NOT_TO_SAY'")
            }
        }

        /**
         * Legacy migration placeholder kept for users upgrading from old app versions.
         */
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) = Unit
        }

        /**
         * Legacy migration placeholder kept for users upgrading from old app versions.
         */
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) = Unit
        }

        /**
         * Migration from version 4 to 5: Add indices to charts table for query performance
         * - createdAt: Used for ordering charts (getAllCharts)
         * - name: Used for searching charts (searchCharts)
         * - location: Used for searching charts (searchCharts)
         */
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE INDEX IF NOT EXISTS index_charts_createdAt ON charts(createdAt)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_charts_name ON charts(name)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_charts_location ON charts(location)")
            }
        }

        /**
         * Migration from version 5 to 6: Remove legacy assistant tables and indices.
         */
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS chat_messages")
                db.execSQL("DROP TABLE IF EXISTS chat_conversations")
                db.execSQL("DROP INDEX IF EXISTS index_chat_messages_conversationId")
                db.execSQL("DROP INDEX IF EXISTS index_chat_messages_createdAt")
            }
        }

        fun getInstance(context: Context): ChartDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChartDatabase::class.java,
                    "astrovajra_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
