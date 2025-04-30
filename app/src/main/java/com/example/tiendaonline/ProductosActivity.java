package com.example.tiendaonline;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProductosActivity extends AppCompatActivity implements ProductoAdapter.OnProductoClickListener {
    private RecyclerView rvProductos;
    private ProductoAdapter adapter;
    private List<Producto> productos;
    private FloatingActionButton fabCarrito;
    private FloatingActionButton fabMapa;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        rvProductos = findViewById(R.id.rvProductos);
        fabCarrito = findViewById(R.id.fabCarrito);
        fabMapa = findViewById(R.id.fabMapa);
        dbHelper = new DatabaseHelper(this);

        // Insertar productos de ejemplo si la base está vacía
        if (dbHelper.getAllProducts().getCount() == 0) {
            dbHelper.addProduct("Laptop", 999.99);
            dbHelper.addProduct("Smartphone", 699.99);
            dbHelper.addProduct("Auriculares", 199.99);
        }

        // Cargar productos desde la base de datos
        productos = cargarProductosDesdeBD();
        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductoAdapter(productos, this);
        rvProductos.setAdapter(adapter);

        fabCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductosActivity.this, CarritoActivity.class);
                startActivity(intent);
            }
        });

        fabMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductosActivity.this, MapaActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<Producto> cargarProductosDesdeBD() {
        List<Producto> lista = new ArrayList<>();
        Cursor cursor = dbHelper.getAllProducts();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                // No hay descripción ni imagen en la BD, así que ponemos valores por defecto
                lista.add(new Producto(id, nombre, "Sin descripción", precio, R.mipmap.ic_launcher));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    @Override
    public void onAgregarClick(Producto producto) {
        boolean agregado = dbHelper.addToCart(producto.getId(), 1);
        if (agregado) {
            Toast.makeText(this, "Producto agregado al carrito: " + producto.getNombre(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al agregar al carrito", Toast.LENGTH_SHORT).show();
        }
    }
}