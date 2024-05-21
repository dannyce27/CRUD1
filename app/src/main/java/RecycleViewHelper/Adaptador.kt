package RecyclerViewHelper

import Daniel.Soriano.crud_daniel_soriano.R
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.claseConexion
import modelo.dataClassMascotas
import java.sql.PreparedStatement


class Adaptador(private var Datos: List<dataClassMascotas>) : RecyclerView.Adapter<ViewHolder>() {

    fun  actualizarLista(nuevaLista: List<dataClassMascotas>){
        Datos = nuevaLista
        //esto es lo que notifica al recycleView que hay nuevos datos agregados a la base de datos
        notifyDataSetChanged()
    }
    fun EliminarDatos(nombreMascota: String, posicion: Int){
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO) {
            val objConnection = claseConexion().cadenaConexion()

            //2- Crear una variable que contenga un PrepareStatement
            val deleteMascotas = objConnection?.prepareStatement("Delete From tbMascota where nombreMascota = ?")!!
            deleteMascotas.setString(1, nombreMascota)
            deleteMascotas.executeUpdate()

            val commit = objConnection.prepareStatement("commit")!!
            commit.executeUpdate()


        }
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_card_item, parent, false)

        return ViewHolder(vista)
    }
    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = Datos[position]
        holder.textView.text = producto.nombreMascota

        holder.btnEliminar.setOnClickListener {
            val context = holder.itemView.context
            val Builder = AlertDialog.Builder(context)

            Builder.setTitle("Eliminar")
            Builder.setMessage("¿Desea eliminar la mascota?")

            //botones

            Builder.setPositiveButton("si"){dialog, switch -> EliminarDatos(producto.nombreMascota, position)}

            Builder.setNegativeButton("No"){dialog, switch -> dialog.dismiss()}


            val dialog = Builder.create()
            dialog.show()
        }
    }




}
