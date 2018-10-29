package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRetrofit;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProjectRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GestionProductoActivity extends AppCompatActivity {
    private Button btnMenu;
    private Button btnGuardar;
    private Spinner comboCategorias;
    private EditText nombreProducto;
    private EditText descProducto;
    private EditText precioProducto;
    private ArrayAdapter<Categoria> adaptadorspin;
    private ToggleButton opcionNuevoBusqueda;
    private EditText idProductoBuscar;
    private Button btnBuscar;
    private Button btnBorrar;
    private Boolean flagActualizacion;
    private ArrayAdapter<Categoria> comboAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_producto);
        flagActualizacion = false;
        opcionNuevoBusqueda = (ToggleButton) findViewById(R.id.abmProductoAltaNuevo);
        idProductoBuscar = (EditText) findViewById(R.id.abmProductoIdBuscar);
        nombreProducto = (EditText) findViewById(R.id.abmProductoNombre);
        descProducto = (EditText) findViewById(R.id.abmProductoDescripcion);
        precioProducto = (EditText) findViewById(R.id.abmProductoPrecio);
        comboCategorias = (Spinner) findViewById(R.id.abmProductoCategoria);
        btnMenu = (Button) findViewById(R.id.btnAbmProductoVolver);
        btnGuardar = (Button) findViewById(R.id.btnAbmProductoCrear);
        btnBuscar = (Button) findViewById(R.id.btnAbmProductoBuscar);
        btnBorrar = (Button) findViewById(R.id.btnAbmProductoBorrar);
        opcionNuevoBusqueda.setChecked(false);
        btnBuscar.setEnabled(false);
        btnBorrar.setEnabled(false);
        idProductoBuscar.setEnabled(false);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                /*CategoriaRest catRest = new CategoriaRest();
                final Categoria[] cats = catRest.listarTodas().toArray(new Categoria[0]);*/

                ProjectRepository.getInstance(getApplicationContext()); //Crea la DB

                final List<Categoria> cats = ProjectRepository.getAll();

                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {

                                      adaptadorspin = new ArrayAdapter<Categoria>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cats);
                                      comboCategorias.setAdapter(adaptadorspin);
                                      comboCategorias.setSelection(0);
                                  }
                              });
            }
        };
        Thread hiloCargarComo = new Thread(r);
        hiloCargarComo.start();

        opcionNuevoBusqueda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flagActualizacion = isChecked;
                btnBuscar.setEnabled(isChecked);
                btnBorrar.setEnabled(isChecked);
                idProductoBuscar.setEnabled(isChecked);
            }
        });
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread r = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            ProjectRepository.getInstance(getApplicationContext());
                            Producto aBorrar = ProjectRepository.loadById(Integer.parseInt(idProductoBuscar.getText().toString()));

                            ProjectRepository.deleteProducto(aBorrar);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    nombreProducto.setText(null);
                                    precioProducto.setText(null);
                                    descProducto.setText(null);
                                    comboCategorias.setSelection(-1);
                                    idProductoBuscar.setText(null);
                                }
                            });

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GestionProductoActivity.this, "El producto ha sido borrado con éxito", Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GestionProductoActivity.this,"Ha ocurrido un error", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }
                });
                r.start();
                /*ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                Call<Producto> altaCall = clienteRest.borrar(Integer.parseInt(idProductoBuscar.getText().toString()));
                altaCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call, Response<Producto> response) {
                        nombreProducto.setText(null);
                        precioProducto.setText(null);
                        descProducto.setText(null);
                        comboCategorias.setSelection(-1);
                        idProductoBuscar.setText(null);
                        Toast.makeText(GestionProductoActivity.this,"El producto ha sido borrado con éxito", Toast.LENGTH_LONG).show();
                        return ;
                    }

                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        Toast.makeText(GestionProductoActivity.this,"Ha ocurrido un error", Toast.LENGTH_LONG).show();
                        return ;
                    }
                });*/
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread r = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            ProjectRepository.getInstance(getApplicationContext());
                            final Producto buscado = ProjectRepository.loadById(Integer.parseInt(idProductoBuscar.getText().toString()));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    nombreProducto.setText(buscado.getNombre());
                                    descProducto.setText(buscado.getDescripcion());
                                    precioProducto.setText(String.valueOf(buscado.getPrecio()));
                                    comboCategorias.setSelection(buscado.getCategoria().getId()-1);
                                }
                            });

                        } catch (Exception e) {
                            //Log.d("Exception", e.getLocalizedMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GestionProductoActivity.this,"Ha ocurrido un error", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
                r.start();

                /*ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                Call<Producto> altaCall = clienteRest.buscarProductoPorId(Integer.parseInt(idProductoBuscar.getText().toString()));
                altaCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call, Response<Producto> response) {
                        nombreProducto.setText(response.body().getNombre().toString());
                        descProducto.setText(response.body().getDescripcion().toString());
                        precioProducto.setText(response.body().getPrecio().toString());
                        comboCategorias.setSelection(response.body().getCategoria().getId()-1);
                    }

                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        Toast.makeText(GestionProductoActivity.this,"Ha ocurrido un error", Toast.LENGTH_LONG).show();
                        return ;
                    }
                });*/
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread r = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            ProjectRepository.getInstance(getApplicationContext());

                            if (!flagActualizacion) {
                                Producto p = new Producto(nombreProducto.getText().toString(), descProducto.getText().toString(), Double.parseDouble(precioProducto.getText().toString()), (Categoria) comboCategorias.getSelectedItem());

                                ProjectRepository.insertProducto(p);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GestionProductoActivity.this,"El producto ha sido creado con éxito", Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else {


                                Producto aActualizar = ProjectRepository.loadById(Integer.parseInt(idProductoBuscar.getText().toString()));

                                aActualizar.setNombre(nombreProducto.getText().toString());
                                aActualizar.setDescripcion(descProducto.getText().toString());
                                aActualizar.setPrecio(Double.parseDouble(precioProducto.getText().toString()));
                                aActualizar.setCategoria((Categoria) comboCategorias.getSelectedItem());

                                ProjectRepository.updateProducto(aActualizar);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GestionProductoActivity.this,"El producto ha sido actualizado", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }


                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GestionProductoActivity.this,"Ha ocurrido un error", Toast.LENGTH_LONG).show();
                                }
                            });


                        }
                    }
                });
                r.start();



                /*Producto p = new Producto(nombreProducto.getText().toString(), descProducto.getText().toString(), Double.parseDouble(precioProducto.getText().toString()), (Categoria) comboCategorias.getSelectedItem());

                ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);

                Call<Producto> altaCall;

                if (!flagActualizacion) {
                    altaCall = clienteRest.crearProducto(p);
                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> resp) {
                            Toast.makeText(GestionProductoActivity.this,"El producto ha sido creado con éxito", Toast.LENGTH_LONG).show();
                            return ;
                        }

                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Toast.makeText(GestionProductoActivity.this,"Ha ocurrido un error", Toast.LENGTH_LONG).show();
                            return ;
                        }
                    });
                }else{
                    altaCall = clienteRest.actualizarProducto(Integer.parseInt(idProductoBuscar.getText().toString()), p);
                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> resp) {
                            Toast.makeText(GestionProductoActivity.this,"El producto ha sido actualizado", Toast.LENGTH_LONG).show();
                            return ;
                        }

                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Toast.makeText(GestionProductoActivity.this,"Ha ocurrido un error", Toast.LENGTH_LONG).show();
                            return ;
                        }
                    });
                }*/
            }
        });
    }
}