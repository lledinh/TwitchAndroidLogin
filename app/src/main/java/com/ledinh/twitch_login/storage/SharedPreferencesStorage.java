package com.ledinh.twitch_login.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by Lam on 18/01/2016.
 * Cette classe permet de stocker, récupérer, supprimer n'importe quel objet dans un SharedPreferences.
 * L'objet est convertit en chaîne json via la librairie Gson puis est stocké dans le SharedPreferences.
 */
public class SharedPreferencesStorage {
    public static final String SHARED_PREFS_KEY_USER = "shared_prefs_key_json_user";
    public static final String SHARED_PREFS_KEY_TOKEN = "shared_prefs_key_token";

    /**
     * Vérifie qu'une clé est stockée.
     * @param context Le contexte.
     * @param key La clé.
     * @return
     */
    public static boolean contains(Context context, String key){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.contains(key);
    }

    /**
     * Récupère la valeur associée à une clé.
     * @param context Le contexte.
     * @param key La clé.
     * @param clazz La classe du type de la donnée que l'on récupère.
     * @param <T> Le type de la donnée que l'on récupère.
     * @return La donnée stockée.
     */
    public static <T> T retrieve(Context context, String key, Class<T> clazz){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonValue = sharedPreferences.getString(key, null);

        Gson gson = new Gson();
        T result = gson.fromJson(jsonValue, clazz);

        return result;
    }

    /**
     * Stocke une donnée que l'on associe à une clé.
     * @param context Le contexte.
     * @param key La clé.
     * @param value La valeur à stocker.
     * @param <T> Le type de la donnée que l'on stocke.
     */
    public static <T> void store(Context context, String key, T value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(value);

        editor.putString(key, json);
        editor.apply();
    }

    /**
     * Supprime une clé et sa valeur.
     * @param context Le contexte.
     * @param key La clé.
     */
    public static void reset(Context context, String key){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(key);
        editor.apply();
    }
}
