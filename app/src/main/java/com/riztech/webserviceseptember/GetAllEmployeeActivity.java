package com.riztech.webserviceseptember;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.riztech.webserviceseptember.Adapter.EmployeeClickListener;
import com.riztech.webserviceseptember.Adapter.EmployeeListAdapter;
import com.riztech.webserviceseptember.model.BaseResponse;
import com.riztech.webserviceseptember.model.Employee;
import com.riztech.webserviceseptember.service.ApiClient;
import com.riztech.webserviceseptember.service.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAllEmployeeActivity extends AppCompatActivity implements EmployeeClickListener {
    RecyclerView rvEmployee;
    EmployeeListAdapter employeeListAdapter;
    ProgressBar progressbar;
    //declare
    ApiInterface apiInterface;

    //Declare
    List<Employee> employees;

    public static final String DATA="data";
    public static final int REQUEST_UPDATE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_employee);
        rvEmployee = findViewById(R.id.rvEmployee);
        rvEmployee.setLayoutManager(new LinearLayoutManager(this));
        progressbar = findViewById(R.id.progressbar);

        //initilize
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
      fetchAllEmployee();

    }

    private void fetchAllEmployee() {
        Call<List<Employee>> callEmployee = apiInterface.getAllEmployee();

        progressbar.setVisibility(View.VISIBLE);
        callEmployee.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                employees = response.body();
                employeeListAdapter = new EmployeeListAdapter(employees, GetAllEmployeeActivity.this);
                progressbar.setVisibility(View.GONE);

                rvEmployee.setAdapter(employeeListAdapter);
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                progressbar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onDeleteClick(int position) {
        final Employee employee = employeeListAdapter.getItemAtPosition(position);
        int employeeId = employee.getId();
        Call<BaseResponse> callDelete = apiInterface.deleteEmployee(employeeId);
        progressbar.setVisibility(View.VISIBLE);
        callDelete.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                progressbar.setVisibility(View.GONE);
                BaseResponse baseResponse = response.body();
                employees.remove(employee);
                employeeListAdapter.notifyDataSetChanged();
                Toast.makeText(GetAllEmployeeActivity.this,baseResponse.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Employee employee = employeeListAdapter.getItemAtPosition(position);
        Intent intent=new Intent(this,UpdateEmployeeActivity.class);
        intent.putExtra(DATA,employee);
        startActivityForResult(intent,REQUEST_UPDATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_UPDATE && requestCode ==RESULT_OK)
        {
            fetchAllEmployee();
        }
    }
}
