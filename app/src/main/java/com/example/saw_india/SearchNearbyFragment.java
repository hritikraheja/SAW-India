package com.example.saw_india;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.saw_india.modalClasses.AnimalShelter;
import com.example.saw_india.modalClasses.RecyclerViewAdapterForSearchNearbyFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;

public class SearchNearbyFragment extends Fragment{

    LinkedList<AnimalShelter> animalSheltersNearLocation = new LinkedList<>();
    String phoneNumber;
    MapView mapView;
    String name;
    String address;
    double lat;
    double lng;
    String placeId;
    RecyclerView recyclerView;
    public static double myLat;
    public static double myLng;
    ConstraintLayout loadingLayout;
    private GoogleMap googleMap;
    SwipeRefreshLayout swipeRefreshLayout;

    public double getMyLat() {
        return myLat;
    }

    public void setMyLat(Double myLat) {
        this.myLat = myLat;
    }

    public double getMyLng() {
        return myLng;
    }

    public void setMyLng(Double myLng) {
        this.myLng = myLng;
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_search_nearby_fragment, container, false);
        if (savedInstanceState == null) {
            loadingLayout = view.findViewById(R.id.loadingLayout);
            mapView = view.findViewById(R.id.mapView);
            swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
            final GetJsonFromUrl getJsonFromUrl = new GetJsonFromUrl();
            swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
                @Override
                public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                    return mapView.getScrollY() != 0;
                }
            });
            mapView.onCreate(null);
            mapView.onResume();
            recyclerView = view.findViewById(R.id.mapRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            Places.initialize(view.getContext(), getString(R.string.GOOGLE_PLACES_API_KEY));
            final FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            @SuppressLint("MissingPermission") final Task<Location> location = fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, new CancellationTokenSource().getToken());
            location.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location myLocation = task.getResult();
                        if (myLocation==null){
                            Toast.makeText(view.getContext(),"Unable To Fetch Your Last Known Location", Toast.LENGTH_SHORT).show();
                        } else {
                            setMyLat(myLocation.getLatitude());
                            setMyLng(myLocation.getLongitude());
                            mapView.getMapAsync(new OnMapReadyCallback() {
                                @SuppressLint("MissingPermission")
                                @Override
                                public void onMapReady(@NonNull final GoogleMap gMap) {
                                    googleMap = gMap;
                                    googleMap.setMyLocationEnabled(true);
                                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                        @Override
                                        public void onMapLoaded() {
                                            LatLng myLocation = new LatLng(getMyLat(), getMyLng());
                                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.5f));
                                        }
                                    });
                                }
                            });
                        }
                    }
                    getJsonFromUrl.execute(String.valueOf(getMyLat()), String.valueOf(getMyLng()));
                }
            });
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mapView.onCreate(null);
                    mapView.onResume();
                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onMapReady(@NonNull final GoogleMap gMap) {
                            googleMap = gMap;
                            googleMap.setMyLocationEnabled(true);
                            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                @Override
                                public void onMapLoaded() {
                                    LatLng myLocation = new LatLng(getMyLat(), getMyLng());
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(11.8f));
                                }
                            });
                            if(animalSheltersNearLocation.size()!=0) {
                                loadingLayout.setVisibility(View.VISIBLE);
                                for (AnimalShelter animalShelter : animalSheltersNearLocation) {
                                    double shelterLatitude = animalShelter.getLat();
                                    double shelterLongitude = animalShelter.getLng();
                                    LatLng location = new LatLng(shelterLatitude, shelterLongitude);
                                    googleMap.addMarker(new MarkerOptions().position(location).title(animalShelter.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                                    LatLng myLocation = new LatLng(myLat, myLng);
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.5f));
                                }
                                new CountDownTimer(2000,1000){
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        loadingLayout.setVisibility(View.INVISIBLE);
                                    }
                                }.start();
                                swipeRefreshLayout.setRefreshing(false);
                            } else {
                                loadingLayout.setVisibility(View.VISIBLE);
                                getJsonFromUrl.execute(String.valueOf(getMyLat()), String.valueOf(getMyLng()));
                            }
                        }
                    });
                }
            });
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.onSaveInstanceState(savedInstanceState);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @SuppressLint("StaticFieldLeak")
    class GetJsonFromUrl extends AsyncTask<String, Integer, String> {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected String doInBackground(String... string) {
            double mLat = Double.parseDouble(string[0]);
            double mLng = Double.parseDouble(string[1]);
            String result = null;
            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query=animal+shelter&location=" + mLat + "," + mLng + "&radius=10000&key=" + getString(R.string.GOOGLE_PLACES_API_KEY));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    StringBuilder sb = new StringBuilder();
                    String temp;
                    while ((temp = bufferedReader.readLine()) != null) {
                        sb.append(temp);
                        sb.append("\n");
                    }
                    reader.close();
                    connection.disconnect();
                    result = sb.toString();
                } else {
                    result = "ERROR: HTTP NOT OK";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject allData = new JSONObject(s);
                JSONArray array = allData.getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    name = obj.getString("name");
                    address = obj.getString("formatted_address");
                    lat = Double.parseDouble(obj.getJSONObject("geometry").getJSONObject("location").getString("lat"));
                    lng = Double.parseDouble(obj.getJSONObject("geometry").getJSONObject("location").getString("lng"));
                    placeId = obj.getString("place_id");
                    AnimalShelter animalShelter = new AnimalShelter(name, lat, lng, address, "Not Available", placeId, 0);
                    animalSheltersNearLocation.add(animalShelter);
                    for (AnimalShelter a : animalSheltersNearLocation) {
                        double shelterLatitude = a.getLat();
                        double shelterLongitude = a.getLng();
                        LatLng location = new LatLng(shelterLatitude, shelterLongitude);
                        googleMap.addMarker(new MarkerOptions().position(location).title(a.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        LatLng myLocation = new LatLng(myLat, myLng);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.5f));
                    }
                    Collections.sort(animalSheltersNearLocation);
                    recyclerView.setAdapter(new RecyclerViewAdapterForSearchNearbyFragment(animalSheltersNearLocation));
                    loadingLayout.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    loadingLayout.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class GetPhoneNumberFromUrl extends AsyncTask<String, Integer, String> {
        int pos;

        @Override
        protected String doInBackground(String... strings) {
            String pId = strings[0];
            pos = Integer.parseInt(strings[1]);
            String result = "";
            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/place/details/json?place_id=" + pId + "&fields=formatted_phone_number&key=" + getString(R.string.GOOGLE_PLACES_API_KEY));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    StringBuilder sb = new StringBuilder();
                    String temp;
                    while ((temp = bufferedReader.readLine()) != null) {
                        sb.append(temp);
                        sb.append("\n");
                    }
                    connection.disconnect();
                    reader.close();
                    result = sb.toString();
                } else {
                    result = "ERROR: HTTP NOT OK";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s).getJSONObject("result");
                try {
                    phoneNumber = object.getString("formatted_phone_number");
                } catch (JSONException e) {
                    phoneNumber = "Not Available";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            animalSheltersNearLocation.get(pos).setPhoneNumber(phoneNumber);
            if (phoneNumber.compareTo("Not Available") != 0) {
                animalSheltersNearLocation.get(pos).setPhoneNumberAvailableOrNot(1);
            }
            for (AnimalShelter animalShelter : animalSheltersNearLocation) {
                double shelterLatitude = animalShelter.getLat();
                double shelterLongitude = animalShelter.getLng();
                LatLng location = new LatLng(shelterLatitude, shelterLongitude);
                googleMap.addMarker(new MarkerOptions().position(location).title(animalShelter.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                LatLng myLocation = new LatLng(myLat, myLng);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.5f));
            }
            Collections.sort(animalSheltersNearLocation);
            recyclerView.setAdapter(new RecyclerViewAdapterForSearchNearbyFragment(animalSheltersNearLocation));
            if (pos == animalSheltersNearLocation.size() - 1) {
                loadingLayout.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
}