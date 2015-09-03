package com.todor.appslist.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.todor.appslist.ListAdapter;
import com.todor.appslist.R;
import com.todor.appslist.ServiceHandler;
import com.todor.appslist.model.SingleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressDialog progressDialog;
    private static String url = "http://partners.mithrilads.com/ljson.php";
    private static final String TAG_APPS = "android";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "name";
    private static final String TAG_DESCRIPTION = "desc";
    private static final String TAG_URL = "url";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_PROXY = "proxy";
    private static final String TAG_PACKAGE = "package";

    JSONArray appsJson;
    List<SingleItem> appsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        appsList = new ArrayList<>();
        new GetApp().execute();
    }

    private class GetApp extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandler serviceHandler = new ServiceHandler();
            String jsonStr = serviceHandler.makeServiceCall(url, ServiceHandler.GET);

            if(jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    appsJson = jsonObject.getJSONArray(TAG_APPS);

                    for(int i = 0; i < appsJson.length(); i++) {
                        JSONObject app = appsJson.getJSONObject(i);

                        String id = app.getString(TAG_ID);
                        String name = app.getString(TAG_TITLE);
                        String desc = app.getString(TAG_DESCRIPTION);
                        String url = app.getString(TAG_URL);
                        String image = app.getString(TAG_IMAGE);
                        String proxy = app.getString(TAG_PROXY);
                        String appPackage = app.getString(TAG_PACKAGE);

                        SingleItem item = new SingleItem();
                        item.setId(id);
                        item.setTitle(name);
                        item.setDescription(desc);
                        item.setURI(url);
                        item.setImage(image);
                        item.setProxy(proxy);
                        item.setAppPackage(appPackage);
                        appsList.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog.isShowing())
                progressDialog.dismiss();
            ListAdapter adapter = new ListAdapter(MainActivity.this, appsList);
            listView.setAdapter(adapter);
        }
    }
}
