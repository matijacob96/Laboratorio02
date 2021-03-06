package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PedidoAdapter extends ArrayAdapter<Pedido> {
    private Context ctx;
    private List<Pedido> datos;
    private Pedido pedidoaux;

    public PedidoAdapter(Context context, List<Pedido> objects) {
        super(context, 0, objects);
        this.ctx = context;
        this.datos = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("Database", "position "+position);
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View filas = convertView;
        if(filas== null) {
            filas = inflater.inflate(R.layout.fila_historial, parent, false);
        }
        PedidoHolder holder = (PedidoHolder) filas.getTag();
        if(holder==null){
            holder = new PedidoHolder(filas);
            filas.setTag(holder);
        }

        holder.btnCancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int indice = (int) view.getTag();
                        Pedido pedidoSeleccionado = datos.get(indice);
                        if( pedidoSeleccionado.getEstado().equals(Pedido.Estado.REALIZADO)||
                                pedidoSeleccionado.getEstado().equals(Pedido.Estado.ACEPTADO)||
                                pedidoSeleccionado.getEstado().equals(Pedido.Estado.EN_PREPARACION)){
                            pedidoSeleccionado.setEstado(Pedido.Estado.CANCELADO);
                            PedidoAdapter.this.notifyDataSetChanged();
                            return;
                        }
                    }
                }
        );

        pedidoaux = (Pedido) super.getItem(position);
        Log.d("Database", "pedidoaux "+pedidoaux.getId());

        holder.tvMailPedido.setText("Contacto: "+ pedidoaux.getMailContacto());
        if(pedidoaux.getRetirar()){
            holder.tipoEntrega.setImageResource(R.drawable.ic_restaurant_black);
        }else{
            holder.tipoEntrega.setImageResource(R.drawable.ic_auto_black);
        }
        holder.tvHoraEntrega.setText("Fecha de entrega "+ pedidoaux.getFecha());

        holder.tvCantidadItems.setText("Cantidad de Items: " + pedidoaux.getDetalle().size());
        holder.tvPrecio.setText("Precio total: $"+pedidoaux.total());

        switch (pedidoaux.getEstado()){
            case LISTO:
                holder.estado.setTextColor(Color.DKGRAY);
                break;
            case ENTREGADO:
                holder.estado.setTextColor(Color.BLUE);
                break;
            case CANCELADO:
            case RECHAZADO:
                holder.estado.setTextColor(Color.RED);
                break;
            case ACEPTADO:
                holder.estado.setTextColor(Color.GREEN);
                break;
            case EN_PREPARACION:
                holder.estado.setTextColor(Color.MAGENTA);
                break;
            case REALIZADO:
                holder.estado.setTextColor(Color.BLUE);
                break;
        }
        holder.estado.setText("Estado: "+pedidoaux.getEstado());
        holder.btnCancelar.setTag(position);


        holder.btnVerDetalle.setTag(position);
        holder.btnVerDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("idPedidoSeleccionado", pedidoaux.getId());
                bundle.putInt("HAY_PEDIDO", 1);
                Log.d("Database", "idPedidoSeleccionado salida "+pedidoaux.getId().toString());
                Intent i = new Intent(ctx, Dardealta.class);
                i.putExtras(bundle);
                ctx.startActivity(i);
            }
        });


        return filas;

    }

    }
