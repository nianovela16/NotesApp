package com.android.notesapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.notesapp.model.NoteData

@Database(version = 1,entities = [NoteData::class],exportSchema = false)
abstract class MyDatabase: RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "notesapp"
        const val TABLE_NOTE = "notes"
    }
    abstract fun notesDao(): NotesDao
}