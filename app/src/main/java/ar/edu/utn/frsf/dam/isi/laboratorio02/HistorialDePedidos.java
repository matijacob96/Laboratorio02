package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;

public class HistorialDePedidos extends AppCompatActivity {

    PedidoAdapter pedidoaux;
    private PedidoRepository repoPedidos;
    ListView pedidoshechos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_de_pedidos);

        pedidoshechos = findViewById(R.id.lstHistorialPedidos);

        repoPedidos = new PedidoRepository();
        pedidoaux = new PedidoAdapter(this,repoPedidos.getLista());
        pedidoshechos.setAdapter(pedidoaux);
    }
}
