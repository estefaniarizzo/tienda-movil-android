package com.example.tiendaonline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProductosFragment extends Fragment implements ProductoAdapter.OnProductoClickListener {
    private RecyclerView rvProductos;
    private ProductoAdapter adapter;
    private List<Producto> productos;
    private FloatingActionButton fabCarrito;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);

        // Inicializar vistas
        rvProductos = view.findViewById(R.id.rvProductos);
        fabCarrito = view.findViewById(R.id.fabCarrito);

        // Configurar RecyclerView
        rvProductos.setLayoutManager(new LinearLayoutManager(getContext()));
        productos = crearListaProductos();
        adapter = new ProductoAdapter(productos, this);
        rvProductos.setAdapter(adapter);

        // Configurar FAB
        fabCarrito.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).navegarACarrito();
            }
        });

        return view;
    }

    private List<Producto> crearListaProductos() {
        List<Producto> lista = new ArrayList<>();
        lista.add(new Producto("Laptop", "Laptop gaming de última generación", 999.99,
                R.mipmap.ic_launcher));
        lista.add(new Producto("Smartphone", "Smartphone con cámara de alta resolución", 699.99,
                R.mipmap.ic_launcher));
        lista.add(new Producto("Auriculares", "Auriculares inalámbricos con cancelación de ruido", 199.99,
                R.mipmap.ic_launcher));
        return lista;
    }

    @Override
    public void onAgregarClick(Producto producto) {
        CarritoManager.getInstance().agregarProducto(producto);
        Toast.makeText(getContext(), "Producto agregado al carrito: " + producto.getNombre(), Toast.LENGTH_SHORT).show();
    }
} 