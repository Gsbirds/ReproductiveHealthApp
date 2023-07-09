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
    Button nextDogButton;
    Button abortionButton;
    Switch darkView;
    Spinner spinner_languages;
    private String selectedState = "";

    private FragmentFirstBinding binding;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        spinner_languages = view.findViewById(R.id.spinner_languages);
        textView = view.findViewById(R.id.textView);
        darkView = view.findViewById(R.id.switch1);
        mDogImageView = view.findViewById(R.id.dogImageView);
        nextDogButton = view.findViewById(R.id.nextDogButton);
        abortionButton = view.findViewById(R.id.abortionButton);
        darkView.setOnClickListener(View -> setDarkView());
        nextDogButton.setOnClickListener(View -> loadDogImage());
        abortionButton.setOnClickListener(View -> loadAbortionInfo());
        // image of a dog will be loaded once at the start of the app
        loadDogImage();

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

    private void setDarkView() {

        View rootView = requireActivity().getWindow().getDecorView().getRootView();

        // Check if the current background color matches the desired color
        Drawable background = rootView.getBackground();
        int currentColor = 0;
        if (background instanceof ColorDrawable) {
            currentColor = ((ColorDrawable) background).getColor();
        }
        if (currentColor == Color.parseColor("#e9e0c9")) {
            rootView.setBackgroundColor(Color.parseColor("#414a4c"));
            textView.setTextColor(Color.parseColor("#e9e0c9"));// Set the original color here
        } else {
            rootView.setBackgroundColor(Color.parseColor("#e9e0c9"));
            textView.setTextColor(Color.parseColor("#414a4c"));// Set the original color here
        }
    }

    private void loadDogImage() {

        // getting a new volley request queue for making new requests
        RequestQueue volleyQueue = Volley.newRequestQueue(requireActivity());
        // url of the api through which we get random dog images
        String url = "https://dog.ceo/api/breeds/image/random";

        // since the response we get from the api is in JSON, we
        // need to use `JsonObjectRequest` for parsing the
        // request response
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                // we are using GET HTTP request method
                Request.Method.GET,
                // url we want to send the HTTP request to
                url,
                // this parameter is used to send a JSON object to the
                // server, since this is not required in our case,
                // we are keeping it `null`
                null,

                // lambda function for handling the case
                // when the HTTP request succeeds
                (Response.Listener<JSONObject>) response -> {
                    // get the image url from the JSON object
                    String dogImageUrl;
                    try {
                        dogImageUrl = response.getString("message");
                        // load the image into the ImageView using Glide.
                        Glide.with(requireActivity()).load(dogImageUrl).into(mDogImageView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                // lambda function for handling the case
                // when the HTTP request fails
                (Response.ErrorListener) error -> {
                    // make a Toast telling the user
                    // that something went wrong
                    Toast.makeText(requireActivity(), "Some error occurred! Cannot fetch dog image", Toast.LENGTH_LONG).show();
                    // log the error message in the error stream
                    Log.e("MainActivity", "loadDogImage error: ${error.localizedMessage}");
                }
        );

        // add the json request object created above
        // to the Volley request queue
        volleyQueue.add(jsonObjectRequest);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedState = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void loadAbortionInfo() {
        String myApiKey = API_KEY;
        // getting a new volley request queue for making new requests
        RequestQueue volleyQueue = Volley.newRequestQueue(requireActivity());
        // url of the api through which we get abortion information
        String url = "https://api.abortionpolicyapi.com/v1/gestational_limits/states/"+ selectedState + "/";

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
                        // update the TextView with the retrieved information
                        textView.setText("Exception for life: "+ info2 +"," + " \nbanned after weeks since LMP:"+ info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> {
                    // handle the error
                    Toast.makeText(requireActivity(), "Some error occurred! Cannot fetch abortion information", Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "loadAbortionInfo error: " + error.getLocalizedMessage());
                }
        ) {
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