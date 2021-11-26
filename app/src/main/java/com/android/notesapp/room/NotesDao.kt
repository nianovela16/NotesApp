package com.android.notesapp.room

import androidx.room.*
import com.android.notesapp.model.NoteData

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNote(note: NoteData)

    @Query("DELETE FROM ${MyDatabase.TABLE_NOTE} WHERE id = :noteId")
    fun deleteNote(noteId: Long): Int

    @Query("SELECT * FROM ${MyDatabase.TABLE_NOTE} ORDER BY id DESC")
    fun getNotes(): List<NoteData>
}