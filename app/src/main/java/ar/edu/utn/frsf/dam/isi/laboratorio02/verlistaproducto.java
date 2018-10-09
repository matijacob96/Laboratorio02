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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class verlistaproducto extends AppCompatActivity {

    Spinner cmbProductosCategoria;
    ListView lstProductos;
    ArrayAdapter<Categoria> adaptadorspin;
    ArrayAdapter<Producto> adaptadorlist;
    ProductoRepository repoAux;
    EditText edtProdCantidad;
    Button btnProdAddPedido;
    Producto itemSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verlistaproducto);

        Bundle bundle = getIntent().getExtras();

        edtProdCantidad = (EditText) findViewById(R.id.edtProdCantidad);
        btnProdAddPedido = findViewById(R.id.btnProdAddPedido);
        cmbProductosCategoria = (Spinner) findViewById(R.id.cmbProductosCategoria);
        lstProductos = (ListView) findViewById(R.id.lstProductos);


        if (bundle.getInt("NUEVO_PEDIDO") == 1){
            edtProdCantidad.setEnabled(true);
            btnProdAddPedido.setEnabled(true);
        } else {
            edtProdCantidad.setEnabled(false);
            btnProdAddPedido.setEnabled(false);
        }
        repoAux = new ProductoRepository();

        adaptadorspin = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, repoAux.getCategorias());
        cmbProductosCategoria.setAdapter(adaptadorspin);


        Categoria cat = repoAux.getCategorias().get(0);
        adaptadorlist = new ArrayAdapter<Producto>(getApplicationContext(),android.R.layout.simple_list_item_single_choice, repoAux.buscarPorCategoria(cat));
        lstProductos.setAdapter(adaptadorlist);
        lstProductos.setSelection(0);



        cmbProductosCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Categoria cat = repoAux.getCategorias().get(position);
                adaptadorlist = new ArrayAdapter<Producto>(getApplicationContext(),android.R.layout.simple_list_item_single_choice, repoAux.buscarPorCategoria(cat));
                lstProductos.setAdapter(adaptadorlist);
                lstProductos.setItemChecked(0, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lstProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSeleccionado = (Producto) parent.getItemAtPosition(position);
                lstProductos.setItemChecked(position, true);
            }
        });

        btnProdAddPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle returnValores = new Bundle();
                returnValores.putInt("cantidad",Integer.parseInt(edtProdCantidad.getText().toString()));
                Log.d("DATABASE", itemSeleccionado.getId().toString());
                returnValores.putInt("idProducto",itemSeleccionado.getId());
                Intent i = new Intent(getApplicationContext(), Dardealta.class);
                i.putExtras(returnValores);
                Log.d("DATABASE", "VerLista "+i.getExtras().toString());
                setResult(RESULT_OK, i);
                finish();

            }
        });
    }
}
