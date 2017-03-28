package com.example.maciekBro.cardatabase.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.maciekBro.cardatabase.Car;
import com.example.maciekBro.cardatabase.MotoDatabaseOpenHelper;
import com.example.maciekBro.cardatabase.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by RENT on 2017-03-28.
 */

public class DetailsFragment extends Fragment {

    private static final String CAR_ID_KEY = "car_id_key";

    private MotoDatabaseOpenHelper openHelper;
    private Unbinder unbinder;

    @BindView(R.id.make_and_model)
    TextView makeAndModel;

    @BindView(R.id.detail_image)
    ImageView detailImage;

    @BindView(R.id.year)
    TextView year;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openHelper = new MotoDatabaseOpenHelper(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {     //jak metoda overrideowana nic nie zwraca to należy zostawiać super!!
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        String carId = getArguments().getString(CAR_ID_KEY);
        Car car = openHelper.getCarWithId(carId);
        makeAndModel.setText(car.getMake()+ " " + car.getModel());
        year.setText("Rocznik: " + car.getYear());
        Glide.with(getActivity()).load(car.getImage()).into(detailImage);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @NonNull    //anotacja że nie bedzie nulla
    public static Fragment getInstance(String carId) {       //metoda do tworzenia fragmentu
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putString(CAR_ID_KEY, carId);
        detailsFragment.setArguments(arguments);
        return detailsFragment;
    }
}
