package be.nane.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.nane.R
import be.nane.models.Animal

class AnimalViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
    val tvName : TextView = itemView.findViewById(R.id.tv_name)
    val tvType : TextView = itemView.findViewById(R.id.tv_type)
    val tvWeight : TextView = itemView.findViewById(R.id.tv_weight)

    fun bind(animal: Animal) {
        tvName.text = "${animal.name}"
        tvType.text = "${animal.type}"
        if (animal.weight < 1000){
            tvWeight.text = "${animal.weight} g"
        } else {
            tvWeight.text = "${animal.weight/1000.0} kg"
        }
    }
}