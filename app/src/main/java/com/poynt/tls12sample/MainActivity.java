package com.poynt.tls12sample;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    final ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).build();
                    final TrustManagerFactory tmf;
                    tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    tmf.init((KeyStore) null);


                    final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                            .connectTimeout(10000, TimeUnit.MILLISECONDS)
                            .writeTimeout(10000, TimeUnit.MILLISECONDS)
                            .readTimeout(10000, TimeUnit.MILLISECONDS)
                            .connectionSpecs(Collections.singletonList(spec))
                            .sslSocketFactory(new TLSSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0]);
                    OkHttpClient client = clientBuilder.build();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, "{'message':'hello, world'}");
                    Request request = new Request.Builder()
                            .url("https://httpbin.org/post")
                            .post(body)
                            .build();

                    Response response  = client.newCall(request).execute();
                    String responseString = response.body().string();
                    System.out.println(responseString);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();





    }
}
