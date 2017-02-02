package com.cholo.sourabhzalke.cholo;

import android.annotation.TargetApi;
import android.graphics.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by sourabhzalke on 02/02/17.
 */

public class CrimeCameraFragment extends Fragment {
     private static final String TAG="CrimeCameraFragment";

     private Camera mCamera;
    private SurfaceView mSurfaceView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_crime_camera,parent,false);

        Button takePictureButton = (Button)v.findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getActivity().finish();
            }
        });
        mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
        return v;
    }
    @TargetApi(19)
    public void onResume(){
        super.onResume();
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
           mCamera = Camera.open(0);
       }
        else{
           mCamera = Camera.open();
       }

    }
}
