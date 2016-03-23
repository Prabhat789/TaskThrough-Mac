package com.pktworld.taskthrough.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pktworld.taskthrough.R;
import com.pktworld.taskthrough.utils.CallService;
import com.pktworld.taskthrough.utils.Globals;
import com.pktworld.taskthrough.utils.Utils;

import org.apache.http.Header;

public class LoginActivity extends AppCompatActivity implements OnMapReadyCallback ,View.OnClickListener{

    private static final String TAG = LoginActivity.class.getSimpleName();
    private GoogleMap mMap;
    private Button btnLogin,btnSocialLogin;
    private EditText editUsername, editPassword;
    private ProgressDialog mProgressDialog;
    private int doneId;
    private Globals glo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Utils.checkLocationStatus(this);

        glo = new Globals(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLogin  = (Button)findViewById(R.id.btnLogin);
        btnSocialLogin  = (Button)findViewById(R.id.btnSocial);
        editUsername = (EditText)findViewById(R.id.editUsername);
        editPassword = (EditText)findViewById(R.id.editPassword);

        btnLogin.setOnClickListener(this);
        btnSocialLogin.setOnClickListener(this);
        try{
            editPassword.setText(glo.getUserPassword());
            editUsername.setText(glo.getUserName());
        }catch (Exception e){
            e.printStackTrace();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setBuildingsEnabled(true);
        LatLng sydney = new LatLng(Double.parseDouble(glo.getLatitude()),Double.parseDouble(glo.getLongitude()));
        mMap.addMarker(new MarkerOptions().position(sydney).
                title(Utils.getAddress(Double.parseDouble(glo.getLatitude()), Double.parseDouble(glo.getLongitude()), LoginActivity.this)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, doneId, 0, getString(R.string.action_settings))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == doneId) {
            Intent i = new Intent(LoginActivity.this,SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin){
            if (validate()){
                if (Utils.isConnected(LoginActivity.this)){
                    login(LoginActivity.this,editUsername.getText().toString(),editPassword.getText().toString());
                }
            }

        }else if (v == btnSocialLogin){
            Toast.makeText(LoginActivity.this,"Work in progress",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate(){
        if (editUsername.getText().toString().trim().length() == 0 ){
            editUsername.setError(getResources().getString(R.string.invalid_username));
            return false;
        }else if (editPassword.getText().toString().trim().length() == 0){
            editPassword.setError(getResources().getString(R.string.invalid_password));
            return false;
        }else{
            return true;
        }

    }

    void login(final Context mContext, final String username, final String password){
        final RequestParams requestParams = new RequestParams();
        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
        final String Url = "http://"+glo.getRemoteUrl()+"/webservice.asmx/LoginValidation?"
                + "UserName="+username
                + "&Password="+password;
        System.out.println("URL "+Url);
        CallService.get(Url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // TODO Auto-generated method stub
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                System.out.println("DataResponse: Success");
                String response = CallService.getResponse(TAG, "sendDataToWebsite - success", Url, requestParams, responseBody, headers, statusCode, null);
                try {
                    if (statusCode == 200){

                        glo.setUserName(username);
                        glo.setUserPassword(password);
                        Intent i = new Intent(mContext,HomeActivity.class);
                        startActivity(i);
                    }


                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                // TODO Auto-generated method stub
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                System.out.println("DataResponse: Fail");
                String response = CallService.getResponse(TAG, "sendDataToWebsite - fails", Url, requestParams, responseBody, headers, statusCode, e);
                System.out.println("DataResponse: " + response);
            }
        });
    }

    /*private class LoginWebServiceTask extends AsyncTask<Void, Void, Document> {

        private String userName = null;
        private String password = null;
        Context mContext;

        LoginWebServiceTask(Context context,String username, String password){
            this.mContext = context;
            this.userName = username;
            this.password = password;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(mContext, "",
                    getResources().getString(R.string.processing), true);
            mProgressDialog.show();
        }

        @Override
        protected Document doInBackground(Void... params) {
            try {
                String remoteURL = glo.getRemoteUrl();
                String url = "http://" + remoteURL + "/webservice.asmx/LoginValidation?UserName="
                        + URLEncoder.encode(userName, "UTF-8")
                        + "&Password="
                        + URLEncoder.encode(password, "UTF-8");
                URI URL = URI.create(url);
                Log.i("Webservice", URL.toString());
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response;

                response = httpclient.execute(new HttpGet(URL));

                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();

                    String responseString = out.toString();
                    Log.i("Webservice",responseString);
                    Document doc = XMLfromString(responseString);
                    return doc;
                } else {
                    // Closes the connection.
                    response.getEntity().getContent().close();
                }
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Document doc) {
            progressDialog.dismiss();
            if (doc == null) {
                showAlert("Network error occured please try again later");
            } else {
                doc.getDocumentElement().normalize();
                Element root = doc.getDocumentElement();
                NodeList nodeList = root.getElementsByTagName("Authentication");
                if (nodeList.item(0).getFirstChild().getNodeValue()
                        .equalsIgnoreCase("true")) {
                    NodeList staffList = root.getElementsByTagName("StaffId");
                    TaskThroughApp.getInstance().setStaffId(staffList.item(0).getFirstChild().getNodeValue());

                    new PushNotifTask().execute();

                } else {
                    showAlert("Authentication Failed");
                }

            }
        }

    }*/
}