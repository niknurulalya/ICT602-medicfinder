package com.example.medicfinder;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.medicfinder.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Vector;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final String URL = "http://192.168.1.5/medicfinder/get_clinics.php";
    private RequestQueue requestQueue;
    private LatLng centerlocation;
    private Vector<MarkerOptions> markerOptionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        requestQueue = Volley.newRequestQueue(this);
        centerlocation = new LatLng(5.77141407353668, 102.22311181937711);
        markerOptionsList = new Vector<>();

        markerOptionsList.add(new MarkerOptions().title("UiTM Kampus Machang")
                .position(new LatLng(5.75710436662611, 102.27347947511261))
                .snippet("Student View"));
        markerOptionsList.add(new MarkerOptions().title("Klinik Primer Machang")
                .position(new LatLng(5.782204132243936, 102.20364511348664))
                .snippet("Open 9am-10pm"));
        markerOptionsList.add(new MarkerOptions().title("Klinik Bharu Machang")
                .position(new LatLng(5.764356386657738, 102.21875573746746))
                .snippet("Open 9am-10pm"));
        markerOptionsList.add(new MarkerOptions().title("Klinik Pergigian Machang")
                .position(new LatLng(5.7648899863757075, 102.2261032117134))
                .snippet("Open 9am-10pm"));
        markerOptionsList.add(new MarkerOptions().title("Klinik Wan Fuad")
                .position(new LatLng(5.764719422041521, 102.22043957206584))
                .snippet("Open 9am-10pm"));
        markerOptionsList.add(new MarkerOptions().title("KLINIK PERGIGIAN DR. BAIHAKI")
                .position(new LatLng(5.765438925726011, 102.21968804696296))
                .snippet("Open 9am-10pm"));
        markerOptionsList.add(new MarkerOptions().title("Klinik Gigi Zuraina")
                .position(new LatLng(5.76413189262791, 102.21962169964601))
                .snippet("Open 9am-10pm"));
        markerOptionsList.add(new MarkerOptions().title("Klinik Familia")
                .position(new LatLng(5.771661983489821, 102.17563522665819))
                .snippet("Open 9am-10pm"));
        markerOptionsList.add(new MarkerOptions().title("Klinik Desa Wakaf Bata")
                .position(new LatLng(5.777160449165909, 102.21377834411871))
                .snippet("Open 9am-10pm"));


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // utk tambah semua marker statik
        for (MarkerOptions mark : markerOptionsList) {
            mMap.addMarker(mark);
        }

        // Ni ambik data dari database
        fetchClinicLocations();


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerlocation, 12));
    }

    private void fetchClinicLocations() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                String name = obj.getString("name");
                                String description = obj.getString("description");
                                double lat = obj.getDouble("lat");
                                double lng = obj.getDouble("lng");

                                LatLng location = new LatLng(lat, lng);
                                mMap.addMarker(new MarkerOptions()
                                        .position(location)
                                        .title(name)
                                        .snippet(description)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                );
                            }
                        } catch (JSONException e) {
                            Log.e("MapsActivity", "JSON Parsing Error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MapsActivity", "Error fetching data: " + error.getMessage());
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

}

