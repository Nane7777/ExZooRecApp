package be.nane.viewHolderAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import be.nane.R
import be.nane.activities.AnimalList
import be.nane.models.Animal
import be.nane.viewHolder.AnimalViewHolder

class AnimalAdapter (val animals : List<Animal>) : RecyclerView.Adapter<AnimalViewHolder>() {
    var selectedPosition: Int = RecyclerView.NO_POSITION


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }


    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animals[position]
        holder.bind(animal)

        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.selected_item_background)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.notselected_item_background)
        }

        holder.itemView.setOnClickListener {
            selectedPosition = position
            val activity = holder.itemView.context as AnimalList
            notifyDataSetChanged()
            activity.selectedAnimal = animal
            activity.etName.setText(animal.name)
            activity.etType.setText(animal.type)
            activity.etWeight.setText(animal.weight.toString())
        }
    }

    override fun getItemCount() = animals.size
}
