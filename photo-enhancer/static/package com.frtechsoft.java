package com.frtechsoft.loncetimer;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class LalToDhaka extends Fragment {
    ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
    HashMap<String,String>hashMap;
    Animation animation;
    InterstitialAd mInterstitialAd;
    RecyclerView recyclerView;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView=inflater.inflate(R.layout.fragment_lal_to_dhaka, container, false);

        recyclerView=MyView.findViewById(R.id.recyClerView);
        progressBar=MyView.findViewById(R.id.progressBar);


        String url="https://rabbifazlay.com/launcetimerapp/dhaka_to_hatia.json";

        JsonArrayRequest jsonArrayRequest =new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);


                for (int x=0;x<response.length();x++){
                    try {
                        JSONObject jsonObject=response.getJSONObject(x);
                        String type=jsonObject.getString("TYPE");
                        JSONObject iteam=jsonObject.getJSONObject("iteam");
                        hashMap=new HashMap<>();
                        hashMap.put("type",type);

                        if (type.equals("lonce")){
                            String date=iteam.getString("date");
                            String lonceName=iteam.getString("lonceName");
                            String time=iteam.getString("time");
                            String num=iteam.getString("num");

                            hashMap.put("date",date);
                            hashMap.put("lonceName",lonceName);
                            hashMap.put("time",time);
                            hashMap.put("num",num);

                        }else {
                            String date=iteam.getString("date");
                            String lonceName=iteam.getString("lonceName");
                            String time=iteam.getString("time");
                            String num=iteam.getString("num");

                            hashMap.put("date",date);
                            hashMap.put("lonceName",lonceName);
                            hashMap.put("time",time);
                            hashMap.put("num",num);
                        }

                        arrayList.add(hashMap);
                        Log.d("RouteIteam", "onResponse: "+hashMap);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
                MyAdapter myAdapter=new MyAdapter();
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.VISIBLE);
                showNetworkAlertDialog();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);






        return MyView;
    }
   private class ADAPTER extends RecyclerView.Adapter{

        int launce=0;
        int nads=1;

       private class LAUNCE extends RecyclerView.ViewHolder{
           TextView lonceName,dateToLeave,timeToLeave;
           AppCompatButton cabinBook;
           MaterialCardView singleIteam;

           public LAUNCE(@NonNull View itemView) {
               super(itemView);
               lonceName=itemView.findViewById(R.id.lonceName);
               dateToLeave=itemView.findViewById(R.id.dateToLeave);
               timeToLeave=itemView.findViewById(R.id.timeToLeave);
               cabinBook=itemView.findViewById(R.id.cabinBook);
               singleIteam=itemView.findViewById(R.id.singleIteam);
           }
       }
       private class NATIVADS extends RecyclerView.ViewHolder{
           TemplateView nativeAdsTem;

           public NATIVADS(@NonNull View itemView) {
               super(itemView);
               nativeAdsTem=itemView.findViewById(R.id.nativeAdsTem);
           }
       }
       @NonNull
       @Override
       public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

           LayoutInflater layoutInflater=getLayoutInflater();
           if (viewType==launce){
               View myView=layoutInflater.inflate(R.layout.iteam_lonce,parent,false);
               return new LAUNCE(myView);
           }else {
               View myView=layoutInflater.inflate(R.layout.nativtemplate,parent,false);
               return  new NATIVADS(myView);

           }

       }

       @Override
       public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
           if (getItemViewType(position)==launce){
               LAUNCE launce1= (LAUNCE) holder;

               //retrive data from hashmap

               hashMap=arrayList.get(position);
               String nameLonce =hashMap.get("lonceName");
               String dateLonce =hashMap.get("date");
               String timeLonce =hashMap.get("time");
               String num =hashMap.get("num");

               //animation iteam

               animation= AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
               launce1.singleIteam.startAnimation(animation);

               //set data that come from hashmap

               DateCheck dateCheck=new DateCheck(getContext());
               String date=dateCheck.date;
               launce1.lonceName.setText(nameLonce);
               launce1.timeToLeave.setText(timeLonce);

               if (dateLonce.contains(date)){
                   launce1.dateToLeave.setText("TODAY");
               }else {
                   launce1.dateToLeave.setText(dateLonce);
               }
               //end set value

               //cabinBook method

               if (nameLonce.contains("মানিক-১১")){
                   launce1.cabinBook.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent intent=new Intent(getContext(), CabinBook.class);
                           intent.putExtra("LONCE_NUM",num);
                           intent.putExtra("LONCE_ROUTE","লালমোহন");
                           intent.putExtra("LONCE_NAME",nameLonce);
                           startActivity(intent);
                       }
                   });

               }else if (nameLonce.contains("শ্রীনগর-৮")) {
                   launce1.cabinBook.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent intent = new Intent(getContext(), CabinBook.class);
                           intent.putExtra("LONCE_NUM", num);
                           intent.putExtra("LONCE_ROUTE", "লালমোহন");
                           intent.putExtra("LONCE_NAME", nameLonce);
                           startActivity(intent);
                       }
                   });
               }else if (nameLonce.contains("শ্রীনগর-৭")) {
                   launce1.cabinBook.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent intent = new Intent(getContext(), CabinBook.class);
                           intent.putExtra("LONCE_NUM", num);
                           intent.putExtra("LONCE_ROUTE", "লালমোহন");
                           intent.putExtra("LONCE_NAME", nameLonce);
                           startActivity(intent);
                       }
                   });
               }else if (nameLonce.contains("প্রিন্স সাকিন")) {
                   launce1.cabinBook.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent intent = new Intent(getContext(), CabinBook.class);
                           intent.putExtra("LONCE_NUM", num);
                           intent.putExtra("LONCE_ROUTE", "লালমোহন");
                           intent.putExtra("LONCE_NAME", nameLonce);
                           startActivity(intent);
                       }
                   });
               }






           }else {
               NATIVADS nativads= (NATIVADS) holder;
               hashMap=arrayList.get(position);

               AdLoader adLoader = new AdLoader.Builder(getContext(), "ca-app-pub-3940256099942544/2247696110")
                       .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                           @Override
                           public void onNativeAdLoaded(NativeAd nativeAd) {

                               adsholder.nativeAds.setNativeAd(nativeAd);
                           }
                       })
                       .build();

               adLoader.loadAd(new AdRequest.Builder().build());
           }

       }



       @Override
       public int getItemCount() {
           return arrayList.size();
       }

       @Override
       public int getItemViewType(int position) {
           hashMap=arrayList.get(position);
           String TYPE=hashMap.get("type");

           if (TYPE.contains("lonce")){
               return launce;
           }else return nads;

       }
   }

   //alert dialoge

    private void showNetworkAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Network Connection")
                .setMessage("Network connection is required. Please turn on WiFi or mobile data.")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Open network settings
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}