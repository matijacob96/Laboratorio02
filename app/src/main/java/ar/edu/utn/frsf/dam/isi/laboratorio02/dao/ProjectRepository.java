package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class ProjectRepository {
    private static ProjectRepository _REPO = null;
    private static CategoriaDao categoriaDao;

    private ProjectRepository(Context ctx) {
        AppDatabase db = Room.databaseBuilder(ctx,
                AppDatabase.class, "dam-pry-2018").fallbackToDestructiveMigration()
                .build();
        categoriaDao = db.categoriaDao();
    }

    public static ProjectRepository getInstance(Context ctx) {
        if (_REPO == null) _REPO = new ProjectRepository(ctx);
        return _REPO;
    }

    public static List<Categoria> getAll() {
        return categoriaDao.getAll();
    }

    public static List<Categoria> loadAllByIds(int[] categoriaIds) {
        return categoriaDao.loadAllByIds(categoriaIds);
    }

    public static void insertAll(Categoria... categorias) {
        categoriaDao.insertAll(categorias);
    }

    public static void delete(Categoria categoria) {
        categoriaDao.delete(categoria);
    }

    public static void update(Categoria categoria) {
        categoriaDao.update(categoria);
    }
}
