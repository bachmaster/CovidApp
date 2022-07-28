package pe.edu.ulima.pm.covidapp

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Visibility
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import pe.edu.ulima.pm.covidapp.adapters.ListadoDepartamentosAdapter
import pe.edu.ulima.pm.covidapp.models.GestorPersona
import pe.edu.ulima.pm.covidapp.models.beans.Departamento
import java.util.*
import kotlin.time.Duration.Companion.days

class DataActivity : AppCompatActivity() {

    private var rViewDepartamentos : RecyclerView? = null
    private var txtEditFecha : EditText? = null
    private var txtNoHayData : TextView? = null
    private var butBuscar : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        val gestor = GestorPersona()
        val sp = getSharedPreferences(Constantes.NOMBRE_SP, Context.MODE_PRIVATE)
        txtEditFecha = findViewById(R.id.txtEditFecha)
        txtNoHayData = findViewById(R.id.txtNohaydata)
        rViewDepartamentos = findViewById(R.id.rViewDepartamentos)
        butBuscar = findViewById(R.id.butBuscar)

        // Verificar si ya se introdujo fecha
        val fechaSP = sp.getBoolean(Constantes.FECHA_IMPLEMENTADA, false)
        if (!fechaSP) {

            // Preguntar por introducir fecha
            mostrarDialogPasivo("Recordatorio", "Introduzca una fecha para empezar")

        }
        else{
            val fechaSelect = sp.getString(Constantes.FECHA_ACTUAL, "20220621")
            val year = fechaSelect!!.take(4)
            val month = fechaSelect.subSequence(4,6)
            val day = fechaSelect.takeLast(2)
            txtEditFecha!!.setText("$day/$month/$year")
            conexionRoom(gestor, sp)
        }

        // Introducir fecha
        txtEditFecha!!.setOnClickListener{
            activarSelectorTexto(gestor ,sp)
        }

        butBuscar!!.setOnClickListener{
            conexionRoom(gestor, sp)
        }
    }

    private fun mostrarDialogPasivo(titulo: String, cuerpo: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(titulo)
        dialog.setMessage(cuerpo)
        dialog.setPositiveButton("OK") { dialog, which -> }
        dialog.setCancelable(false)
        dialog.show()
    }
    private fun activarSelectorTexto(gestor: GestorPersona, sp: SharedPreferences) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Seleccione una fecha")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = Date(it)
            val year = calendar.get(Calendar.YEAR).toString()
            var month = (calendar.get(Calendar.MONTH)+1).toString()
            var day = calendar.get(Calendar.DAY_OF_MONTH).toString()
            if (month.length == 1) {
                month = "0$month"
            }
            if (day.length == 1) {
                day = "0$day"
            }
            sp.edit().putString(Constantes.FECHA_ACTUAL, year + month + day).commit()
            sp.edit().putBoolean(Constantes.FECHA_IMPLEMENTADA, true).commit()
            txtEditFecha!!.setText("$day/$month/$year")
            conexionRoom(gestor, sp)
        }
        datePicker.show(supportFragmentManager, "Fecha")
    }
    private fun conexionRoom(gestor: GestorPersona, sp: SharedPreferences) {
        var listaDepartamentos: List<Departamento>
        val fecha = sp.getString(Constantes.FECHA_ACTUAL, "20220621")
        listaDepartamentos = gestor.contarPorDepartamento(applicationContext, fecha!!)

        if(listaDepartamentos.isEmpty()){
            txtNoHayData!!.visibility = View.VISIBLE
            txtNoHayData!!.text = "No hay data para mostrar"
        }
        else{
            txtNoHayData!!.visibility = View.GONE
            txtNoHayData!!.text = ""
        }
        val adapter = ListadoDepartamentosAdapter(listaDepartamentos)
        rViewDepartamentos!!.adapter = adapter
    }
}
