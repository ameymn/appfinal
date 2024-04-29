package com.example.jeeevandan;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

        private EditText usernameEditText;
        private EditText passwordEditText;
        private Button loginButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);

            setTitle("Jeevandan");
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int colorCodeDark = Color.parseColor("#5ec567");
            window.setStatusBarColor(colorCodeDark);

            // Find the views by their IDs
            usernameEditText = findViewById(R.id.editTextText);
            passwordEditText = findViewById(R.id.editTextTextPassword);
            loginButton = findViewById(R.id.button);

            // Set a click listener on the login button
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the values from the EditText fields
                    String id = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                    Call<Data> call = apiService.login(id, password);
                    call.enqueue(new Callback<Data>() {
                        @Override
                        public void onResponse(Call<Data> call, Response<Data> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Log.d(TAG, "Response code: " + response.code());
                                Log.d(TAG, "Response body: " + response.body().getName());
                                try {
                                    if (response.body().getSuccess()) {
                                        Toast.makeText(com.example.jeeevandan.MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        // Handle successful login
                                        Intent intent = new Intent(com.example.jeeevandan.MainActivity.this, NewActivity.class);
                                        intent.putExtra("id",response.body().getId());
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(com.example.jeeevandan.MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                        // Handle failed login
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(com.example.jeeevandan.MainActivity.this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(com.example.jeeevandan.MainActivity.this, "Please verify your id and password.", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Response error: " + response.code());
                            }
                        }



                        @Override
                        public void onFailure(Call<Data> call, Throwable t) {
                            Toast.makeText(com.example.jeeevandan.MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            // Handle network error
                        }
                    });

                    Toast.makeText(com.example.jeeevandan.MainActivity.this, "id: " + id + ", Password: " + password, Toast.LENGTH_SHORT).show();
                }
            });

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }