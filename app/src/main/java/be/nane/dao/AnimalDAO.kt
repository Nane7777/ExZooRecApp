package be.nane.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import be.nane.db.DbHelper
import be.nane.models.Animal

class AnimalDAO(context: Context) {

    companion object {
        private const val TABLE_ANIMAL = "animal"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_WEIGHT = "weight"

        const val CREATE_REQUEST =
            """
            CREATE TABLE $TABLE_ANIMAL (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_TYPE TEXT NOT NULL,
                $COLUMN_WEIGHT INTEGER NOT NULL
            )"""

        const val UPGRADE_REQUEST = "DROP TABLE IF EXISTS $TABLE_ANIMAL"
    }

    private var db: SQLiteDatabase? = null
    private val dbHelper: DbHelper = DbHelper(context)

    fun openWritable(): AnimalDAO {
        db = dbHelper.writableDatabase
        return this@AnimalDAO
    }

    fun openReadable(): AnimalDAO {
        db = dbHelper.readableDatabase
        return this@AnimalDAO
    }

    fun close() {
        db?.close()
        dbHelper.close()
    }
    fun insertAnimal(animal: Animal): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, animal.name)
            put(COLUMN_TYPE, animal.type)
            put(COLUMN_WEIGHT, animal.weight)
        }
        return db!!.insert(TABLE_ANIMAL, null, values)
    }

    fun deleteAnimal(id: Long) {
        db!!.delete(TABLE_ANIMAL, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun updateAnimal(animal: Animal): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, animal.name)
            put(COLUMN_TYPE, animal.type)
            put(COLUMN_WEIGHT, animal.weight)
        }
        return db!!.update(TABLE_ANIMAL, values, "$COLUMN_ID = ?", arrayOf(animal.id.toString())).toLong()
    }

    fun getById(id: Long): Animal? {
        val cursor = db?.query(
            TABLE_ANIMAL, null, "$COLUMN_ID = ?",
            arrayOf(id.toString()), null, null, null)

        return if (cursor != null && cursor.moveToFirst()) {
            val user = fromCursor(cursor)
            cursor.close()
            user
        } else {
            cursor?.close()
            null
        }
    }

    private fun fromCursor(cursor: Cursor): Animal {
        return Animal(
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
        )
    }

    fun getAll(): List<Animal> {
        val animalList = mutableListOf<Animal>()
        val cursor = db?.query(
            TABLE_ANIMAL, null, null,
            null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            do { animalList.add(fromCursor(cursor))} while (cursor.moveToNext())
        }

        cursor?.close()
        return animalList
    }
}