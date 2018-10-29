package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ProjectRepository {
    private static ProjectRepository _REPO = null;
    private static CategoriaDao categoriaDao;
    private static ProductoDao productoDao;
    private static PedidoDao pedidoDao;
    private static PedidoDetalleDao pedidoDetalleDao;

    private ProjectRepository(Context ctx) {
        AppDatabase db = Room.databaseBuilder(ctx,
                AppDatabase.class, "dam-pry-2018").fallbackToDestructiveMigration()
                .build();
        categoriaDao = db.categoriaDao();
        productoDao = db.productoDao();
        pedidoDao = db.pedidoDao();
        pedidoDetalleDao = db.pedidoDetalleDao();

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

    public static List<Producto> getAllProducto(){return productoDao.getAll();}

    public static List<Producto> loadAllByIdsProducto(int[] productoIds){return productoDao.loadAllByIds(productoIds);}
    public static void insertAllProducto(Producto... productos) {
        productoDao.insertAll(productos);
    }
    public static void insertProducto(Producto producto) {
        productoDao.insert(producto);
    }

    public static void deleteProducto(Producto producto) {
        productoDao.delete(producto);
    }

    public static void updateProducto(Producto producto) {
        productoDao.update(producto);
    }

    public static Producto loadById(int prodId) {
        return productoDao.loadById(prodId);
    }

    public static void insertPedido(Pedido unPedido) {
        pedidoDao.insertAll(unPedido);
    }

    public static List<Pedido> getAllPedido() {
        return pedidoDao.getAll();
    }

    public static Pedido loadByIdPedido(Integer pedidoId){
        int[] pid = {pedidoId};

        return pedidoDao.loadAllByIds(pid).get(0);
    }


    public static List<Producto>  buscarPorCategoria(Categoria cat) {
        return productoDao.buscarPorCategoria(cat.getId());
    }
}
