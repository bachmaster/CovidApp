package pe.edu.ulima.pm.covidapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pe.edu.ulima.pm.covidapp.room.dao.PersonaRoomDAO
import pe.edu.ulima.pm.covidapp.room.models.PersonaRoom

@Database(entities = [PersonaRoom::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getPersonaRoomDAO() : PersonaRoomDAO

    companion object {
        private var mInstance : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase {
            if(mInstance == null){
                mInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "POSITIVOS"
                ).allowMainThreadQueries().build()
            }
            return mInstance!!
        }
    }
}