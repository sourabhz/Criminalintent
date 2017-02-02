package com.cholo.sourabhzalke.cholo;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    public static final String EXTRA_CRIME_ID="com.bignerdranch.android.criminalintent.crime_id";
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    private void updateDate(){
        mDateButton.setText(mCrime.getDate().toString());
    }

    private static final String DIALOG_DATE="date";
    private static final int REQUEST_DATE=0;

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_DATE) {
            Date date =(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            //mDateButton.setText(mCrime.getDate().toString());
           updateDate();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {super.onCreate(savedInstanceState);
        UUID crimeId= (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime= CrimeLab.get(getActivity()).getCrime(crimeId);
        setHasOptionsMenu(true);
    }

    //creating fragment arguements
    public static CrimeFragment newInstance(UUID crimeID){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID,crimeID);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);    // method for attaching arguements
        return fragment;
    }
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_crime2,parent,false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if(NavUtils.getParentActivityName(getActivity())!=null)
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        //SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM F, yyyy.");
        //mDateButton.setText(sdf.format(mCrime.getDate()));
       updateDate();
        //mDateButton.setEnabled(false);


        //Now showing DialogFragment
        mDateButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                FragmentManager fm= getActivity()
                        .getSupportFragmentManager();
                DatePickerFragment dialog =  DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(getParentFragment(),REQUEST_DATE);
                dialog.show(fm,DIALOG_DATE);
            }
        });

        mTitleField=(EditText)v.findViewById(R.id.crime_title);

        //puttng the crime data on titlefield
        mTitleField.setText(mCrime.getTitle());

        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSolvedCheckBox=(CheckBox)v.findViewById(R.id.crime_solved);

        //updating the data coming from intent extra on solved check

        mSolvedCheckBox.setChecked(mCrime.isSolved());

        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        return v;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                if(NavUtils.getParentActivityName(getActivity())!=null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onPause(){
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

}
