package pe.edu.ulima.pm.covidapp.models

import android.content.Context
import android.os.Handler
import android.util.Log

import pe.edu.ulima.pm.covidapp.models.beans.Departamento
import pe.edu.ulima.pm.covidapp.models.beans.Persona
import pe.edu.ulima.pm.covidapp.room.AppDatabase
import pe.edu.ulima.pm.covidapp.room.dao.PersonaRoomDAO
import pe.edu.ulima.pm.covidapp.room.models.PersonaRoom
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class GestorPersona {

    fun obtenerListaPersonasCorrutinas(context: Context) : List<Persona>
    {
        val db = AppDatabase.getInstance(
            context
        )
        val daoPersona : PersonaRoomDAO = db.getPersonaRoomDAO()

        var resultado = mutableListOf<Persona>()
        val url = URL("https://files.minsa.gob.pe/s/eRqxR35ZCxrzNgr/download")
        val urlConnection : HttpURLConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "GET"
        urlConnection.doInput = true

        try {
            urlConnection.connect()
            val response = urlConnection.responseCode
            Log.d(null, "El código de respuesta es: $response")

            if(response == HttpURLConnection.HTTP_OK) {
                //limitar la cantidad de inputs a 1000
                val ingreso : BufferedReader  = urlConnection.inputStream.bufferedReader()
                var inputLine : String?
                var cont = 0
                ingreso.readLine()

                while ((ingreso.readLine().also { inputLine = it }) != null && cont < 1000) {
                    //response.append(inputLine)
                    var ter : MutableList<String> = inputLine!!.split(";") as MutableList<String>
                    if (ter[5] == ""){
                        ter[5] = "0"
                    }
                    if (ter[8] == ""){
                        ter[8] = "0"
                    }
                    if (ter[9] == ""){
                        ter[9] = "0"
                    }

                    var persona = PersonaRoom(0,ter[0],ter[1],ter[2],ter[3],ter[4],ter[5].toInt(),ter[6],ter[7],ter[8].toInt(),ter[9].toInt())
                    daoPersona.insert(persona)

                    cont++
                }
                Log.i(null, "Se terminó de leer la data")
                ingreso.close()
            }

        } finally {
            urlConnection.disconnect()
        }
        return resultado
    }

    fun obtenerListaPersonasRoom(context: Context) : List<Persona> {
        val daoPersona : PersonaRoomDAO = AppDatabase.getInstance(
            context
        ).getPersonaRoomDAO()
        val listaPersonaRoom = daoPersona.getAll()
        println(listaPersonaRoom.size)
        val listaPersonas = listaPersonaRoom.map {
            Persona(it.fecha_corte,it.departamento,it.provincia,it.distrito,it.metodox,it.edad,it.sexo,it.fecha_resultado,it.ubigeo,it.id_persona)

        }
        return listaPersonas
    }

    fun eliminarListaPersonasRoom(context: Context) {
        val daoPersona : PersonaRoomDAO = AppDatabase.getInstance(
            context
        ).getPersonaRoomDAO()
        daoPersona.dropTable()
    }


    fun contarPorDepartamento(context: Context, fecha_resultado : String) : MutableList<Departamento> {
        val db = AppDatabase.getInstance(
            context
        )

        val listaDepartamentos = db.getPersonaRoomDAO().countbyDepartamento(fecha_resultado)

        var listaDepartamentosF = mutableListOf<Departamento>()


        listaDepartamentos.forEach {
            var departamento = Departamento(it.DEPARTAMENTO, it.CUENTA)
            listaDepartamentosF.add(departamento)
        }

        Log.i(null, listaDepartamentos.toString())

        return listaDepartamentosF
    }
}