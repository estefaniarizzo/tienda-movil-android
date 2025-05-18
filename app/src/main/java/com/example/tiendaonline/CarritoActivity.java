package com.example.tiendaonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarritoActivity extends AppCompatActivity {
    private RecyclerView rvCarrito;
    private TextView tvTotal;
    private Button btnCheckout;
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
        List<Producto> carrito = CarritoManager.getInstance().getCarrito();
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
                Intent intent = new Intent(CarritoActivity.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });

        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = CarritoManager.getInstance().getTotal();
        tvTotal.setText(String.format("Total: $%.2f", total));
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        actualizarTotal();
    }
}