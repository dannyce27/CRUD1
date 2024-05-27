package RecyclerViewHelper

import Daniel.Soriano.crud_daniel_soriano.R
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.claseConexion
import modelo.dataClassMascotas
import java.sql.PreparedStatement
import java.util.UUID


class Adaptador(private var Datos: List<dataClassMascotas>) : RecyclerView.Adapter<ViewHolder>() {

    fun  actualizarLista(nuevaLista: List<dataClassMascotas>){
        Datos = nuevaLista
        //esto es lo que notifica al recycleView que hay nuevos datos agregados a la base de datos
        notifyDataSetChanged()
    }

    fun actualizarPantalla(uuid: String, nuevoNombre: String){
        val index = Datos.indexOfFirst { it.uuid == uuid }
        Datos[index].nombreMascota == nuevoNombre
        notifyDataSetChanged()
    }

    ///////TODO: ELIMINAR DATOS
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

    //////TODO: ACTUALIZAR DATOS
    fun actualizarDatos(nuevoNombre: String, uuid: String){
        GlobalScope.launch(Dispatchers.IO) {
            ///1- Creo un obj de la clase conexion
            val objConexion = claseConexion().cadenaConexion()

            //2- Creo una variable que tenga un prepareStatement
            val updateMascota = objConexion?.prepareStatement("update tbMascota set nombreMascota = ? where uuid = ?")!!
            updateMascota.setString(1, nuevoNombre)
            updateMascota.setString(2, uuid)

            updateMascota.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

        }

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




        holder.btnEditar.setOnClickListener{


            val context = holder.itemView.context
            val Builder = AlertDialog.Builder(context)

            val cuadroTexto = EditText(context)
            cuadroTexto.setHint(producto.nombreMascota)
            Builder.setView(cuadroTexto)

            Builder.setTitle("Editar")
            Builder.setMessage("¿Desea editar esta mascota?")

            Builder.setPositiveButton("Actualizar"){
                dialog, switch -> actualizarDatos(cuadroTexto.text.toString(), producto.uuid)
            }

            Builder.setNegativeButton("Cancelar"){
                dialog, switch -> dialog.dismiss()
            }

            val dialog = Builder.create()
            dialog.show()
        }
    }




}
