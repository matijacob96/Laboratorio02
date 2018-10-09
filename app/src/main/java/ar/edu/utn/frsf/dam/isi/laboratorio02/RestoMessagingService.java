package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class RestoMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

            Integer idPedido = Integer.parseInt(remoteMessage.getData().get("ID_PEDIDO"));

            PedidoRepository pedidoRepository = new PedidoRepository();
            Pedido pedido = pedidoRepository.buscarPorId(idPedido);
            System.out.println("A ver que onda "+pedido.getId() +"El estado es "+pedido.getEstado());
            pedido.setEstado(Pedido.Estado.LISTO);
//            remoteMessage.getData().put("NUEVO_ESTADO", "LISTO");

            Intent intent = new Intent(getApplicationContext(), EstadoPedidoReceiver.class);
            intent.putExtra("idPedido", pedido.getId());
            intent.setAction("ar.edu.utn.frsf.dam.isi.laboratorio02.LISTO");
            getApplicationContext().sendBroadcast(intent);
            //Toast.makeText(Dardealta.this, "Información de pedidos ACTUALIZADA", Toast.LENGTH_LONG).show();
        }

    /*    public RestoMessagingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }*/

}
