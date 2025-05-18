package com.example.tiendaonline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiendaonline.database.AppDatabase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoogleSignIn;
    private GoogleSignInClient mGoogleSignInClient;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = SessionManager.getInstance(this);

        // Si ya está logueado, ir a MainActivity
        if (sessionManager.estaLogueado()) {
            navegarAMainActivity();
            return;
        }

        // Inicializar vistas
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);

        // Configurar Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId() // Solicitar ID del usuario
                .requestProfile() // Solicitar información del perfil
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Configurar botones
        btnLogin.setOnClickListener(v -> iniciarSesionNormal());
        btnGoogleSignIn.setOnClickListener(v -> iniciarSesionGoogle());
    }

    private void iniciarSesionNormal() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ejecutar en un hilo separado
        new Thread(() -> {
            Usuario usuario = AppDatabase.getInstance(this)
                    .usuarioDao()
                    .login(email, password);

            runOnUiThread(() -> {
                if (usuario != null) {
                    sessionManager.crearSesion(usuario);
                    navegarAMainActivity();
                } else {
                    Toast.makeText(LoginActivity.this, 
                            "Credenciales incorrectas", 
                            Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void iniciarSesionGoogle() {
        try {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } catch (Exception e) {
            Log.e(TAG, "Error al iniciar Google Sign-In: " + e.getMessage());
            Toast.makeText(this, "Error al iniciar sesión con Google: " + e.getMessage(), 
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                manejarResultadoGoogleSignIn(task);
            } catch (Exception e) {
                Log.e(TAG, "Error en onActivityResult: " + e.getMessage());
                Toast.makeText(this, "Error al procesar el resultado de Google: " + e.getMessage(), 
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void manejarResultadoGoogleSignIn(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                Log.d(TAG, "Google Sign-In exitoso: " + account.getEmail());
                
                // Crear usuario con datos de Google
                Usuario usuario = new Usuario(
                        account.getEmail(),
                        "", // No necesitamos contraseña para Google
                        account.getDisplayName() != null ? account.getDisplayName() : "Usuario Google",
                        "cliente" // Rol por defecto
                );

                // Guardar usuario en la base de datos
                new Thread(() -> {
                    try {
                        AppDatabase.getInstance(this)
                                .usuarioDao()
                                .insert(usuario);
                        
                        runOnUiThread(() -> {
                            sessionManager.crearSesion(usuario);
                            navegarAMainActivity();
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Error al guardar usuario: " + e.getMessage());
                        runOnUiThread(() -> 
                            Toast.makeText(LoginActivity.this, 
                                    "Error al guardar datos del usuario: " + e.getMessage(), 
                                    Toast.LENGTH_LONG).show()
                        );
                    }
                }).start();
            }
        } catch (ApiException e) {
            Log.e(TAG, "Error en Google Sign-In: " + e.getStatusCode() + " - " + e.getMessage());
            String mensaje = "Error al iniciar sesión con Google: ";
            switch (e.getStatusCode()) {
                case 7:
                    mensaje += "No hay conexión a Internet";
                    break;
                case 10:
                    mensaje += "Error en la configuración de la cuenta";
                    break;
                case 12:
                    mensaje += "Inicio de sesión cancelado";
                    break;
                default:
                    mensaje += e.getMessage();
            }
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        }
    }

    private void navegarAMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
} 