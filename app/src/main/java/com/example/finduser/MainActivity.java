package com.example.finduser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private userAdapter yourRecyclerViewAdapter;
    private ArrayList<User> userList= new ArrayList<>();
    private  RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        yourRecyclerViewAdapter = new userAdapter(this, userList);
        recyclerView.setAdapter(yourRecyclerViewAdapter);
        requestQueue = Volley.newRequestQueue(this);

        fetchData();
    }
        private void fetchData () {
            String url = "https://jsonplaceholder.typicode.com/users";
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            userList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject userObject = response.getJSONObject(i);
                                    User user = new User(
                                            userObject.getString("name"),
                                            userObject.getString("email"),
                                            userObject.getString("phone")
                                    );
                                    userList.add(user);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            yourRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            requestQueue.add(request);
        }
        @Override
       public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
            MenuItem searchItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    yourRecyclerViewAdapter.getFilter().filter(newText);
                    return true;
                }
            });
            return true;
        }
    }



