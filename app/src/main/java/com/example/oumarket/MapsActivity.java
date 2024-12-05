package com.example.oumarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.oumarket.Adapter.WardAdapter;
import com.example.oumarket.Class.City;
import com.example.oumarket.Class.District;
import com.example.oumarket.Class.Ward;
import com.example.oumarket.Class.AnAddress;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Common.AddressType;
import com.example.oumarket.Database.Database;
import com.example.oumarket.Adapter.CityAdapter;
import com.example.oumarket.Adapter.DistrictAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.rejowan.cutetoast.CuteToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int FINE_PERMISSION_CODE = 7;
    Location currentLocation;
    SearchView searchView;
    MaterialButtonToggleGroup toggle_group;
    AppCompatSpinner spinner_city, spinner_district, spinner_ward;
    TextInputEditText editText_name, editText_phone, editText_house_number;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button btn_home, btn_work, btn_other, btn_doneMap, btn_doneInput;
    ImageButton current_location;
    SwitchMaterial switchMaterial;

    List<City> cities;
    List<District> districts;
    List<Ward> wards;

    CityAdapter adapterCity;
    DistrictAdapter adapterDistricts;
    WardAdapter adapterWards;

    AddressType addressType = AddressType.HOME;

    Database database;
    Context context;
    LatLng centerLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        context = getBaseContext();

        database = new Database(getBaseContext());

        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(java.lang.String query) {
                java.lang.String location = query;

                List<Address> list = null;
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        list = geocoder.getFromLocationName(location, 1);

                    } catch (IOException e) {
                        Toast.makeText(MapsActivity.this, "None", Toast.LENGTH_SHORT).show();
                    }
                }
                Address address = list.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                return false;
            }

            @Override
            public boolean onQueryTextChange(java.lang.String newText) {
                return false;
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        current_location = findViewById(R.id.current_location);
        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });

        editText_name = findViewById(R.id.name);
        editText_phone = findViewById(R.id.phone);

        spinner_city = findViewById(R.id.spinner_city);
        spinner_district = findViewById(R.id.spinner_district);
        spinner_ward = findViewById(R.id.spinner_ward);

        initSpinner();

        editText_house_number = findViewById(R.id.house_number);

        btn_home = findViewById(R.id.btn_home);
        btn_work = findViewById(R.id.btn_work);
        btn_other = findViewById(R.id.btn_other);

        toggle_group = findViewById(R.id.toggle_group);
        toggle_group.check(R.id.btn_home);
        toggle_group.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btn_home) {
                    selectedTypeAddress(AddressType.HOME);
                } else if (checkedId == R.id.btn_work) {
                    selectedTypeAddress(AddressType.WORK);
                } else {
                    selectedTypeAddress(AddressType.OTHER);
                }
            }
        });

        switchMaterial = findViewById(R.id.switch_dia_chi);

        btn_doneInput = findViewById(R.id.btn_done_input);
        btn_doneInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText_name.getText().toString()) || TextUtils.isEmpty(editText_phone.getText().toString())) {
                    CuteToast.ct(context, "Hãy nhập thông tin liên hệ", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
                    return;
                }

                if (spinner_ward.getSelectedItemPosition() == 0) {
                    CuteToast.ct(context, "Hãy nhập thông tin liên hệ", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
                    return;
                }

                if (TextUtils.isEmpty(editText_house_number.getText().toString())) {
                    CuteToast.ct(context, "Hãy Số nhà, Tên đường, Tòa nhà", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
                    return;
                }

                AnAddress address = new AnAddress(editText_name.getText().toString(), editText_phone.getText().toString(), (Ward) spinner_ward.getSelectedItem(), editText_house_number.getText().toString(), addressType, switchMaterial.isChecked(), false);

                Common.CURRENTUSER.addAddress(address);

                Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child("Address").setValue(Common.CURRENTUSER.getAddresses());

                Intent intent = new Intent(MapsActivity.this, YourAddressesActivity.class);
                startActivity(intent);

                finish();

            }
        });

        btn_doneMap = findViewById(R.id.btn_done_map);
        btn_doneMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText_name.getText().toString()) || TextUtils.isEmpty(editText_phone.getText().toString())) {
                    CuteToast.ct(context, "Hãy nhập thông tin liên hệ", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
                    return;
                }

                AnAddress address = new AnAddress(editText_name.getText().toString(), editText_phone.getText().toString(), centerLatLng.latitude, centerLatLng.longitude, addressType, switchMaterial.isChecked(), true);
                address.setIsMap(true);

                Common.CURRENTUSER.addAddress(address);

                Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.CURRENTUSER.getIdUser()).child("Addresses").setValue(Common.CURRENTUSER.getAddresses());
                Intent intent = new Intent(MapsActivity.this, YourAddressesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mapFragment.getMapAsync(MapsActivity.this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

//        // Tắt mọi tương tác
//        mMap.getUiSettings().setAllGesturesEnabled(false); // Tắt toàn bộ cử chỉ
//        mMap.getUiSettings().setZoomControlsEnabled(false); // Tắt các nút zoom
//        mMap.getUiSettings().setCompassEnabled(false); // Tắt la bàn
//        mMap.getUiSettings().setMyLocationButtonEnabled(false); // Tắt nút định vị

        mMap = googleMap;
        mMap.setOnCameraIdleListener(() -> {
            centerLatLng = mMap.getCameraPosition().target;
            Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            try {

                Address address = geocoder.getFromLocation(centerLatLng.latitude, centerLatLng.longitude, 1).get(0);
                java.lang.String locationInfo = address.getAddressLine(0);

                Log.d("ZZZZZ", centerLatLng.latitude + " " + centerLatLng.longitude);

            } catch (Exception e) {
                Toast.makeText(this, "Địa chỉ không đúng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLastLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new java.lang.String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//                    mMap.addMarker(new MarkerOptions().position(myLocation).title("Your location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull java.lang.String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectedTypeAddress(AddressType s) {

        btn_home.setBackgroundColor(getResources().getColor(R.color.trang));
        btn_work.setBackgroundColor(getResources().getColor(R.color.trang));
        btn_other.setBackgroundColor(getResources().getColor(R.color.trang));

        btn_home.setTextColor(getResources().getColor(R.color.den));
        btn_work.setTextColor(getResources().getColor(R.color.den));
        btn_other.setTextColor(getResources().getColor(R.color.den));

        if (s == AddressType.HOME) {
            btn_home.setBackgroundColor(getResources().getColor(R.color.cam));
            btn_home.setTextColor(getResources().getColor(R.color.trang));
        } else if (s == AddressType.WORK) {
            btn_work.setBackgroundColor(getResources().getColor(R.color.cam));
            btn_work.setTextColor(getResources().getColor(R.color.trang));
        } else {
            btn_other.setBackgroundColor(getResources().getColor(R.color.cam));
            btn_other.setTextColor(getResources().getColor(R.color.trang));
        }

        addressType = s;

    }

    private void initSpinner() {
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    adapterDistricts.setDistrictList(new ArrayList<>());
                } else {
                    City item = (City) parent.getSelectedItem();
                    adapterDistricts.setDistrictList(Common.districts(context, R.raw.district, item.getCode()));
                }
                adapterDistricts.notifyDataSetChanged();
                spinner_district.setSelection(0, false);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    adapterWards.setWardList(new ArrayList<>());
                } else {
                    District item = (District) parent.getSelectedItem();
                    adapterWards.setWardList(Common.wards(context, R.raw.ward, item.getCode()));
                }
                adapterWards.notifyDataSetChanged();
                spinner_ward.setSelection(0, false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_ward.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cities = Common.cities(getBaseContext(), R.raw.city);
        cities.add(0, new City("-1", "Chọn Tỉnh/Thành phố"));
        adapterCity = new CityAdapter(getBaseContext(), R.layout.item_spinner, cities);
        adapterCity.setDropDownViewResource(R.layout.item_spinner);
        spinner_city.setAdapter(adapterCity);

        districts = new ArrayList<>();
        districts.add(0, new District("-1", "Chọn Quận/Huyện", "-1"));
        adapterDistricts = new DistrictAdapter(getBaseContext(), R.layout.item_spinner, districts);
        spinner_district.setAdapter(adapterDistricts);

        wards = new ArrayList<>();
        wards.add(0, new Ward("-1", "Chọn Xã/Phường", "-1", "-1"));
        adapterWards = new WardAdapter(getBaseContext(), R.layout.item_spinner, wards);
        spinner_ward.setAdapter(adapterWards);

    }

}