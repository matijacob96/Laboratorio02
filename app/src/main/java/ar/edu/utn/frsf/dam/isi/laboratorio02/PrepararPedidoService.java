package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PrepararPedidoService extends IntentService {
    public PrepararPedidoService() {
        super("PrepararPedidoService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent inten) {
        try {
            Thread.currentThread().sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PedidoRepository repoaux = new PedidoRepository();
        List<Pedido> pedidos = repoaux.getLista();

        for(Pedido p : pedidos){
            if(p.getEstado() == Pedido.Estado.ACEPTADO) {
                p.setEstado(Pedido.Estado.EN_PREPARACION);
                Intent intent = new Intent(getApplicationContext(),EstadoPedidoReceiver.class);
                intent.putExtra("idPedido",p.getId());
                intent.setAction("ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_EN_PREPARACION");
                getApplicationContext().sendBroadcast(intent);
            }
        }


                //Toast.makeText(Dardealta.this, "Información de pedidos ACTUALIZADA", Toast.LENGTH_LONG).show();
    }
}
