package com.cholo.sourabhzalke.cholo;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Exchanger;

/**
 * Created by sourabhzalke on 03/01/17.
 */

public class CrimeLab {

    private static final String TAG="CrimeLab";
    private static final String FILENAME = "crimes.json";


    private ArrayList<Crime> mCrimes;
    private CriminalIntentJSONSerializer mSerializer;    //json

    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    private CrimeLab(Context appContext)
    {
        mAppContext=appContext;

        mSerializer= new CriminalIntentJSONSerializer(mAppContext,FILENAME);
        //mCrimes= new ArrayList<Crime>();
        try{
            mCrimes= mSerializer.loadCrimes();
        }catch (Exception e){
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG,"Error loading crimes: ",e);
        }//json
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    public static CrimeLab get(Context c)
    {
        if(sCrimeLab==null) //if instance is not present
        {
            sCrimeLab= new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;  //if instance is present simply return the instance
    }

    public ArrayList<Crime> getCrimes(){
        return mCrimes;
    }
    public Crime getCrime(UUID id){
        for(Crime c: mCrimes){
            if(c.getId().equals(id))
                return c;
        }
        return null;
    }
    public boolean saveCrimes(){
        try{
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG,"crimes saved to file");
            return true;
        }
        catch(Exception e){
            Log.e(TAG,"Error saving crimes: ",e);
            return false;
        }
    }
    public void deleteCrime(Crime c){
        mCrimes.remove(c);
    }
}
