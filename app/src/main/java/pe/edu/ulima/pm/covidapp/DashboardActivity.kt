package pe.edu.ulima.pm.covidapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.edu.ulima.pm.covidapp.models.GestorPersona
import pe.edu.ulima.pm.covidapp.models.beans.Persona

class DashboardActivity : AppCompatActivity() {

    var customProgressBar : Dialog? = null
    var btnSincronizar : Button? = null
    var btnLimpiar : Button? = null
    var btnVerData : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Declaración de Variables Globales
        val gestor = GestorPersona()
        val sp = getSharedPreferences(Constantes.NOMBRE_SP, Context.MODE_PRIVATE)

        // Declaración de Botones
        btnSincronizar = findViewById(R.id.btnSincronizar)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        btnVerData = findViewById(R.id.btnVerData)

        customProgressBar = Dialog(this)
        // Button Launcher Pack
        btnSincronizar!!.setOnClickListener{
            startSync(gestor, sp)
        }
        btnLimpiar!!.setOnClickListener {
            clearData(gestor, sp)
        }
        btnVerData!!.setOnClickListener {
            seeData()
        }
    }

    // Función de Sincronización
    fun startSync(gestor : GestorPersona, sp : SharedPreferences) {

        GlobalScope.launch(Dispatchers.Main) {
            val estaSincronizado = sp.getBoolean(Constantes.SP_ESTA_SINCRONIZADO, false)
            var lista: List<Persona>
            if(!estaSincronizado) {

                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
                customProgressBar!!.setContentView(R.layout.dialog_custom_progress)
                customProgressBar!!.setCancelable(false)
                customProgressBar!!.show()


                    Log.i(null, "ProgressBar activo")
                    lista = withContext(Dispatchers.IO) {
                        gestor.obtenerListaPersonasCorrutinas(applicationContext)
                    }

                    // Guardando datos en Room
                    Log.d("Personas", lista.size.toString())
                    sp.edit().putBoolean(
                        Constantes.SP_ESTA_SINCRONIZADO, true
                    ).commit()

                btnSincronizar!!.isClickable = false
                customProgressBar!!.dismiss()
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR

                Log.i(null, "La tocó")
            } else {
                btnSincronizar!!.isClickable = false
                // Obteniendo datos de Room

                lista = gestor.obtenerListaPersonasRoom(applicationContext)
                println(lista.toString())

            }
        }
    }
    // Función de Limpieza de Datos
    fun clearData(gestor: GestorPersona, sp: SharedPreferences) {
        gestor.eliminarListaPersonasRoom(applicationContext)
        btnSincronizar!!.isClickable = true
        sp.edit().putBoolean(Constantes.SP_ESTA_SINCRONIZADO, false).commit()

        val toast = Toast.makeText(applicationContext, "La BD Room se eliminó correctamente", Toast.LENGTH_SHORT)
        toast.show()
    }
    // Función de Ver Datos
    fun seeData() {
        startActivity(Intent(this, DataActivity::class.java))
    }
}