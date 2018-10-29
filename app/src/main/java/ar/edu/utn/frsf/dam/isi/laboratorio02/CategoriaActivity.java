package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProjectRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class CategoriaActivity extends AppCompatActivity {

    private EditText textoCat;
    private Button btnCrear;
    private Button btnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        textoCat = (EditText) findViewById(R.id.txtNombreCategoria);
        btnCrear = (Button) findViewById(R.id.btnCrearCategoria);
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread() {
                    @Override
                    public void run(){
                        /*CategoriaRest crest = new CategoriaRest();*/
                        ProjectRepository.getInstance(getApplicationContext()); //Crea la DB

                        final Categoria c = new Categoria(textoCat.getText().toString());
                        try {
                            /*crest.crearCategoria(c);*/
                            ProjectRepository.insertAll(c);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),c.getNombre()+ " fue creada correctamente",Toast.LENGTH_SHORT).show();
                                }
                            });

                            textoCat.setText("");
                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Error: No se pudo ejecutar la operacion", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }
                };
                thread.start();
            }
        });
        btnMenu= (Button) findViewById(R.id.btnCategoriaVolver);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoriaActivity.this,
                        MainActivity.class);
                startActivity(i);
            }
        });
    }
}
