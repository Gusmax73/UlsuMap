package com.isteam.ulsumap.ui.change;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.isteam.ulsumap.MainActivity;
import com.isteam.ulsumap.R;

public class ChangeFragment extends Fragment {

    private OnChangeFragmentInteractionListener mListener;
    private ArrayAdapter<String> adapter_etazh2;
    private Spinner spinner_korp;
    private Spinner spinner_etazh;
    private String[] str_korp;
    private String[][] str_etazh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        str_korp = new String[] {getString(R.string.nameK1), getString(R.string.nameK2), getString(R.string.nameK3), getString(R.string.nameK4)};       //Строки для спиннера корпусов
        str_etazh = new String[][] {{"1", "2", "3", "4", "5", "6", "7"},        //1 корпус
                {"1", "2", "3"},                            //2 корпус
                {"1", "2", "3", "4", "5"},                  //3 корпус
                {"1", "2", "3", "4"}};                      //4 корпус
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change, container, false);

        spinner_korp = (Spinner) root.findViewById(R.id.spinner_change_korp);      //Спиннер корпуса
        spinner_etazh = (Spinner) root.findViewById(R.id.spinner_change_etazh);    //Спиннер этажа
        Button button_claim = (Button) root.findViewById(R.id.button_change_claim);

        ArrayAdapter<String> adapter_korp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, str_korp);
        adapter_korp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_korp.setAdapter(adapter_korp);

        ArrayAdapter<String> adapter_etazh = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, str_etazh[spinner_korp.getSelectedItemPosition()]);
        adapter_etazh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_etazh.setAdapter(adapter_etazh);

        //Подмена списка этажей при изменении корпуса.
        spinner_korp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {
                MainActivity ma = (MainActivity) getActivity();
                int buf;
                adapter_etazh2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, str_etazh[spinner_korp.getSelectedItemPosition()]);
                adapter_etazh2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_etazh.setAdapter(adapter_etazh2);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Нажатие на кнопку: передаем корпус и этаж, и закрывам фрагмент.
        button_claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentKorpEtazh(spinner_korp.getSelectedItemPosition(), spinner_etazh.getSelectedItemPosition());
                getActivity().onBackPressed();
            }
        });


        return root;
    }

    public interface OnChangeFragmentInteractionListener {               //Интерфейс для взаимодействия с Activity
        void onFragmentKorpEtazh(int korp, int etazh);    //Передача номера корпуса и этажа в Activity
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnChangeFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity ma = (MainActivity) getActivity();
        spinner_korp.setSelection(ma.getKorpus());
    }
}