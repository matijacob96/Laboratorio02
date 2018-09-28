package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class Dardealta extends AppCompatActivity {

    Pedido unPedido;
    PedidoRepository repositorioPedido;
    ProductoRepository repositorioProducto;
    RadioGroup optPedidoModoEntrega;
    EditText edtPedidoDireccion;
    ArrayAdapter<PedidoDetalle> adaptadorlist;
    ListView lstPedidoItems;
    Button btnAddProducto;
    PedidoDetalle detalleaux;
    TextView lblTotalPedido;
    Double varaux = 0.0;
    //Integer newId;
    Button btnHacerpedido;
    EditText edtCorreo;
    EditText edtPedidoHoraEntrega;
    Button btnVolver;
    Button btnQuitarProducto;
    PedidoDetalle itemSeleccionado;

    private final int REQUEST_NUEVOITEM = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dardealta);

        repositorioPedido = new PedidoRepository();
        repositorioProducto = new ProductoRepository();
        optPedidoModoEntrega = findViewById(R.id.optPedidoModoDeEntrega);
        edtPedidoDireccion = findViewById(R.id.edtPedidoDireccion);
        lstPedidoItems = findViewById(R.id.lstPedidoItems);
        btnAddProducto = findViewById(R.id.btnPedidoAddProducto);
        lblTotalPedido = findViewById(R.id.lblTotalPedido);
        btnHacerpedido = findViewById(R.id.btnHacerPedido);
        edtCorreo = findViewById(R.id.edtPedidoCorreo);
        edtPedidoHoraEntrega = findViewById(R.id.edtPedidoHoraEntrega);
        btnVolver = findViewById(R.id.btnDAVolver);
        btnQuitarProducto = findViewById(R.id.btnQuitarProducto);



        Intent i1= getIntent();
        Integer idPedido = 0;
        if(i1.getExtras()!=null){
            idPedido = i1.getExtras().getInt("idPedidoSeleccionado");
            Log.d("Database", "idPedidoSeleccionado llegada "+idPedido.toString());

            if (i1.getExtras().getInt("HAY_PEDIDO") == 1) {
                edtCorreo.setEnabled(false);
                edtPedidoDireccion.setEnabled(false);
                edtPedidoHoraEntrega.setEnabled(false);
                btnQuitarProducto.setEnabled(false);
                btnHacerpedido.setEnabled(false);
                btnAddProducto.setEnabled(false);
                lstPedidoItems.setEnabled(false);
                lstPedidoItems.setClickable(false);
                optPedidoModoEntrega.setEnabled(false);
                findViewById(R.id.optPedidoRetira).setClickable(false);
                findViewById(R.id.optPedidoRetira).setEnabled(false);
                findViewById(R.id.optPedidoEnviar).setClickable(false);
                findViewById(R.id.optPedidoEnviar).setEnabled(false);

                for (int i=0; i<lstPedidoItems.getChildCount(); i++) {
                    lstPedidoItems.getChildAt(i).setEnabled(false);
                }


            }
        }
        if(idPedido>0){
            unPedido = repositorioPedido.buscarPorId(idPedido);
            edtCorreo.setText(unPedido.getMailContacto());
            edtPedidoDireccion.setText(unPedido.getDireccionEnvio());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            edtPedidoHoraEntrega.setText(sdf.format(unPedido.getFecha()));
            if (unPedido.getRetirar()){
                optPedidoModoEntrega.check(R.id.optPedidoRetira);
            } else {
                optPedidoModoEntrega.check(R.id.optPedidoEnviar);
            }


            if (i1.getExtras().getInt("HAY_PEDIDO") == 1) {
                String str;

                for(PedidoDetalle d : unPedido.getDetalle()){
                    varaux += d.getCantidad()*d.getProducto().getPrecio();
                }
                str = getResources().getString(R.string.pedido_lbl_totalpedido, varaux.toString());
                lblTotalPedido.setText(str);
            }

        }else {
            unPedido = new Pedido();
        }


        edtPedidoDireccion.setEnabled(false);



        optPedidoModoEntrega.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.optPedidoEnviar){
                    edtPedidoDireccion.setEnabled(true);
                } else {
                    edtPedidoDireccion.setEnabled(false);
                }
            }
        });

        adaptadorlist = new ArrayAdapter<PedidoDetalle>(getApplicationContext(),android.R.layout.simple_list_item_single_choice, unPedido.getDetalle());
        lstPedidoItems.setAdapter(adaptadorlist);

        btnAddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("NUEVO_PEDIDO", 1);
                Intent i = new Intent(getApplicationContext(),verlistaproducto.class);
                i.putExtras(bundle);
                startActivityForResult(i, REQUEST_NUEVOITEM);
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        lstPedidoItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSeleccionado = (PedidoDetalle) parent.getItemAtPosition(position);
                lstPedidoItems.setItemChecked(position, true);
            }
        });

        btnQuitarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unPedido.quitarDetalle(itemSeleccionado);
                adaptadorlist.notifyDataSetChanged();
            }
        });


        btnHacerpedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (algunnull()){
                    Toast.makeText(Dardealta.this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
                } else{
                    unPedido.setEstado(Pedido.Estado.REALIZADO);
                    if (optPedidoModoEntrega.getCheckedRadioButtonId() == R.id.optPedidoEnviar){
                        unPedido.setDireccionEnvio(edtPedidoDireccion.getText().toString());
                    }
                    unPedido.setMailContacto(edtCorreo.getText().toString());
                    if (optPedidoModoEntrega.getCheckedRadioButtonId() == R.id.optPedidoRetira){
                        unPedido.setRetirar(true);
                    } else{
                        unPedido.setRetirar(false);
                    }

                    String[] horaIngresada = edtPedidoHoraEntrega.getText().toString().split(":");
                    GregorianCalendar hora = new GregorianCalendar();
                    int valorHora = Integer.valueOf(horaIngresada[0]);
                    int valorMinuto = Integer.valueOf(horaIngresada[1]);
                    if(valorHora<0 || valorHora>23){
                        Toast.makeText(getApplicationContext(),
                                "La hora ingresada "+valorHora+" es incorrecta",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(valorMinuto <0 || valorMinuto >59){
                        Toast.makeText(getApplicationContext(),
                                "Los minutos "+valorMinuto+" son incorrectos",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    hora.set(Calendar.HOUR_OF_DAY,valorHora);
                    hora.set(Calendar.MINUTE,valorMinuto);
                    hora.set(Calendar.SECOND,Integer.valueOf(0));
                    unPedido.setFecha(hora.getTime());
                    repositorioPedido.guardarPedido(unPedido);

                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.currentThread().sleep(10000);
                            } catch (InterruptedException e){
                                e.printStackTrace();
                            }
                            List<Pedido> lista = repositorioPedido.getLista();
                            for(Pedido p:lista){
                                if(p.getEstado().equals(Pedido.Estado.REALIZADO))
                                        p.setEstado(Pedido.Estado.ACEPTADO);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(),EstadoPedidoReceiver.class);
                                    intent.putExtra("idPedido",unPedido.getId());
                                    intent.setAction("ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_ACEPTADO");
                                    getApplicationContext().sendBroadcast(intent);
                                    //Toast.makeText(Dardealta.this, "Información de pedidos ACTUALIZADA", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    };

                    Thread unHilo = new Thread(r);
                    unHilo.start();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });



    }


    public boolean algunnull(){
        return (edtCorreo.getText().toString().isEmpty() || (edtPedidoDireccion.getText().toString().isEmpty() && optPedidoModoEntrega.getCheckedRadioButtonId()== R.id.optPedidoEnviar )|| edtPedidoHoraEntrega.getText().toString().isEmpty());
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);

        if (requestCode == REQUEST_NUEVOITEM){
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();

                detalleaux = new PedidoDetalle();
                detalleaux.setProducto(repositorioProducto.buscarPorId(bundle.getInt("idProducto")));
                detalleaux.setCantidad(bundle.getInt("cantidad"));

                detalleaux.setPedido(unPedido);


                String str;
                varaux += detalleaux.getCantidad()*detalleaux.getProducto().getPrecio();
                str = getResources().getString(R.string.pedido_lbl_totalpedido, varaux.toString());
                lblTotalPedido.setText(str);
                adaptadorlist.notifyDataSetChanged();
            }
        }
    }


}
