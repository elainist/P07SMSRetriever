package com.myapplicationdev.android.p07_smsretriever;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
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
public class FragmentTwo extends Fragment {

    TextView tvFrag2,tvMsg2;
    EditText etText2;
    Button btnRetrieve2,btnEmail;

    String smsBody = "";




    public FragmentTwo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_two, container,false);

        tvFrag2 = view.findViewById(R.id.tvFrag2);
        tvMsg2 = view.findViewById(R.id.tvMsg2);
        btnRetrieve2 = view.findViewById(R.id.btnRetrieve2);
        etText2 = view.findViewById(R.id.etText2);
        btnEmail = view.findViewById(R.id.btnEmail);



        btnRetrieve2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Uri uri = Uri.parse("content://sms");

                String input = etText2.getText().toString();
                String[] splitStr = input.trim().split(" ");

                String[] reqCols = new String[]{"date", "address", "body", "type"};
                ContentResolver cr = getActivity().getApplicationContext().getContentResolver();

                for (int i = 0; i < splitStr.length; i++) {
                    String filter = "body LIKE ? ";
                    String[] filterArgs = {"%" + input + "%"};


                    Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);

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

                }
                tvMsg2.setText(smsBody);
            }
        });



        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent (Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL,new String[]{"elainenkx@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT,"SMS");
                email.putExtra(Intent.EXTRA_TEXT, smsBody);

                email.setType("message/rfc882");
                startActivity(Intent.createChooser(email,"Choose an Email client: "));


            }
        });
        return view;
        }
    }



