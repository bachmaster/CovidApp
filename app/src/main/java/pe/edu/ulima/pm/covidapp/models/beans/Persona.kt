package pe.edu.ulima.pm.covidapp.models.beans

import java.util.*

data class Persona (
    val fecha_corte : String,
    val departamento : String,
    val provincia : String,
    val distrito : String,
    val metododx :String,
    val edad : Int,
    val sexo : String,
    val fecha_resultado : String,
    val ubigeo : Int,
    val id_persona : Int
)