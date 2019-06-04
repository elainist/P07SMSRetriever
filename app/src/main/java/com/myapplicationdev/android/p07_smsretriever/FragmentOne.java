package com.myapplicationdev.android.p07_smsretriever;


import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOne extends Fragment {


    TextView tvFrag1,tvMsg1;
    EditText etText1;
    Button btnRetrieve1;




    public FragmentOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container,false);
        tvFrag1 = view.findViewById(R.id.tvFrag1);
        tvMsg1 = view.findViewById(R.id.tvMsg1);
        btnRetrieve1 = view.findViewById(R.id.btnRetrieve1);
        etText1 = view.findViewById(R.id.etText1);

        btnRetrieve1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse("content://sms");

                String input = etText1.getText().toString();

                String[] reqCols = new String[]{"date", "address", "body", "type"};
                ContentResolver cr = getActivity().getApplicationContext().getContentResolver();

                String filter="address LIKE ? ";
                String[] filterArgs = {"%"+input+"%"};


                Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);
                String smsBody = "";
                if (cursor.moveToFirst()) {
                    do {
                        long dateInMillis = cursor.getLong(0);
                        String date = (String) DateFormat
                                .format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                        String address = cursor.getString(1);
                        String body = cursor.getString(2);
                        String type = cursor.getString(3);
                        if (type.equalsIgnoreCase("1")) {
                            type = "Inbox:";
                        } else {
                            type = "Sent:";
                        }
                        smsBody += type + " " + address + "\n at " + date
                                + "\n\"" + body + "\"\n\n";
                    } while (cursor.moveToNext());
                }
                tvMsg1.setText(smsBody);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}


