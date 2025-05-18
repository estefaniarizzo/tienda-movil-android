package com.example.tiendaonline;

import android.app.Application;

import com.example.tiendaonline.database.AppDatabase;

public class TiendaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Inicializar la base de datos
        AppDatabase db = AppDatabase.getInstance(this);
        
        // Agregar usuario de prueba en un hilo separado
        new Thread(() -> {
            // Verificar si ya existe el usuario
            if (db.usuarioDao().getByEmail("admin@tienda.com") == null) {
                // Crear usuario administrador
                Usuario admin = new Usuario(
                    "admin@tienda.com",
                    "admin123",
                    "Administrador",
                    "admin"
                );
                db.usuarioDao().insert(admin);
            }
        }).start();
    }
} 