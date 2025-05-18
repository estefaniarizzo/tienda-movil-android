package com.example.tiendaonline;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "TiendaOnlineSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ROL = "userRol";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    private static SessionManager instance;

    private SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    public void crearSesion(Usuario usuario) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, usuario.getId());
        editor.putString(KEY_USER_EMAIL, usuario.getEmail());
        editor.putString(KEY_USER_NAME, usuario.getNombre());
        editor.putString(KEY_USER_ROL, usuario.getRol());
        editor.commit();
    }

    public void cerrarSesion() {
        editor.clear();
        editor.commit();
    }

    public boolean estaLogueado() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "");
    }

    public String getUserRol() {
        return pref.getString(KEY_USER_ROL, "");
    }
} 