package com.example.tiendaonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {
    private EditText etNombre, etEmail, etPassword, etConfirmarPassword, etTelefono;
    private Button btnRegistrar, btnVolver;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);
        etTelefono = findViewById(R.id.etTelefono);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolver = findViewById(R.id.btnVolver);

        dbHelper = new DatabaseHelper(this);

        // Configurar listeners
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    String nombre = etNombre.getText().toString();
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    // Guardar usuario en la base de datos
                    boolean registrado = dbHelper.registerUser(nombre, email, password);
                    if (registrado) {
                        Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegistroActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean validarCampos() {
        String nombre = etNombre.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmarPassword = etConfirmarPassword.getText().toString();
        String telefono = etTelefono.getText().toString();

        if (nombre.isEmpty()) {
            etNombre.setError("El nombre es requerido");
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("El email es requerido");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email inválido");
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("La contraseña es requerida");
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("La contraseña debe tener al menos 6 caracteres");
            return false;
        }

        if (!password.equals(confirmarPassword)) {
            etConfirmarPassword.setError("Las contraseñas no coinciden");
            return false;
        }

        if (telefono.isEmpty()) {
            etTelefono.setError("El teléfono es requerido");
            return false;
        }

        return true;
    }
}