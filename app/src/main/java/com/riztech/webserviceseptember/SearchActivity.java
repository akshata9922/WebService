package com.riztech.webserviceseptember;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.riztech.webserviceseptember.model.BaseResponse;
import com.riztech.webserviceseptember.model.Employee;
import com.riztech.webserviceseptember.service.ApiClient;
import com.riztech.webserviceseptember.service.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    EditText edtName, edtAddress, edtPhoneNumber, edtSalary, edtDesignation,edtid;
    Button btnUpdate,btnSearch;
    Employee employee;
    ApiInterface apiInterface;
    BaseResponse baseResponse;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        edtName = findViewById(R.id.edtName);
        edtid = findViewById(R.id.edtid);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtSalary = findViewById(R.id.edtSalary);
        edtDesignation = findViewById(R.id.edtDesignation);

        btnUpdate = findViewById(R.id.btnUpdate);

    }



    public void searchToServer(View view) {
        String id =edtid.getText().toString().trim();
         if( TextUtils.isEmpty(id)){
             Toast.makeText(this,"please enter the id for search",Toast.LENGTH_SHORT).show();
             return;
         }
        Call <Employee> call = apiInterface.searchEmployee(id);
         call.enqueue(new Callback<Employee>() {
             @Override
             public void onResponse(Call<Employee> call, Response<Employee> response) {

                 Employee employee = response.body();
                 if (employee==null){
                     Toast.makeText(getApplicationContext(),"Employee not found",Toast.LENGTH_SHORT).show();
                 }
                 else {
                     edtName.setText(employee.getName());
                     edtAddress.setText(employee.getAddress());
                     edtPhoneNumber.setText(employee.getPhoneNumber());
                     edtSalary.setText(String.valueOf(employee.getSalary()));
                     edtDesignation.setText(employee.getDesignation());
                 }
             }

             @Override
             public void onFailure(Call<Employee> call, Throwable t) {
                 Toast.makeText(getApplicationContext(),"Employee not found",Toast.LENGTH_SHORT).show();
             }
         });

    }


    public void updateToServer(View view) {
        String id =edtid.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String salString = edtSalary.getText().toString().trim();
        String designation = edtDesignation.getText().toString().trim();

        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(address)||TextUtils.isEmpty(phoneNumber)
                ||TextUtils.isEmpty(salString)||TextUtils.isEmpty(designation)){
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long salary = Long.parseLong(salString);

        Employee employee = new Employee(name, address, phoneNumber, salary, designation);
        int id1 = Integer.parseInt(id);
        employee.setId(id1);


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);


        Call <BaseResponse> call = apiInterface.updateEmployee(id1,employee);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Toast.makeText(SearchActivity.this, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(SearchActivity.this, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
