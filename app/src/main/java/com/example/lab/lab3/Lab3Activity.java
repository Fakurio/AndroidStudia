package com.example.lab.lab3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lab.R;
import com.example.lab.addToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Optional;


public class Lab3Activity extends AppCompatActivity implements addToolbar, PhoneListAdapterInterface {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private RecyclerView phoneListView;
    private PhoneListAdapter phoneListAdapter;
    private ItemTouchHelper itemTouchHelper;
    private PhoneViewModel phoneViewModel;
    private static final int ADD_NEW_PHONE_REQUEST = 10;
    private static final int UPDATE_PHONE_REQUEST = 20;
    @Override
    public void setToolbar(Toolbar toolbar, Optional<String> title) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title.orElse(null));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setFloatingActionButton() {
        floatingActionButton.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, Lab3PhoneForm.class), ADD_NEW_PHONE_REQUEST);
        });
    }

    public void setItemTouchHelper() {
        ItemTouchHelper.SimpleCallback itemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Phone phone = phoneViewModel.getAllPhones().getValue().get(position);
                phoneViewModel.deletePhone(phone);
            }
        };
        itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(phoneListView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3);

        toolbar = findViewById(R.id.toolbar_lab3);
        setToolbar(toolbar, Optional.of("Phone DB"));

        floatingActionButton = findViewById(R.id.floatingButton);
        setFloatingActionButton();

        phoneListView = findViewById(R.id.phoneList);
        phoneListAdapter = new PhoneListAdapter(new ArrayList<>(), this);
        phoneListView.setAdapter(phoneListAdapter);
        phoneListView.setLayoutManager(new LinearLayoutManager(this));
        phoneViewModel = new ViewModelProvider(this).get(PhoneViewModel.class);
        phoneViewModel.getAllPhones().observe(this, phoneList -> {
            phoneListAdapter.setPhoneList(phoneList);
        });

        setItemTouchHelper();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab3_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.deleteOption) {
           this.phoneViewModel.deleteAllPhones();
           return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_NEW_PHONE_REQUEST && resultCode == RESULT_OK && data != null) {
            Bundle phoneBundle = data.getExtras();
            String manufacturer = phoneBundle.getString("manufacturer");
            String model = phoneBundle.getString("model");
            String version = phoneBundle.getString("version");
            String website = phoneBundle.getString("website");
            Phone newPhone = new Phone(manufacturer, model, version, website);
            this.phoneViewModel.insertNewPhone(newPhone);
        } else if(requestCode == UPDATE_PHONE_REQUEST && resultCode == RESULT_OK && data != null) {
            Bundle phoneBundle = data.getExtras();
            String manufacturer = phoneBundle.getString("manufacturer");
            String model = phoneBundle.getString("model");
            String version = phoneBundle.getString("version");
            String website = phoneBundle.getString("website");
            String id = phoneBundle.getString("ID");
            Phone phoneToUpdate = this.phoneViewModel.getPhoneByID(Long.parseLong(id));
            phoneToUpdate.setManufacturer(manufacturer);
            phoneToUpdate.setModel(model);
            phoneToUpdate.setAndroidVersion(version);
            phoneToUpdate.setWebsite(website);
            this.phoneViewModel.updatePhone(phoneToUpdate);
        }
    }

    @Override
    public void onItemClick(int position) {
        Phone phone = phoneViewModel.getAllPhones().getValue().get(position);
        Bundle phoneBundle = new Bundle();
        phoneBundle.putString("ID", String.valueOf(phone.getId()));
        phoneBundle.putString("manufacturer", phone.getManufacturer());
        phoneBundle.putString("model", phone.getModel());
        phoneBundle.putString("version", phone.getAndroidVersion());
        phoneBundle.putString("website", phone.getWebsite());
        startActivityForResult(new Intent(this, Lab3PhoneForm.class).putExtras(phoneBundle)
                , UPDATE_PHONE_REQUEST);
    }
}