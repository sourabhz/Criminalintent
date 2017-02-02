package com.cholo.sourabhzalke.cholo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by sourabhzalke on 02/02/17.
 */

public class CrimeCameraActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new CrimeCameraFragment();
    }

}
