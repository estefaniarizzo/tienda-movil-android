package com.example.tiendaonline.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tiendaonline.Usuario;

@Dao
public interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    Usuario login(String email, String password);

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    Usuario getByEmail(String email);

    @Insert
    void insert(Usuario usuario);
} 