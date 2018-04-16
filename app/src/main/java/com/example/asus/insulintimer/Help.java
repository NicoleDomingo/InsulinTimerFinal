package com.example.asus.insulintimer;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;

/**
 * Created by Asus on 3/14/2018.
 */

public class Help extends Fragment{

    private String ques[] = {"Select a question", "How can I add my blood sugar reading?", "How can I add an alarm?",
            "How do I manage my schedule?",
            "What is the importance of getting the minimum and maximum blood sugar of a user?",
            "When will the application notify the user?",
            "What is the purpose of the Tips function?", "What does the Report function shows?"};
    private Spinner spin;
    private String item;
    private TextView desc;
    private ArrayAdapter<String> quesadapter;

    public static Help newInstance() {
        Help fragment = new Help();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.help_layout, container, false);
        getActivity().setTitle("Help");

        spin = (Spinner) view.findViewById(R.id.quesspin);
        desc = (TextView) view.findViewById(R.id.answer);
        quesadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ques);
        spin.setAdapter(quesadapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterView.getItemAtPosition(i).toString();
                if (item.equals("How can I add my blood sugar reading?")){
                    desc.setText("The users can add a reading by adding their blood sugar, when did they took it, and what type of insulin did they use. Furthermore, they can also add some comments and notes to each reading. This will then be recorded and will be seen in the Reports tab.");
                }
                else if (item.equals("How can I add an alarm?")){
                    desc.setText("You can add an alarm by pressing the time you want and then choose whether its AM or PM. To successfully add it in your alarm schedules, press add and if you don't want to continue adding it, press cancel.");
                }
                else if (item.equals("Select a question")) {
                    desc.setText("");
                }
                else if (item.equals("How do I manage my schedule?")){
                    desc.setText("You can manage your schedule through adding an alarm and deleting it by sliding it to the right.");
                }
                else if (item.equals("What is the importance of getting the minimum and maximum blood sugar of a user?")){
                    desc.setText("The importance of getting the minimum and maximum blood sugar of the user is to measure how many times the user's blood sugar reading exceed or fail to be in its range. If the user reached 5 times, the application will notify the user and will be advised to see his/her doctor as soon as possible.");
                }
                else if (item.equals("When will the application notify the user?")){
                    desc.setText("The application will notify the user if he/she reached 5 times of exceeding to his/her max blood sugar or falling below his/her min blood sugar.");
                }
                else if (item.equals("What is the purpose of the Tips function?")){
                    desc.setText("The purpose of the tips found in Home is to educate the users about some tips that they can take into account in handling their sugar intake");
                }
                else if (item.equals("What does the Report function shows?")){
                    desc.setText("The report function shows the Monthly and Weekly Blood Sugar Summary which contains all the readings that the user added. This will be filtered through its meal type or when did the user took the reading.");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

}
