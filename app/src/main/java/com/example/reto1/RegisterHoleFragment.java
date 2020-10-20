package com.example.reto1;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RegisterHoleFragment extends DialogFragment implements View.OnClickListener {

    private TextView holeCoordinateTV;
    private TextView holeAddressTV;
    private Button registerHoleBTN;
    private OnRegisterListener listener;
    //private String location;
    //private String address;

    public RegisterHoleFragment() {
        // Required empty public constructor
    }


    public static RegisterHoleFragment newInstance(String coordinate, String address) {
        RegisterHoleFragment fragment = new RegisterHoleFragment();
        Bundle args = new Bundle();
        args.putString("coordinate", coordinate);
        args.putString("address", address);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register_hole, container, false);
        holeCoordinateTV = root.findViewById(R.id.holeCoordinateTV);
        holeAddressTV = root.findViewById(R.id.holeAddressTV);
        registerHoleBTN = root.findViewById(R.id.registerHoleBTN);
        registerHoleBTN.setOnClickListener(this);

        Bundle args = getArguments();

        holeCoordinateTV.setText(args.getString("coordinate"));
        holeAddressTV.setText(args.getString("address"));
        return root;
    }

    public void setListener(OnRegisterListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerHoleBTN:
                if(listener == null){
                    Log.e("Error", "No hay listener");
                } else {
                    listener.onRegister();
                }
                break;
        }
    }

    public interface OnRegisterListener{
        void onRegister();
    }

    public TextView getHoleCoordinateTV() {
        return holeCoordinateTV;
    }

    public void setHoleCoordinateTV(String coordinate) {
        holeCoordinateTV.setText(coordinate);
    }

    public TextView getHoleAddressTV() {
        return holeAddressTV;
    }

    public void setHoleAddressTV(String address) {
        holeAddressTV.setText(address);
    }
}