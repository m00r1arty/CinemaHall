package tj.ikrom.cinemahall.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dagger.hilt.android.qualifiers.ApplicationContext
import tj.ikrom.cinemahall.data.database.dao.PaymentDao
import tj.ikrom.cinemahall.data.database.entity.PaymentEntity
import tj.ikrom.cinemahall.data.database.entity.converter.PaymentConverter

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        PaymentEntity::class,
    ]
)
@TypeConverters(PaymentConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun paymentDao(): PaymentDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(@ApplicationContext context: Context) = instance?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().setJournalMode(JournalMode.TRUNCATE).build()
    }
}
