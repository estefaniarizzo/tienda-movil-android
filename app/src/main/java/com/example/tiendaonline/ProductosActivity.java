package com.example.tiendaonline;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        // Inicializar vistas
        rvProductos = findViewById(R.id.rvProductos);
        fabCarrito = findViewById(R.id.fabCarrito);

        // Configurar RecyclerView
        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        productos = crearListaProductos();
        adapter = new ProductoAdapter(productos, this);
        rvProductos.setAdapter(adapter);

        // Configurar FAB
        fabCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductosActivity.this, CarritoActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<Producto> crearListaProductos() {
        List<Producto> lista = new ArrayList<>();
        // Aquí agregaríamos productos de ejemplo
        lista.add(new Producto("Laptop", "Laptop gaming de última generación", 999.99,
                R.drawable.ic_launcher_foreground));
        lista.add(new Producto("Smartphone", "Smartphone con cámara de alta resolución", 699.99,
                R.drawable.ic_launcher_foreground));
        lista.add(new Producto("Auriculares", "Auriculares inalámbricos con cancelación de ruido", 199.99,
                R.drawable.ic_launcher_foreground));
        return lista;
    }

    @Override
    public void onAgregarClick(Producto producto) {
        // Aquí implementaríamos la lógica para agregar al carrito
        Toast.makeText(this, "Producto agregado al carrito: " + producto.getNombre(), Toast.LENGTH_SHORT).show();
    }
}