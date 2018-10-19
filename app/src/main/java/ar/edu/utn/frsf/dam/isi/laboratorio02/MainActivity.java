package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class MainActivity extends AppCompatActivity {

    private Button btnNuevoPedido;
    private Button btnHistorial;
    private Button btnListaProductos;
    private Button btnPrepararPedido;
    private Button btnPref;

    private final int REQUEST_NUEVOPEDIDO = 0;

    private void createNotificationChannel() {
        // Crear el canal de notificaciones pero solo para API 26 io superior
        // dado que NotificationChannel es una clase nueva que no está incluida
        // en las librerías de soporte qeu brindan compatibilidad hacía atrás
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Canal de prueba"; //TODO Cambiar esto para que no se hardcodee.
            String description = "Descripcion de prueba";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CANAL01", name, importance);
            channel.setDescription(description);
            // Registrar el canal en el sistema
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        if(getIntent().hasExtra("ID_PEDIDO")){

            Integer idPedido = Integer.valueOf(getIntent().getExtras().getString("ID_PEDIDO"));
            PedidoRepository pedidoRepository = new PedidoRepository();
            Pedido pedido = pedidoRepository.buscarPorId(idPedido);
            if(!pedido.getEstado().equals(Pedido.Estado.LISTO)){
                pedido.setEstado(Pedido.Estado.LISTO);
                Intent intentListo = new Intent(MainActivity.this,EstadoPedidoReceiver.class);
                intentListo.putExtra("idPedido", pedido.getId());
                intentListo.setAction("ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_LISTO");
                getApplicationContext().sendBroadcast(intentListo);
            }
        }

        btnNuevoPedido = (Button) findViewById(R.id.btnMainNuevoPedido);
        btnNuevoPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Dardealta.class);
                startActivity(i);
                /*Intent i = new Intent();
                startActivityForResult(i, REQUEST_NUEVOPEDIDO);*/
            }
        });

        btnHistorial = (Button) findViewById(R.id.btnHistorialPedidos);
        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HistorialDePedidos.class);
                startActivity(i);
            }
        });

        btnListaProductos = (Button) findViewById(R.id.btnListaProductos);
        btnListaProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("NUEVO_PEDIDO", 0);
                Intent i = new Intent(getApplicationContext(), verlistaproducto.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        btnPrepararPedido = (Button) findViewById(R.id.btnPrepararPedidos);
        btnPrepararPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PrepararPedidoService.class);
                startService(intent);
            }
        });

        btnPref = findViewById(R.id.btnPref);
        btnPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfiguracionActivity.class);
                startActivity(intent);
            }
        });
    }
}
