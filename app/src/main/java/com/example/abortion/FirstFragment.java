package com.example.abortion;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.abortion.databinding.FragmentFirstBinding;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.abortion.BuildConfig.API_KEY;
import androidx.viewpager.widget.ViewPager;

public class FirstFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    ImageView mDogImageView;
    TextView textView;
    TextView textView2;
    TextView textView3;
    Button nextDogButton;
    Button abortionButton;
    Switch darkView;
    Spinner spinner_languages;
    private String selectedState = "";

    private FragmentFirstBinding binding;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int apiResponseCounter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        spinner_languages = view.findViewById(R.id.spinner_languages);
        textView = view.findViewById(R.id.textView);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView2);

        Button abortionButton = view.findViewById(R.id.abortionButton);

        View.OnClickListener myClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedState = spinner_languages.getSelectedItem().toString();

                // Call function 1
                loadAbortionInfo2();

                // Call function 2
                loadAbortionInfo();

                loadAbortionInfo3();
            }
        };

        abortionButton.setOnClickListener(myClickListener);
        Spinner spinnerLanguages = view.findViewById(R.id.spinner_languages);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerLanguages.setAdapter(adapter);
        spinnerLanguages.setOnItemSelectedListener(this);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        View rootView = getView();
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        View rootView = requireActivity().getWindow().getDecorView().getRootView();

        // Check if the current background color matches the desired color
        Drawable background = rootView.getBackground();
        int currentColor = 0;
        if (background instanceof ColorDrawable) {
            currentColor = ((ColorDrawable) background).getColor();
        }
        selectedState = adapterView.getItemAtPosition(i).toString();
        if (spinner_languages != null && spinner_languages.getSelectedView() != null) {
            if (currentColor == Color.parseColor("#e9e0c9")) {
                ((TextView) spinner_languages.getSelectedView()).setTextColor(Color.parseColor("#000000")); // Set the text color of the Spinner

            } else if (currentColor == Color.parseColor("#414a4c")) {
                ((TextView) spinner_languages.getSelectedView()).setTextColor(Color.parseColor("#FFFFFF")); // Set the text color of the Spinner
            } else {
                ((TextView) spinner_languages.getSelectedView()).setTextColor(Color.parseColor("#FFFFFF")); // Set the text color of the Spinner

            }
        }
        }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void loadAbortionInfo2() {
        String myApiKey = API_KEY;
        // getting a new volley request queue for making new requests
        RequestQueue volleyQueue = Volley.newRequestQueue(requireActivity());
        // url of the api through which we get abortion information
        String url2 = "https://api.abortionpolicyapi.com/v1/insurance_coverage/states/"+ selectedState + "/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                // we are using GET HTTP request method
                Request.Method.GET,
                // url we want to send the HTTP request to
                url2,
                null,

                response -> {
                    // handle the response
                    try {
                        JSONObject data = response.getJSONObject(selectedState);
                        String info = data.getString("private_coverage_no_restrictions");
                        String info2 = data.getString("exchange_exception_life");
                        String info3 = data.getString("exchange_exception_rape_or_incest");
                        // update the TextView with the retrieved information
                        textView2.setText("Insurance exchange exception for life: " + info2 + "," + " \nInsurance exchange exception for life:" + info + " \nInsurance exchange exception for rape or \nincest:" + info3);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> {
                    // handle the error
                    Toast.makeText(requireActivity(), "Some error occurred! Cannot fetch abortion information", Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "loadAbortionInfo error: " + error.getLocalizedMessage());
                }
        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Add the API key to the request headers
                Map<String, String> headers = new HashMap<>();
                headers.put("token", myApiKey);
                return headers;
            }
        };

        // add the json request object created above
        // to the Volley request queue
        volleyQueue.add(jsonObjectRequest);
    }
    private void loadAbortionInfo3() {
        String myApiKey = API_KEY;
        // getting a new volley request queue for making new requests
        RequestQueue volleyQueue = Volley.newRequestQueue(requireActivity());
        // url of the api through which we get abortion information
        String url2 = "https://api.abortionpolicyapi.com/v1/waiting_periods/states/"+ selectedState + "/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                // we are using GET HTTP request method
                Request.Method.GET,
                // url we want to send the HTTP request to
                url2,
                null,

                response -> {
                    // handle the response
                    try {
                        JSONObject data = response.getJSONObject(selectedState);
                        String info = data.getString("waiting_period_hours");
                        String info2 = data.getString("counseling_visits");
                        String info3 = data.getString("counseling_waived_condition");
                        // update the TextView with the retrieved information
                        textView3.setText("Waiting period hours: " + info2 + "," + " \nNumber of counseling visits required before abortion allowed:" + info + " \nCounseling can be waived based on condition:" + info3);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> {
                    // handle the error
                    Toast.makeText(requireActivity(), "Some error occurred! Cannot fetch abortion information", Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "loadAbortionInfo error: " + error.getLocalizedMessage());
                }
        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Add the API key to the request headers
                Map<String, String> headers = new HashMap<>();
                headers.put("token", myApiKey);
                return headers;
            }
        };

        // add the json request object created above
        // to the Volley request queue
        volleyQueue.add(jsonObjectRequest);
    }
    private void loadAbortionInfo() {
        String myApiKey = API_KEY;
        // getting a new volley request queue for making new requests
        RequestQueue volleyQueue = Volley.newRequestQueue(requireActivity());
        // url of the api through which we get abortion information
        String url = "https://api.abortionpolicyapi.com/v1/gestational_limits/states/"+ selectedState + "/";
        String url2 = "https://api.abortionpolicyapi.com/v1/insurance_coverage/states/"+ selectedState + "/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                // we are using GET HTTP request method
                Request.Method.GET,
                // url we want to send the HTTP request to
                url,
                null,

                response -> {
                    // handle the response
                    try {
                        JSONObject data = response.getJSONObject(selectedState);
                        String info = data.getString("banned_after_weeks_since_LMP");
                        String info2 = data.getString("exception_life");
                        String info3 = data.getString("exception_health");
                        // update the TextView with the retrieved information
                        textView.setText("Exception for life: "+ info2 +"," + " \nBanned after weeks since LMP:"+ info+" \nHealth exception:" + info3);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> {
                    // handle the error
                    Toast.makeText(requireActivity(), "Some error occurred! Cannot fetch abortion information", Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "loadAbortionInfo error: " + error.getLocalizedMessage());
                }
        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Add the API key to the request headers
                Map<String, String> headers = new HashMap<>();
                headers.put("token", myApiKey);
                return headers;
            }
        };

        // add the json request object created above
        // to the Volley request queue
        volleyQueue.add(jsonObjectRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}