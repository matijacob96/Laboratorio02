package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PedidoRepository repoaux;
        Pedido p;
        Double varaux = 0.0;
        Date fechita;

        repoaux = new PedidoRepository();

        p = repoaux.buscarPorId(intent.getExtras().getInt("idPedido"));
        //Toast.makeText(context,"Pedido para "+p.getMailContacto()+" ha cambiado al estado "+p.getEstado(),Toast.LENGTH_LONG).show();

        for(PedidoDetalle d : p.getDetalle()){
            varaux += d.getCantidad()*d.getProducto().getPrecio();
        }
        fechita = new Date(p.getFecha().getTime());
        DateFormat formater = new SimpleDateFormat("HH:mm");
        String fechitaformato = formater.format(fechita);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "CANAL01")
                .setSmallIcon(R.drawable.ic_restaurant_black)
                .setContentTitle("Tu pedido fue aceptado")
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("El costo será de: $"+varaux)
                        .addLine("El horario de retiro/envio es: "+fechitaformato));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(p.getId(), notification.build());
    }
}
