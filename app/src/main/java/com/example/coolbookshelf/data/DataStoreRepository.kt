package com.example.coolbookshelf.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.example.coolbookshelf.util.Constants.Companion.DEFAULT_PRINT_TYPE
import com.example.coolbookshelf.util.Constants.Companion.DEFAULT_Q_KIND
import com.example.coolbookshelf.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.example.coolbookshelf.util.Constants.Companion.PREFERENCES_NAME
import com.example.coolbookshelf.util.Constants.Companion.PREFERENCES_PRINT_TYPE
import com.example.coolbookshelf.util.Constants.Companion.PREFERENCES_PRINT_TYPE_ID
import com.example.coolbookshelf.util.Constants.Companion.PREFERENCES_Q_ID
import com.example.coolbookshelf.util.Constants.Companion.PREFERENCES_Q_KIND
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context) {

    private object PreferencesKeys {
        val selectedBookKind = preferencesKey<String>(PREFERENCES_Q_KIND)
        val selectedBookKindId = preferencesKey<Int>(PREFERENCES_Q_ID)
        val backOnline= preferencesKey<Boolean>(PREFERENCES_BACK_ONLINE)
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(name = PREFERENCES_NAME)

    suspend fun saveBackOnline(backOnline: Boolean){
        dataStore.edit{preferences ->
            preferences[PreferencesKeys.backOnline] = backOnline
        }
    }

    suspend fun saveBookKind(
        bookKind: String,
        bookKindId: Int
    ) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.selectedBookKind] = bookKind
            preferences[PreferencesKeys.selectedBookKindId] = bookKindId
        }

    }

    val readBookType: Flow<BookKind> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedBookKind = preferences[PreferencesKeys.selectedBookKind] ?: DEFAULT_Q_KIND
            val selectedBookKindId = preferences[PreferencesKeys.selectedBookKindId] ?: 0
            BookKind(
                selectedBookKind,
                selectedBookKindId
            )
        }


    val readBackOnline: Flow<Boolean> = dataStore.data
        .catch{exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map{
                preferences-> val backOnline = preferences[PreferencesKeys.backOnline] ?: false
            backOnline
        }
}

data class BookKind(
    val selectedBookKind: String,
    val selectedBookKindId: Int
)


