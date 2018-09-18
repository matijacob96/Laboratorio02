package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    Integer newId;
    Button btnHacerpedido;
    EditText edtCorreo;
    EditText edtPedidoHoraEntrega;


    private final int REQUEST_NUEVOITEM = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dardealta);

        unPedido = new Pedido();
        repositorioPedido = new PedidoRepository();
        repositorioProducto = new ProductoRepository();
        optPedidoModoEntrega = findViewById(R.id.optPedidoModoDeEntrega);
        edtPedidoDireccion = findViewById(R.id.edtPedidoDireccion);
        lstPedidoItems = findViewById(R.id.lstPedidoItems);
        btnAddProducto = findViewById(R.id.btnPedidoAddProducto);
        lblTotalPedido = findViewById(R.id.lblTotalPedido);
        btnHacerpedido = findViewById(R.id.btnHacerPedido);
        edtCorreo = findViewById(R.id.edtPedidoCorreo);
        edtPedidoDireccion = findViewById(R.id.edtPedidoDireccion);
        edtPedidoHoraEntrega = findViewById(R.id.edtPedidoHoraEntrega);

        edtPedidoDireccion.setEnabled(false);
        newId = repositorioPedido.generateId();
        unPedido.setId(newId);


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
