package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProjectRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class HistorialDePedidos extends AppCompatActivity {

    PedidoAdapter pedidoaux;
    private PedidoRepository repoPedidos;
    ListView pedidoshechos;
    Button btnMenu;
    Button btnNuevo;

    private final int REQUEST_NUEVOITEM = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_de_pedidos);

        pedidoshechos = findViewById(R.id.lstHistorialPedidos);
        btnMenu = findViewById(R.id.btnHistorialMenu);
        btnNuevo = findViewById(R.id.btnHistorialNuevo);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //repoPedidos = new PedidoRepository();
                //pedidoaux = new PedidoAdapter(this, repoPedidos.getLista());
                ProjectRepository.getInstance(getApplicationContext());
                final List<Pedido> pedidos = ProjectRepository.getAllPedido();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pedidoaux = new PedidoAdapter(getApplicationContext(), pedidos);
                        pedidoshechos.setAdapter(pedidoaux);
                    }
                });

            }
        });
        t.start();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Dardealta.class);
                startActivityForResult(i, REQUEST_NUEVOITEM);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);

        if (requestCode == REQUEST_NUEVOITEM){
            if(resultCode == RESULT_OK){
                pedidoaux.notifyDataSetChanged();

            }
        }
    }
}
