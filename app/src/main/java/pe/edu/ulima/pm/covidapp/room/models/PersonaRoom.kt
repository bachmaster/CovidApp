package pe.edu.ulima.pm.covidapp.room.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class PersonaRoom (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name = "Fecha_corte")
    val fecha_corte : String,
    @ColumnInfo(name = "DEPARTAMENTO")
    val departamento : String,
    @ColumnInfo(name = "Provincia")
    val provincia : String,
    @ColumnInfo(name = "Distrito")
    val distrito : String,
    @ColumnInfo(name = "Metodox")
    val metodox : String,
    @ColumnInfo(name = "Edad")
    val edad : Int,
    @ColumnInfo(name = "Sexo")
    val sexo : String,
    @ColumnInfo(name = "Fecha_Resultado")
    val fecha_resultado : String,
    @ColumnInfo(name = "Ubigeo")
    val ubigeo : Int,
    @ColumnInfo(name = "Id_persona")
    val id_persona : Int
)
