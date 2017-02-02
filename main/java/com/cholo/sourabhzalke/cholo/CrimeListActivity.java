package com.cholo.sourabhzalke.cholo;

import android.support.v4.app.Fragment;

/**
 * Created by sourabhzalke on 03/01/17.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
