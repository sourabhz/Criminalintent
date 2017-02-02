package com.cholo.sourabhzalke.cholo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by sourabhzalke on 03/01/17.
 */

public class CrimeListFragment extends ListFragment {

    //Getting Result with Fragments
    private static final int REQUEST_CRIME = 1;
    private ArrayList<Crime> mCrimes;
    private boolean mSubtitleVisible;
    private static final String TAG ="CrimeListFragment";
    @Override
    public void onCreate(Bundle saveInstancState){
        super.onCreate(saveInstancState);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        mSubtitleVisible = false;

        getActivity().setTitle(R.string.crime_title);
        mCrimes=CrimeLab.get(getActivity()).getCrimes();
         CrimeAdapter adapter= new CrimeAdapter(mCrimes);
        setListAdapter(adapter);
    }

    //Reloading the list in onResume()
    @Override
    public void onResume(){
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
    }
    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime= new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(),CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getId());
                startActivityForResult(i,0);
                return true;
            case R.id.menu_item_show_subtitle:
                if(getActivity().getActionBar().getSubtitle()==null) {
                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    mSubtitleVisible=true;
                    item.setTitle(R.string.hide_subtitle);
                }
                else{
                    getActivity().getActionBar().setSubtitle(null);
                    mSubtitleVisible=false;
                    item.setTitle(R.string.show_subtitle);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onListItemClick(ListView l, View v, int position,long id)
    {
        Crime c=((CrimeAdapter)getListAdapter()).getItem(position);

        //start Activity
     //   Intent i = new Intent(getActivity(),CrimeActivity.class);
        // Start CrimePager Activity with this crime
        Intent i = new  Intent(getActivity(),CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID,c.getId());
        startActivityForResult(i,REQUEST_CRIME);
    }
    private class CrimeAdapter extends ArrayAdapter<Crime> {
        public CrimeAdapter(ArrayList<Crime> crimes){
            super(getActivity(),0,crimes);
        }
        @Override
        public View getView(int position,View convertView,ViewGroup parent)
        {
            //If we weren't given a view , inflate me
            if(convertView== null){
                convertView= getActivity().getLayoutInflater().inflate(R.layout.list_item_crime,null);
            }
            //Configure the view for this Crime
            Crime c= getItem(position);

            TextView titleTextView=
                    (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());

            TextView dateTextView =
                    (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c.getDate().toString());
            CheckBox solvedCheckBox =
                    (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());

            return convertView;
        }
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
        View v= super.onCreateView(inflater,parent,savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if(mSubtitleVisible){
                getActivity().getActionBar().setSubtitle(R.string.subtitle);

            }

        }
        /*********************/
        ListView listView=(ListView)v.findViewById(android.R.id.list);

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB){
            //Use floating context menus on Froyo and Gingerbread
            registerForContextMenu(listView);}
        else{
            //Use contextual action bar on Honeycomb and higher
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            //++++++++//
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater= mode.getMenuInflater();
                    inflater.inflate(R.menu.crime_list_item,menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }
                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter=(CrimeAdapter)getListAdapter();
                            CrimeLab crimeLab = CrimeLab.get(getActivity());
                            for(int i=adapter.getCount()-1;i>=0;i--){
                                if(getListView().isItemChecked(i)){
                                    crimeLab.deleteCrime(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
        return v;
    }
    @Override
    public void onActivityResult (int requestCode,int resultCode,Intent data){
        if(requestCode== REQUEST_CRIME){
            //Handle result
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo){
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item,menu);
    }
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position= info.position;
        CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
        Crime crime = adapter.getItem(position);

        switch(item.getItemId())
        {
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crime);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

}
