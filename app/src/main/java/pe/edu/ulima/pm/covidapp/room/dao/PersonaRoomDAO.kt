package pe.edu.ulima.pm.covidapp.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pe.edu.ulima.pm.covidapp.models.beans.Departamento
import pe.edu.ulima.pm.covidapp.models.beans.Persona
import pe.edu.ulima.pm.covidapp.room.models.PersonaRoom

@Dao
interface PersonaRoomDAO {
    @Query("SELECT * FROM PersonaRoom")
    fun getAll() : List<PersonaRoom>

    @Query("SELECT * FROM PersonaRoom WHERE id=:id")
    fun findById(id: Int) : PersonaRoom

    @Insert
    fun insert(persona : PersonaRoom)

    @Delete
    fun delete(persona : PersonaRoom)

    @Query("DELETE FROM PersonaRoom")
    fun dropTable()

    @Query("SELECT DEPARTAMENTO, COUNT(*) AS CUENTA FROM PersonaRoom WHERE Fecha_resultado=:fecha_resultado GROUP BY DEPARTAMENTO")
    fun countbyDepartamento(fecha_resultado : String) : List<Departamento>
}
