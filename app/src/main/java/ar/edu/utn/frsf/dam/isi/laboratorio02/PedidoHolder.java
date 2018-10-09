package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PedidoHolder {

        public TextView tvMailPedido;
        public TextView tvHoraEntrega;
        public TextView tvCantidadItems;
        public TextView tvPrecio;
        public TextView estado;
        public ImageView tipoEntrega;
        public Button btnCancelar;
        public Button btnVerDetalle;

        PedidoHolder(View base){
                this.tvMailPedido = base.findViewById(R.id.tvFilaMail);
                this.tvHoraEntrega = base.findViewById(R.id.tvFilaHoraEntrega);
                this.tvCantidadItems = base.findViewById(R.id.tvFilaCantidadItems);
                this.tvPrecio = base.findViewById(R.id.tvFilaCosto);
                this.estado = base.findViewById(R.id.tvFilaEstado);
                this.tipoEntrega = base.findViewById(R.id.igFilaIcono);
                this.btnCancelar = base.findViewById(R.id.btnFilaCancelar);
                this.btnVerDetalle = base.findViewById(R.id.btnFilaVerDetalle);
        }



    }
