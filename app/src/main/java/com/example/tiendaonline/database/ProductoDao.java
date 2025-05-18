package com.example.tiendaonline.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tiendaonline.Producto;

import java.util.List;

@Dao
public interface ProductoDao {
    @Query("SELECT * FROM productos")
    List<Producto> getAll();

    @Query("SELECT * FROM productos WHERE id = :id")
    Producto getById(int id);

    @Insert
    void insert(Producto producto);

    @Update
    void update(Producto producto);

    @Delete
    void delete(Producto producto);
} 