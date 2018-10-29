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
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProjectRepository;
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

        Runnable r = new Runnable() {
            @Override
            public void run() {
                //CategoriaRest catRest = new CategoriaRest();
                // final Categoria[] cats = catRest.listarTodas().toArray(new Categoria[0]);

                ProjectRepository.getInstance(getApplicationContext()); //Crea la DB

                final List<Categoria> cats = ProjectRepository.getAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adaptadorspin = new ArrayAdapter<Categoria>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cats);
                        cmbProductosCategoria.setAdapter(adaptadorspin);
                        cmbProductosCategoria.setSelection(0);

                        adaptadorlist = new ArrayAdapter<Producto>(getApplicationContext(),android.R.layout.simple_list_item_single_choice, repoAux.buscarPorCategoria((Categoria) cmbProductosCategoria.getItemAtPosition(0)));
                        lstProductos.setAdapter(adaptadorlist);

                        cmbProductosCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                adaptadorlist.clear();
                                adaptadorlist.addAll(repoAux.buscarPorCategoria((Categoria)parent.getItemAtPosition(position))
                                );
                                adaptadorlist.notifyDataSetChanged();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });


                    }
                });
            }
        };
        Thread hiloCargarComo = new Thread(r);
        hiloCargarComo.start();


        lstProductos.setSelection(0);

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
