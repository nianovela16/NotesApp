package com.android.notesapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.notesapp.room.MyDatabase
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(
    tableName = MyDatabase.TABLE_NOTE
)
data class NoteData(
    @PrimaryKey(autoGenerate = true) @Json(name = "id") var id: Long = 0,
    @Json(name = "title") var title: String = "",
    @Json(name = "content") var content: String = ""
)
