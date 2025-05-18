package com.example.tiendaonline;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {
    private EditText etNombre;
    private EditText etDireccion;
    private EditText etTarjeta;
    private EditText etCVV;
    private TextView tvTotal;
    private Button btnConfirmarPago;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre);
        etDireccion = findViewById(R.id.etDireccion);
        etTarjeta = findViewById(R.id.etTarjeta);
        etCVV = findViewById(R.id.etCVV);
        tvTotal = findViewById(R.id.tvTotal);
        btnConfirmarPago = findViewById(R.id.btnConfirmarPago);

        // Mostrar total
        double total = CarritoManager.getInstance().getTotal();
        tvTotal.setText(String.format("Total a pagar: $%.2f", total));

        // Configurar botón de confirmar pago
        btnConfirmarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    procesarPago();
                }
            }
        });
    }

    private boolean validarCampos() {
        if (etNombre.getText().toString().trim().isEmpty()) {
            etNombre.setError("Ingrese su nombre");
            return false;
        }
        if (etDireccion.getText().toString().trim().isEmpty()) {
            etDireccion.setError("Ingrese su dirección");
            return false;
        }
        if (etTarjeta.getText().toString().trim().isEmpty()) {
            etTarjeta.setError("Ingrese el número de tarjeta");
            return false;
        }
        if (etCVV.getText().toString().trim().isEmpty()) {
            etCVV.setError("Ingrese el CVV");
            return false;
        }
        return true;
    }

    private void procesarPago() {
        // Aquí iría la lógica real de procesamiento de pago
        // Por ahora solo simulamos el proceso
        Toast.makeText(this, "Procesando pago...", Toast.LENGTH_SHORT).show();
        
        // Simulamos un delay para el procesamiento
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Limpiamos el carrito
                CarritoManager.getInstance().limpiarCarrito();
                
                // Mostramos mensaje de éxito
                Toast.makeText(CheckoutActivity.this, 
                    "¡Pago realizado con éxito! Gracias por su compra.", 
                    Toast.LENGTH_LONG).show();
                
                // Volvemos a la actividad de productos
                finish();
            }
        }, 2000);
    }
} 