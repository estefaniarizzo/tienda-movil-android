package com.example.tiendaonline;

import android.database.Cursor;
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
    private CarritoAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        rvCarrito = findViewById(R.id.rvCarrito);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        dbHelper = new DatabaseHelper(this);

        // Cargar productos del carrito desde la base de datos
        carrito = cargarCarritoDesdeBD();
        rvCarrito.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarritoAdapter(carrito, new CarritoAdapter.OnCarritoClickListener() {
            @Override
            public void onEliminarClick(int cartId) {
                dbHelper.removeCartItem(cartId);
                carrito = cargarCarritoDesdeBD();
                adapter.setCarrito(carrito);
                actualizarTotal();
            }
        });
        rvCarrito.setAdapter(adapter);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carrito.isEmpty()) {
                    Toast.makeText(CarritoActivity.this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(CarritoActivity.this, "Procediendo al pago...", Toast.LENGTH_SHORT).show();
                dbHelper.clearCart();
                carrito.clear();
                adapter.setCarrito(carrito);
                actualizarTotal();
            }
        });

        actualizarTotal();
    }

    private List<Producto> cargarCarritoDesdeBD() {
        List<Producto> lista = new ArrayList<>();
        Cursor cursor = dbHelper.getCartItems();
        if (cursor.moveToFirst()) {
            do {
                int cartId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                Producto producto = new Producto(cartId, nombre, "", precio, R.mipmap.ic_launcher);
                // Usamos el campo descripcion para guardar la cantidad (truco rápido)
                producto.setDescripcion(String.valueOf(cantidad));
                lista.add(producto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    private void actualizarTotal() {
        double total = 0;
        for (Producto producto : carrito) {
            int cantidad = Integer.parseInt(producto.getDescripcion());
            total += producto.getPrecio() * cantidad;
        }
        tvTotal.setText(String.format("Total: $%.2f", total));
    }
}