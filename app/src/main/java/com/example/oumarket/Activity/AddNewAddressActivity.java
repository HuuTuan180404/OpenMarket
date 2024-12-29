package com.example.oumarket.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.oumarket.Adapter.WardAdapter;
import com.example.oumarket.Class.City;
import com.example.oumarket.Helper.Customer_LoadingDialog;
import com.example.oumarket.Class.District;
import com.example.oumarket.Class.Ward;
import com.example.oumarket.Class.AnAddress;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Common.AddressType;
import com.example.oumarket.Database.Database;
import com.example.oumarket.Adapter.CityAdapter;
import com.example.oumarket.Adapter.DistrictAdapter;
import com.example.oumarket.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
    MaterialButtonToggleGroup toggle_group;
    AppCompatSpinner spinner_city, spinner_district, spinner_ward;
    TextInputEditText editText_name, editText_phone, editText_house_number;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button btn_home, btn_work, btn_other, btn_save;
    ImageButton current_location;
    SwitchMaterial switchMaterial;

    LinearLayout layout_btn_current_location;

    Location currentLocation;

    List<City> cities;
    List<District> districts;
    List<Ward> wards;

    CityAdapter adapterCity;
    DistrictAdapter adapterDistricts;
    WardAdapter adapterWards;

    AddressType addressType;

    Database database;
    Context context;

    List<String> addressText;

    int getIndexAnAddress;

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);

        getIndexAnAddress = getIntent().getIntExtra("editAnAddress", -1);

        requestPermissions();

        initMap();

        initView();

        initSpinner();

        handleIntent();

    }

    private void handleIntent() {
        if (getIndexAnAddress != -1) {

            btn_save.setOnClickListener(v -> {
                if (validateAndSaveAddress()) {
                    AnAddress address = new AnAddress(editText_house_number.getText().toString(), switchMaterial.isChecked(), editText_name.getText().toString(), editText_phone.getText().toString(), addressType, (Ward) spinner_ward.getSelectedItem());
                    editAnAddress(address);
                }
            });
            if (getIndexAnAddress != -1) {
                AnAddress getAnAddress = Common.CURRENTUSER.getAddresses().get(getIndexAnAddress);
                editText_house_number.setText(getAnAddress.getAddress());
                editText_name.setText(getAnAddress.getName());
                editText_phone.setText(getAnAddress.getPhone());
                switchMaterial.setChecked(getAnAddress.getIsDefault());
                if (getAnAddress.getTypeAddress() == AddressType.HOME) btn_home.performClick();
                else if (getAnAddress.getTypeAddress() == AddressType.WORK) btn_work.performClick();
                else btn_other.performClick();
                String[] ss = getAnAddress.getWard().getPath().split(",");
                addressText = new ArrayList<>();
                for (int i = 0; i < ss.length; i++) {
                    addressText.add(ss[i].trim());
                }
                updateSpinnerCity(addressText.get(0), addressText.get(1), addressText.get(2));
            }
        } else {
            btn_save.setOnClickListener(v -> {
                if (validateAndSaveAddress()) {
                    AnAddress address = new AnAddress(editText_house_number.getText().toString(), switchMaterial.isChecked(), editText_name.getText().toString(), editText_phone.getText().toString(), addressType, (Ward) spinner_ward.getSelectedItem());
                    addNewAddress(address);
                }
            });
        }
    }

    private boolean validateAndSaveAddress() {
        if (TextUtils.isEmpty(editText_name.getText().toString()) || TextUtils.isEmpty(editText_phone.getText().toString())) {
            CuteToast.ct(context, "Hãy nhập thông tin liên hệ", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
            return false;
        }
        if (spinner_ward.getSelectedItemPosition() == 0) {
            CuteToast.ct(context, "Hãy chọn địa chỉ", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
            return false;
        }
        if (TextUtils.isEmpty(editText_house_number.getText().toString())) {
            CuteToast.ct(context, "Hãy Số nhà, Tên đường, Tòa nhà", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
            return false;
        }
        return true;
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void initView() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        context = getBaseContext();

        database = new Database(getBaseContext());

        current_location = findViewById(R.id.current_location);
        current_location.setOnClickListener(v -> {
            getCurrentLocation();
        });

        editText_name = findViewById(R.id.name);
        editText_phone = findViewById(R.id.phone);
        editText_house_number = findViewById(R.id.house_number);

        spinner_city = findViewById(R.id.spinner_city);
        spinner_district = findViewById(R.id.spinner_district);
        spinner_ward = findViewById(R.id.spinner_ward);

        layout_btn_current_location = findViewById(R.id.layout_btn_current_location);
        layout_btn_current_location.setOnClickListener(v -> {

            current_location.performClick();

            new Handler().postDelayed(() -> {
                getAddressFromLocation();
            }, 500);

        });

        btn_home = findViewById(R.id.btn_home);
        btn_work = findViewById(R.id.btn_work);
        btn_other = findViewById(R.id.btn_other);

        toggle_group = findViewById(R.id.toggle_group);
        toggle_group.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btn_home) {
                    selectedTypeAddress(AddressType.HOME);
                } else if (checkedId == R.id.btn_work) {
                    selectedTypeAddress(AddressType.WORK);
                } else if (checkedId == R.id.btn_other) {
                    selectedTypeAddress(AddressType.OTHER);
                } else
                    selectedTypeAddress(AddressType.HOME);
            }
        });

        switchMaterial = findViewById(R.id.switch_dia_chi);

        btn_save = findViewById(R.id.btn_save);
    }

    private void selectedTypeAddress(AddressType s) {
        addressType = s;
    }

    private void editAnAddress(AnAddress newAnAddress) {
        List<AnAddress> list = Common.CURRENTUSER.getAddresses();

        if (newAnAddress.getIsDefault()) {
            list.get(0).setIsDefault(false);
            list.add(0, newAnAddress);
        } else list.add(getIndexAnAddress, newAnAddress);

        list.remove(getIndexAnAddress + 1);

        Common.CURRENTUSER.setAddresses(list);
        Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.CURRENTUSER.getIdUser()).child("Addresses").setValue(list);

        finish();
    }

    private void addNewAddress(AnAddress anAddress) {
        List<AnAddress> list = Common.CURRENTUSER.getAddresses();

        if (list == null || list.isEmpty()) {
            anAddress.setIsDefault(true);
            list = new ArrayList<>();
            list.add(0, anAddress);
        } else {
            if (anAddress.getIsDefault()) {
                list.get(0).setIsDefault(false);
                list.add(0, anAddress);
            } else list.add(anAddress);
        }

        Common.CURRENTUSER.setAddresses(list);
        Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.CURRENTUSER.getIdUser()).child("Addresses").setValue(list);

        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void getCurrentLocation() {

        Customer_LoadingDialog dialog_getCurrentLocation = new Customer_LoadingDialog(this, "Loading...");
        dialog_getCurrentLocation.show();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            dialog_getCurrentLocation.dismiss();
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
                mMap.addMarker(new MarkerOptions().position(myLocation));
            }
        });

        dialog_getCurrentLocation.dismiss();
    }

    private void getAddressFromLocation() {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 5);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String[] ss = address.getAddressLine(0).split(",");
                addressText = new ArrayList<>();
                for (int i = ss.length - 4; i < ss.length; i++) {
                    addressText.add(ss[i].trim());
                }
                updateSpinnerCity(addressText.get(0), addressText.get(1), addressText.get(2));
            }
        } catch (IOException e) {
            Log.e("GeocoderError", "Failed to get address: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean isDenied = false;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    CuteToast.ct(this, permissions[i] + " granted!", Toast.LENGTH_SHORT, CuteToast.HAPPY, true).show();
                } else {
                    isDenied = true;
                    break;
                }
            }
            if (isDenied) {
                requestPermissions();
            }
        }
    }

    private void requestPermissions() {
        List<String> permissionsToRequest = new ArrayList<>();

        // Kiểm tra quyền truy cập vị trí
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        // Nếu còn quyền chưa được cấp, yêu cầu tất cả trong một lần
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        }
    }


    private void initSpinner() {

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
                spinner_district.setSelection(0, true);
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
                spinner_ward.setSelection(0, true);
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
    }

    private void moveCameraMap(String s) {

        Customer_LoadingDialog dialog_moveCameraMap = new Customer_LoadingDialog(this, "Loading...");
        dialog_moveCameraMap.show();

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
                CuteToast.ct(AddNewAddressActivity.this, "IOException", Toast.LENGTH_SHORT, CuteToast.WARN, true).show();
            }
        }

        dialog_moveCameraMap.dismiss();
    }

    private void updateSpinnerCity(String ward, String district, String city) {
        for (City i : adapterCity.getCityList()) {
            if (i.getName().contains(city)) {
                spinner_city.setSelection(adapterCity.getPosition(i), false);
                adapterDistricts.setDistrictList(Common.districts(context, R.raw.district, i.getCode()));
                adapterDistricts.notifyDataSetChanged();
                break;
            }
        }

        new android.os.Handler().postDelayed(() -> {
            for (District j : adapterDistricts.getDistrictList()) {
                if (j.getName().contains(district)) {
                    spinner_district.setSelection(adapterDistricts.getPosition(j), false);
                    adapterWards.setWardList(Common.wards(context, R.raw.ward, j.getCode()));
                    adapterWards.notifyDataSetChanged();
                    break;
                }
            }

            new android.os.Handler().postDelayed(() -> {
                for (Ward k : adapterWards.getWardList()) {
                    if (k.getName().contains(ward)) {
                        spinner_ward.setSelection(adapterWards.getPosition(k), true);
                        break;
                    }
                }
            }, 300);

        }, 300);
    }

}