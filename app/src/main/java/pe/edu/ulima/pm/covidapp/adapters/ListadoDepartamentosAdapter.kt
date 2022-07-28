package pe.edu.ulima.pm.covidapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import pe.edu.ulima.pm.covidapp.R
import pe.edu.ulima.pm.covidapp.models.beans.Departamento

class ListadoDepartamentosAdapter(private val mListaDepartamentos : List<Departamento>)
    : RecyclerView.Adapter<ListadoDepartamentosAdapter.ViewHolder>(){
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val tviDepartamentosNombre : TextView
        val tviDepartamentosCounter : TextView

        init {
            tviDepartamentosNombre = view.findViewById(R.id.txtNombreDepartamento)
            tviDepartamentosCounter = view.findViewById(R.id.txtDepartamentoContador)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_departamento, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val departamento = mListaDepartamentos[position]
        holder.tviDepartamentosNombre.text = departamento.DEPARTAMENTO
        holder.tviDepartamentosCounter.text = "" + departamento.CUENTA

    }

    override fun getItemCount(): Int {
        return mListaDepartamentos.size
    }
}