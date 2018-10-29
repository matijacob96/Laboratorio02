package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

@Dao
public interface ProductoDao {
    @Query("SELECT * FROM producto")
    List<Producto> getAll();

    @Query("SELECT * FROM producto WHERE id IN (:productoIds)")
    List<Producto> loadAllByIds(int[] productoIds);

    @Insert
    void insertAll(Producto... productos);

    @Delete
    void delete(Producto producto);

    @Update
    void update(Producto producto);

    @Query("SELECT * FROM producto WHERE id IN (:productoId)")
    Producto loadById(int productoId);

    @Insert
    void insert(Producto producto);
}
