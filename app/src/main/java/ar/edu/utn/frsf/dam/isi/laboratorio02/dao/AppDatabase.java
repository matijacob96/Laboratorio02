package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;


@Database(entities = {Categoria.class, Producto.class}, version = 1, exportSchema = false)

    public abstract class AppDatabase extends RoomDatabase {
        public abstract CategoriaDao categoriaDao();
        public abstract ProductoDao productoDao();
    }

