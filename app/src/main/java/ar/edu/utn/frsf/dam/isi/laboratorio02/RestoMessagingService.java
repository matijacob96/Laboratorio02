package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

public class RestoMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

            Integer idPedido = Integer.parseInt(remoteMessage.getData().get("ID_PEDIDO"));

            PedidoRepository pedidoRepository = new PedidoRepository();
            Pedido pedido = pedidoRepository.buscarPorId(idPedido);
            System.out.println("A ver que onda "+pedido.getId() +"El estado es "+pedido.getEstado());
            pedido.setEstado(Pedido.Estado.LISTO);

            showNotificacion(pedido);
        }

        private void showNotificacion(Pedido p){

            Double varaux = 0.0;
            Date fechita;
            PedidoRepository repoaux;

            for (PedidoDetalle d : p.getDetalle()) {
                varaux += d.getCantidad() * d.getProducto().getPrecio();
            }
            fechita = new Date(p.getFecha().getTime());
            DateFormat formater = new SimpleDateFormat("HH:mm");
            String fechitaformato = formater.format(fechita);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "CANAL01")
                    .setSmallIcon(R.drawable.ic_restaurant_black)
                    .setContentTitle("Tu pedido está listo")
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("El costo será de: $" + varaux)
                            .addLine("El horario de retiro/envio es: " + fechitaformato));

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

            notificationManager.notify(p.getId(), notification.build());
        }

}
