package com.example.expense.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expense.Model.UserModel;
import com.example.expense.R;
import com.example.expense.Tools.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText etUsername, etPassword, etEmail, etPhone, etAddress;
    Button btnRegister;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Liên kết các view từ XML
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnRegister = findViewById(R.id.btnRegister);

        // Khởi tạo DBHelper
        dbHelper = new DBHelper(this);

        // Xử lý sự kiện khi nhấn nút đăng ký
        btnRegister.setOnClickListener(this::onClick);
    }

    private void onClick(View v) {
        // Lấy dữ liệu từ form
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // Xác thực dữ liệu
        if (!validateInput(username, password, email, phone, address)) {
            return;
        }

        // Kiểm tra username hoặc email đã tồn tại
        int existStatus = dbHelper.checkUserOrEmailExist(username, email);

        if (existStatus == 1) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            return;
        } else if (existStatus == 2) {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
            return;
        } else if (existStatus == 3) {
            Toast.makeText(this, "Username and Email already exist", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng UserModel với dữ liệu từ form
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);

        // Lấy ngày giờ hiện tại cho created_at và updated_at
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        user.setCreated_at(currentDate);
        user.setUpdated_at(currentDate);

        // Lưu dữ liệu vào cơ sở dữ liệu
        if (dbHelper.registerUser(user)) {
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            // Chuyển sang màn hình LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration Failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm xác thực dữ liệu đầu vào
    private boolean validateInput(String username, String password, String email, String phone, String address) {
        if (username.isEmpty()) {
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.isEmpty()) {
            Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!phone.matches("\\d{10}")) {
            Toast.makeText(this, "Phone number must be 10 digits", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (address.isEmpty()) {
            Toast.makeText(this, "Address is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Kiểm tra email hợp lệ
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }
}
