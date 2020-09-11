package com.example.abhikalpana;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudyLog extends AppCompatActivity {

    String id;
    String complete_log = "";
    String log, date, incharge;
    RequestQueue requestQueue;
    String jsonResponse = "";
    String url_get_data = "http://thantrajna.com/AbhiKalpana/get_log.php?id=";
    String url_post_data = "http://thantrajna.com/AbhiKalpana/update_log_post.php";
    TextView tv_studylog;
    EditText et_editlog;
    Button btn_editlog;
    boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_log);

        id = String.valueOf(getIntent().getExtras().getInt("id", 0));
        Log.v("TAG", id);
        requestQueue = Volley.newRequestQueue(this);
        tv_studylog = findViewById(R.id.tv_studylog);
        et_editlog = findViewById(R.id.et_editlog);
        btn_editlog = findViewById(R.id.btn_editlog);
        url_get_data = url_get_data + id;
        Log.v("TAG", url_get_data);

        getData();

        btn_editlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editMode) {
                    complete_log = et_editlog.getText().toString();
                    if(complete_log.isEmpty()) {}
                    else {
//                        url_post_data = "http://thantrajna.com/AbhiKalpana/update_log.php?id=" + id + "&log=" + complete_log;
                        updateData();
                        et_editlog.getText().clear();
                        et_editlog.setVisibility(View.GONE);
                        btn_editlog.setText("Edit last log");
                        getData();
                    }
                    editMode = false;
                    getData();
                }
                else {
                    et_editlog.getText().clear();
                    et_editlog.setVisibility(View.VISIBLE);
                    btn_editlog.setText("Update");
                    editMode = true;
                }
            }
        });

    }

    void getData() {
        try {
            volleyGetData();
            Log.v("TAG", complete_log);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to fetch", Toast.LENGTH_SHORT).show();
            Log.v("TAG", "Failed to fetch");
        }
    }

    void updateData() {
        Log.v("TAG", url_post_data);
        try {
            volleyLogin();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void volleyGetData() throws JSONException
    {
        JsonArrayRequest req = new JsonArrayRequest(url_get_data,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray)
                    {

                        try {
                            jsonResponse = "";
                            complete_log = "";
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                log = jsonObject.getString("log");
                                date = jsonObject.getString("date");
                                incharge = jsonObject.getString("incharge");
                                complete_log = complete_log + date + "\n" + "Teacher: " + incharge + "\n" +
                                        log + "\n\n";
                                Log.v("TAG", complete_log);
                            }
                            tv_studylog.setText(complete_log);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(req);
    }

    public void volleyLogin() throws JSONException
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_post_data,
                new Response.Listener<String>()
                {
                    JSONObject res = null;
                    @Override
                    public void onResponse(String ServerResponse)
                    {
                        try {
                            res = new JSONObject(ServerResponse);

                            if(res.getString("error").length() == 0)
                            {
                                Toast.makeText(getApplicationContext(), "Thank you: "+res.getString("success"), Toast.LENGTH_LONG).show();
                            }

                            else
                            {
                                Toast.makeText(getApplicationContext(), "Sorry!! : "+res.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            Toast.makeText(getApplicationContext(), "Error JSON : ", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        Toast.makeText(getApplicationContext(), "ERROR_2 "+volleyError.toString(), Toast.LENGTH_LONG).show();
                    }



                })

        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", id);
                params.put("log", complete_log);
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}