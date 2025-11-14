package com.example.tiendaonline;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiendaonline.database.AppDatabase;

public class ProductoFormActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCTO_ID = "extra_producto_id";

    private EditText etNombreProducto;
    private EditText etDescripcionProducto;
    private EditText etPrecioProducto;
    private Button btnGuardarProducto;
    private Button btnEliminarProducto;
    private Button btnTomarFotoProducto;
    private ImageView ivPreviewProducto;

    private Producto productoActual;
    private Uri fotoUriActual;

    private final ActivityResultLauncher<Intent> tomarFotoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && fotoUriActual != null) {
                    ivPreviewProducto.setImageURI(fotoUriActual);
                } else {
                    fotoUriActual = null;
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_form);

        etNombreProducto = findViewById(R.id.etNombreProducto);
        etDescripcionProducto = findViewById(R.id.etDescripcionProducto);
        etPrecioProducto = findViewById(R.id.etPrecioProducto);
        btnGuardarProducto = findViewById(R.id.btnGuardarProducto);
        btnEliminarProducto = findViewById(R.id.btnEliminarProducto);
        btnTomarFotoProducto = findViewById(R.id.btnTomarFotoProducto);
        ivPreviewProducto = findViewById(R.id.ivPreviewProducto);

        int productoId = getIntent().getIntExtra(EXTRA_PRODUCTO_ID, -1);
        if (productoId != -1) {
            cargarProducto(productoId);
        }

        btnGuardarProducto.setOnClickListener(v -> guardarProducto());
        btnEliminarProducto.setOnClickListener(v -> eliminarProducto());
        btnTomarFotoProducto.setOnClickListener(v -> lanzarCamara());
    }

    private void cargarProducto(int id) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            Producto p = db.productoDao().getById(id);
            productoActual = p;
            if (p != null) {
                runOnUiThread(() -> {
                    etNombreProducto.setText(p.getNombre());
                    etDescripcionProducto.setText(p.getDescripcion());
                    etPrecioProducto.setText(String.valueOf(p.getPrecio()));
                    btnEliminarProducto.setVisibility(android.view.View.VISIBLE);

                    if (p.getImagenUri() != null && !p.getImagenUri().isEmpty()) {
                        fotoUriActual = Uri.parse(p.getImagenUri());
                        ivPreviewProducto.setImageURI(fotoUriActual);
                    }
                });
            }
        }).start();
    }

    private void lanzarCamara() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "producto_" + System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DESCRIPTION, "Foto de producto");

        fotoUriActual = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (fotoUriActual == null) {
            Toast.makeText(this, "No se pudo crear el archivo de la foto", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUriActual);
        tomarFotoLauncher.launch(intent);
    }

    private void guardarProducto() {
        String nombre = etNombreProducto.getText().toString().trim();
        String descripcion = etDescripcionProducto.getText().toString().trim();
        String precioStr = etPrecioProducto.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(descripcion) || TextUtils.isEmpty(precioStr)) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            if (productoActual == null) {
                Producto nuevo = new Producto(nombre, descripcion, precio, R.mipmap.ic_launcher);
                if (fotoUriActual != null) {
                    nuevo.setImagenUri(fotoUriActual.toString());
                }
                db.productoDao().insert(nuevo);
            } else {
                productoActual.setNombre(nombre);
                productoActual.setDescripcion(descripcion);
                productoActual.setPrecio(precio);
                if (fotoUriActual != null) {
                    productoActual.setImagenUri(fotoUriActual.toString());
                }
                db.productoDao().update(productoActual);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    private void eliminarProducto() {
        if (productoActual == null) {
            finish();
            return;
        }

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            db.productoDao().delete(productoActual);
            runOnUiThread(() -> {
                Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
