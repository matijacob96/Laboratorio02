package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Notification;
import android.app.PendingIntent;
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
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProjectRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        //repoaux = new PedidoRepository();

        //p = repoaux.buscarPorId(intent.getExtras().getInt("idPedido"));



        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                finishOnReceive(context, intent);
            }
        });
    }

    private void finishOnReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //PedidoRepository repoaux;
        Pedido p;
        Double varaux = 0.0;
        Date fechita;

        ProjectRepository.getInstance(context);
        p = ProjectRepository.loadByIdPedido(intent.getExtras().getInt("idPedido"));
        //Toast.makeText(context,"Pedido para "+p.getMailContacto()+" ha cambiado al estado "+p.getEstado(),Toast.LENGTH_LONG).show();

        for (PedidoDetalle d : p.getDetalle()) {
            varaux += d.getCantidad() * d.getProducto().getPrecio();
        }
        fechita = new Date(p.getFecha().getTime());
        DateFormat formater = new SimpleDateFormat("HH:mm");
        String fechitaformato = formater.format(fechita);

        Intent notificationIntent = new Intent(context,HistorialDePedidos.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent intent2 = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        if(action == "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_ACEPTADO") {

            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "CANAL01")
                    .setSmallIcon(R.drawable.ic_restaurant_black)
                    .setContentTitle("Tu pedido fue aceptado")
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("El costo será de: $" + varaux)
                            .addLine("El horario de retiro/envio es: " + fechitaformato));

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            Notification notification1 = notification.build();
            notification1.flags |= Notification.FLAG_AUTO_CANCEL;
            notification1.contentIntent = intent2;

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(p.getId(), notification1);
        } else if(action == "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_EN_PREPARACION") {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "CANAL01")
                    .setSmallIcon(R.drawable.ic_restaurant_black)
                    .setContentTitle("Tu pedido está en preparación")
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("El costo será de: $" + varaux)
                            .addLine("El horario de retiro/envio es: " + fechitaformato));

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            Notification notification1 = notification.build();
            notification1.flags |= Notification.FLAG_AUTO_CANCEL;
            notification1.contentIntent = intent2;

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(p.getId(), notification1);
        } else if(action == "ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_LISTO") {

            NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "CANAL01")
                    .setSmallIcon(R.drawable.ic_restaurant_black)
                    .setContentTitle("Tu pedido está listo")
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("El costo será de: $" + varaux)
                            .addLine("El horario de retiro/envio es: " + fechitaformato));

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            Notification notification1 = notification.build();
            notification1.flags |= Notification.FLAG_AUTO_CANCEL;
            notification1.contentIntent = intent2;

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(p.getId(), notification1);
        }
    }
}
