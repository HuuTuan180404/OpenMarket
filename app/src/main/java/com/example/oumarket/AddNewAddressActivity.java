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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class AddNewAddressActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int FINE_PERMISSION_CODE = 7;
    Location currentLocation;
    MaterialButtonToggleGroup toggle_group;
    AppCompatSpinner spinner_city, spinner_district, spinner_ward;
    TextInputEditText editText_name, editText_phone, editText_house_number;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button btn_home, btn_work, btn_other, btn_doneInput;
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
        btn_doneInput.setOnClickListener(v -> {

            if (TextUtils.isEmpty(editText_name.getText().toString()) || TextUtils.isEmpty(editText_phone.getText().toString())) {
                CuteToast.ct(context, "Hãy nhập thông tin liên hệ", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
                return;
            }

            if (spinner_ward.getSelectedItemPosition() == 0) {
                CuteToast.ct(context, "Hãy chọn địa chỉ", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
                return;
            }

            if (TextUtils.isEmpty(editText_house_number.getText().toString())) {
                CuteToast.ct(context, "Hãy Số nhà, Tên đường, Tòa nhà", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
                return;
            }

            AnAddress address = new AnAddress(editText_house_number.getText().toString(), "VN", switchMaterial.isChecked(), editText_name.getText().toString(), editText_phone.getText().toString(), addressType, (Ward) spinner_ward.getSelectedItem());

            addNewAddress(address);

        });

        mapFragment.getMapAsync(AddNewAddressActivity.this);

    }

    private void moveCameraMap(String s) {
        String location = s;

        List<Address> list = null;
        if (location != null) {
            Geocoder geocoder = new Geocoder(AddNewAddressActivity.this);
            try {
                list = geocoder.getFromLocationName(location, 1);
                if (list != null && !list.isEmpty()) {
                    Address address = list.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                } else {
                    CuteToast.ct(AddNewAddressActivity.this, "Không tìm thấy", Toast.LENGTH_SHORT, CuteToast.INFO, true).show();
                }
            } catch (IOException e) {
                Toast.makeText(AddNewAddressActivity.this, "None", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addNewAddress(AnAddress anAddress) {
        List<AnAddress> list = Common.CURRENTUSER.getAddresses();

        if (list == null || list.isEmpty()) {
            anAddress.setIsDefault(true);
            list = new ArrayList<>();
        } else {
            if (anAddress.getIsDefault()) {
                for (AnAddress i : list) {
                    i.setIsDefault(false);
                }
            }
        }

        list.add(anAddress);
        Common.CURRENTUSER.setAddresses(list);

        Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.CURRENTUSER.getIdUser()).child("Addresses").setValue(list);
        Intent intent = new Intent(AddNewAddressActivity.this, YourAddressesActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnCameraIdleListener(() -> {
            centerLatLng = mMap.getCameraPosition().target;
            Geocoder geocoder = new Geocoder(AddNewAddressActivity.this, Locale.getDefault());
            try {

                Address address = geocoder.getFromLocation(centerLatLng.latitude, centerLatLng.longitude, 1).get(0);
                String locationInfo = address.getAddressLine(0);

            } catch (Exception e) {
//                Toast.makeText(this, "Địa chỉ không đúng", Toast.LENGTH_SHORT).show();
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
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//                    mMap.addMarker(new MarkerOptions().position(myLocation).title("Your location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                    moveCameraMap(item.getName());
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
                    City city = (City) spinner_city.getSelectedItem();
                    moveCameraMap(item.getName() + ", " + city.getName());
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
                if (position != 0) {
                    Ward item = (Ward) parent.getSelectedItem();
                    moveCameraMap(item.getPath());
                }
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