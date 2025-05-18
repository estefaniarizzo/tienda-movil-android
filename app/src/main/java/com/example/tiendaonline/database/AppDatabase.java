package com.example.tiendaonline.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tiendaonline.Producto;
import com.example.tiendaonline.Usuario;

@Database(entities = {Producto.class, Usuario.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract ProductoDao productoDao();
    public abstract UsuarioDao usuarioDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "tienda_db"
            ).build();
        }
        return instance;
    }
} 