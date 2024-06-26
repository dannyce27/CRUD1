package RecyclerViewHelper

import Daniel.Soriano.crud_daniel_soriano.R
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView = view.findViewById(R.id.txtMascotaCard)
    val btnEditar: ImageView = view.findViewById(R.id.btnEditar)
    val btnEliminar: ImageView = view.findViewById(R.id.btnEliminar)
}
