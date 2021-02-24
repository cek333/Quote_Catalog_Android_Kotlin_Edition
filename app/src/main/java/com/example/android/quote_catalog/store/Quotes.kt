package com.example.android.quote_catalog.store

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.quote_catalog.DEFAULT_BG_COLOR
import com.example.android.quote_catalog.DEFAULT_TXT_COLOR

@Entity (tableName = "quotes")
data class Quote(
  @PrimaryKey(autoGenerate = true)
  val quoteId: Long = 0L,
  val quoteText: String = "",
  val bgColor: Int = DEFAULT_BG_COLOR,
  val txtColor: Int = DEFAULT_TXT_COLOR,
  val fileName : String = ""
)

@Dao
interface QuoteDatabaseDao {
  @Insert
  suspend fun insert(quote: Quote)

  @Delete
  suspend fun delete(quote: Quote)

  @Query("SELECT * FROM quotes WHERE quoteText LIKE :searchTerm")
  fun queryQuotes(searchTerm: String) : LiveData<List<Quote>>

  @Query("SELECT * FROM quotes")
  fun getAllQuotes() : LiveData<List<Quote>>
}

@Database(entities = [Quote::class], version = 1, exportSchema = false)
abstract class QuoteDatabase : RoomDatabase() {
  abstract val quoteDatabaseDao: QuoteDatabaseDao

  companion object {
    @Volatile
    private var INSTANCE: QuoteDatabase? = null

    fun getInstance(context: Context): QuoteDatabase {
      synchronized(this) {
        var instance = INSTANCE

        if (instance == null) {
          instance = Room.databaseBuilder(
            context.applicationContext,
            QuoteDatabase::class.java,
            "sleep_history_database"
          )
          .fallbackToDestructiveMigration()
          .build()
          INSTANCE = instance
        }
        return instance
      }
    }
  }
}