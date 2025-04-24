package com.example.tiendaonline;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CarritoActivity extends AppCompatActivity {
    private RecyclerView rvCarrito;
    private TextView tvTotal;
    private Button btnCheckout;
    private List<Producto> carrito;
    private ProductoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        // Inicializar vistas
        rvCarrito = findViewById(R.id.rvCarrito);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        // Configurar RecyclerView
        rvCarrito.setLayoutManager(new LinearLayoutManager(this));
        carrito = new ArrayList<>(); // Aquí cargaríamos los productos del carrito
        adapter = new ProductoAdapter(carrito, null);
        rvCarrito.setAdapter(adapter);

        // Configurar botón de checkout
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carrito.isEmpty()) {
                    Toast.makeText(CarritoActivity.this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Aquí implementaríamos la lógica de checkout
                Toast.makeText(CarritoActivity.this, "Procediendo al pago...", Toast.LENGTH_SHORT).show();
            }
        });

        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = 0;
        for (Producto producto : carrito) {
            total += producto.getPrecio();
        }
        tvTotal.setText(String.format("Total: $%.2f", total));
    }
}