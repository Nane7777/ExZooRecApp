package be.nane.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.nane.R
import be.nane.dao.AnimalDAO
import be.nane.viewHolderAdapter.AnimalAdapter
import be.nane.models.Animal

class AnimalList : AppCompatActivity() {
    private lateinit var animalDAO: AnimalDAO
    lateinit var etName: EditText
    lateinit var etType: EditText
    lateinit var etWeight: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var btnClear: Button
    private lateinit var tvNbAnimals: TextView
    private lateinit var rvAnimals: RecyclerView
    var selectedAnimal: Animal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_list)
        initializeView()
        animalDAO = AnimalDAO(this).openReadable()
        setUpRecyclerView()
        setUpButtons()
        updateAnimalsCount()
    }

    override fun onDestroy() {
        super.onDestroy()
        animalDAO.close()
    }

    private fun setUpRecyclerView() {
        rvAnimals.layoutManager = LinearLayoutManager(this)
        rvAnimals.adapter = AnimalAdapter(animalDAO.getAll())

    }

    private fun clear() {
        etName.text.clear()
        etType.text.clear()
        etWeight.text.clear()
    }

    private fun refresh() {
        rvAnimals.adapter = AnimalAdapter(animalDAO.getAll())
    }

    private fun updateAnimalsCount() {
        val nbAnimals = animalDAO.getAll().size
        if (nbAnimals <= 1) {
            tvNbAnimals.text = "${nbAnimals} animal dans le zoo"
        } else {
        tvNbAnimals.text = "${nbAnimals} animaux dans le zoo"
        }
    }

    private fun initializeView() {
        etName = findViewById(R.id.et_name)
        etType = findViewById(R.id.et_type)
        etWeight = findViewById(R.id.et_weight)
        btnAdd = findViewById(R.id.btn_add)
        btnUpdate = findViewById(R.id.btn_update)
        btnDelete = findViewById(R.id.btn_delete)
        btnClear = findViewById(R.id.btn_clear)
        tvNbAnimals = findViewById(R.id.tv_nb_animals)
        rvAnimals = findViewById(R.id.rv_animals)
    }

    private fun setUpButtons() {

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val type = etType.text.toString()
            val weightText = etWeight.text.toString()
            if (name.isNotEmpty() && type.isNotEmpty() && weightText.isNotEmpty()) {
                val weight = weightText.toInt()
                if (weight != null) {
                    val animal = Animal(
                        name = name,
                        type = type,
                        weight = weight
                    )
                    animalDAO.insertAnimal(animal)
                    Toast.makeText(
                        this@AnimalList,
                        "${animal.name} a été ajouté.",
                        Toast.LENGTH_SHORT
                    ).show()
                    refresh()
                    clear()
                    updateAnimalsCount()
                    selectedAnimal = null
                }
            } else {
                Toast.makeText(
                    this@AnimalList,
                    "Veuillez remplir tous les champs",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnUpdate.setOnClickListener {
            if (selectedAnimal != null) {
                val name = etName.text.toString()
                val type = etType.text.toString()
                val weightText = etWeight.text.toString()
                if (name.isNotEmpty() && type.isNotEmpty() && weightText.isNotEmpty()) {
                    val weight = weightText.toIntOrNull()
                    if (weight != null) {
                        val animal = selectedAnimal!!.copy(
                            name = name,
                            type = type,
                            weight = weight
                        )
                        animalDAO.updateAnimal(animal)
                        Toast.makeText(
                            this@AnimalList,
                            "${animal.name} a été mis à jour.",
                            Toast.LENGTH_SHORT
                        ).show()
                        refresh()
                        clear()
                        updateAnimalsCount()
                        selectedAnimal = null
                    }
                } else {
                    Toast.makeText(
                        this@AnimalList,
                        "Veuillez remplir tous les champs",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        btnDelete.setOnClickListener {
            if (selectedAnimal != null) {
                animalDAO.deleteAnimal(selectedAnimal!!.id.toLong())
                Toast.makeText(this@AnimalList, "Animal supprimé", Toast.LENGTH_SHORT).show()
                refresh()
                clear()
                updateAnimalsCount()
                selectedAnimal = null
            } else {
                Toast.makeText(
                    this@AnimalList,
                    "Veuillez sélectionner un animal",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnClear.setOnClickListener {
            clear()
            refresh()
            selectedAnimal = null
        }
    }
}