package com.example.tiendaonline;

import android.content.Intent;
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

import com.example.tiendaonline.database.AppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProductosFragment extends Fragment implements ProductoAdapter.OnProductoClickListener {

    private RecyclerView rvProductos;
    private ProductoAdapter adapter;
    private List<Producto> productos;
    private FloatingActionButton fabCarrito;
    private FloatingActionButton fabAgregarProducto;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);

        sessionManager = SessionManager.getInstance(requireContext());

        // Inicializar vistas
        rvProductos = view.findViewById(R.id.rvProductos);
        fabCarrito = view.findViewById(R.id.fabCarrito);
        fabAgregarProducto = view.findViewById(R.id.fabAgregarProducto);

        // Configurar RecyclerView
        rvProductos.setLayoutManager(new LinearLayoutManager(getContext()));
        productos = new ArrayList<>();
        adapter = new ProductoAdapter(productos, this);
        rvProductos.setAdapter(adapter);

        boolean esAdmin = "admin".equals(sessionManager.getUserRol());

        // Si es admin, permitir edición con long click
        if (esAdmin) {
            adapter.setOnProductoAdminClickListener(producto -> {
                if (getContext() != null) {
                    Intent intent = new Intent(getContext(), ProductoFormActivity.class);
                    intent.putExtra(ProductoFormActivity.EXTRA_PRODUCTO_ID, producto.getId());
                    startActivity(intent);
                }
            });
        }

        // Cargar productos desde la base de datos
        cargarProductosDesdeBD();

        // Configurar FAB del carrito
        fabCarrito.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).navegarACarrito();
            }
        });

        // FAB para agregar producto (solo admin)
        if (esAdmin) {
            fabAgregarProducto.setVisibility(View.VISIBLE);
            fabAgregarProducto.setOnClickListener(v -> {
                if (getContext() != null) {
                    Intent intent = new Intent(getContext(), ProductoFormActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            fabAgregarProducto.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recargar productos al volver del formulario
        cargarProductosDesdeBD();
    }

    private void cargarProductosDesdeBD() {
        if (getContext() == null)
            return;

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());

            // Si no hay productos, insertar algunos de ejemplo
            List<Producto> actuales = db.productoDao().getAll();
            if (actuales.isEmpty()) {
                db.productoDao().insert(new Producto("Laptop", "Laptop gaming de última generación", 999.99,
                        R.mipmap.ic_launcher));
                db.productoDao().insert(new Producto("Smartphone", "Smartphone con cámara de alta resolución", 699.99,
                        R.mipmap.ic_launcher));
                db.productoDao()
                        .insert(new Producto("Auriculares", "Auriculares inalámbricos con cancelación de ruido", 199.99,
                                R.mipmap.ic_launcher));
                actuales = db.productoDao().getAll();
            }

            List<Producto> finalActuales = actuales;
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    productos.clear();
                    productos.addAll(finalActuales);
                    adapter.notifyDataSetChanged();
                });
            }
        }).start();
    }

    @Override
    public void onAgregarClick(Producto producto) {
        CarritoManager.getInstance().agregarProducto(producto);
        Toast.makeText(getContext(), "Producto agregado al carrito: " + producto.getNombre(), Toast.LENGTH_SHORT)
                .show();
    }
}
