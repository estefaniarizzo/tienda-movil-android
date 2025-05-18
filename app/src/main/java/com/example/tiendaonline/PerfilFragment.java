package com.example.tiendaonline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PerfilFragment extends Fragment implements OnMapReadyCallback {
    private TextView tvNombre;
    private TextView tvEmail;
    private TextView tvRol;
    private Button btnEditarPerfil;
    private GoogleMap mMap;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        sessionManager = SessionManager.getInstance(requireContext());

        // Inicializar vistas
        tvNombre = view.findViewById(R.id.tvNombre);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvRol = view.findViewById(R.id.tvRol);
        btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil);

        // Configurar mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Cargar datos del usuario
        cargarDatosUsuario();

        return view;
    }

    private void cargarDatosUsuario() {
        tvNombre.setText(sessionManager.getUserName());
        tvEmail.setText(sessionManager.getUserEmail());
        tvRol.setText(sessionManager.getUserRol());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Configurar ubicación actual
        GeolocalizacionManager.getInstance(requireContext())
                .startLocationUpdates(requireContext(), location -> {
                    LatLng ubicacion = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(ubicacion).title("Mi ubicación"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GeolocalizacionManager.getInstance(requireContext()).stopLocationUpdates();
    }
} 