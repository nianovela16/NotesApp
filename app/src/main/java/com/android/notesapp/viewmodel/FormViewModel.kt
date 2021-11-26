package com.android.notesapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.notesapp.model.NoteData
import com.android.notesapp.repo.NoteRepo

class FormViewModel: ViewModel() {
    fun saveNote(context: Context, note: NoteData): LiveData<Boolean> =
        NoteRepo.saveNote(context, note)
    fun deleteNote(context: Context, noteId: Long): LiveData<Int> =
        NoteRepo.deleteNote(context, noteId)
}