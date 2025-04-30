package com.example.tiendaonline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {
    private List<Producto> carrito;
    private OnCarritoClickListener listener;

    public interface OnCarritoClickListener {
        void onEliminarClick(int cartId);
    }

    public CarritoAdapter(List<Producto> carrito, OnCarritoClickListener listener) {
        this.carrito = carrito;
        this.listener = listener;
    }

    public void setCarrito(List<Producto> carrito) {
        this.carrito = carrito;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        Producto producto = carrito.get(position);
        holder.bind(producto);
    }

    @Override
    public int getItemCount() {
        return carrito.size();
    }

    class CarritoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProducto;
        private TextView tvNombre;
        private TextView tvDescripcion;
        private TextView tvPrecio;
        private Button btnAgregar;
        private Button btnEliminar;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProducto = itemView.findViewById(R.id.ivProducto);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnAgregar = itemView.findViewById(R.id.btnAgregar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        public void bind(final Producto producto) {
            ivProducto.setImageResource(producto.getImagen());
            tvNombre.setText(producto.getNombre());
            tvDescripcion.setText("Cantidad: " + producto.getDescripcion());
            tvPrecio.setText(String.format("$%.2f", producto.getPrecio()));
            btnAgregar.setVisibility(View.GONE); // No se usa en el carrito
            btnEliminar.setVisibility(View.VISIBLE);
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onEliminarClick(producto.getId());
                    }
                }
            });
        }
    }
}