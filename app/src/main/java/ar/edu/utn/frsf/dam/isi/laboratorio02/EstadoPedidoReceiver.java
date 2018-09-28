package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PedidoRepository repoaux;
        Pedido p;

        repoaux = new PedidoRepository();

        p = repoaux.buscarPorId(intent.getExtras().getInt("idPedido"));
        Toast.makeText(context,"Pedido para "+p.getMailContacto()+" ha cambiado al estado "+p.getEstado(),Toast.LENGTH_LONG).show();
    }
}
