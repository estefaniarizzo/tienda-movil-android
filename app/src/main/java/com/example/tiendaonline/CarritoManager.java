package com.example.tiendaonline;

import java.util.ArrayList;
import java.util.List;

public class CarritoManager {
    private static CarritoManager instance;
    private List<Producto> carrito;

    private CarritoManager() {
        carrito = new ArrayList<>();
    }

    public static CarritoManager getInstance() {
        if (instance == null) {
            instance = new CarritoManager();
        }
        return instance;
    }

    public void agregarProducto(Producto producto) {
        carrito.add(producto);
    }

    public List<Producto> getCarrito() {
        return carrito;
    }

    public void limpiarCarrito() {
        carrito.clear();
    }

    public double getTotal() {
        double total = 0;
        for (Producto producto : carrito) {
            total += producto.getPrecio();
        }
        return total;
    }
} 