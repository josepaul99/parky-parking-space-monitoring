package com.jkj.parky;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.content.Intent;
import android.graphics.RadialGradient;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class SignUp extends AppCompatActivity {



    static final String host = "192.168.1.2";
    private static final String url = "jdbc:mysql://"+host+":3306/parkydb";
    private static final String user = "parky";
    private static final String pass = "password";

    private String str_name;
    private String str_email;
    private String str_ph_no;
    private String str_mf;
    private String str_pass;


    Socket socket;
    final int port = 3306;
    final int timeout = 5000;

    Integer bool;
    Integer flag1, flag2, flag3, flag4, flag5, flag6, flag7, flag8, flagg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //Start login activity on login click
        Button btn_lgn = (Button) findViewById(R.id.lgn);
        btn_lgn.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View v) {
                                           SignUp.this.finish();
                                           startActivity(new Intent(SignUp.this, MainActivity.class));
                                       }
                                   }
        );

        final EditText edt_name = (EditText) findViewById(R.id.name);
        final EditText edt_email = (EditText) findViewById(R.id.email);
        final EditText edt_ph_no = (EditText) findViewById(R.id.ph_no);
        final EditText edt_pwd = (EditText) findViewById(R.id.pwd);
        final EditText edt_rpwd = (EditText) findViewById(R.id.rpwd);
        final CheckBox edt_cbtos = (CheckBox) findViewById(R.id.cbtos);
        final RadioGroup edt_rb_mf = (RadioGroup) findViewById(R.id.rb_mf);
        final RadioButton edt_rb_male = (RadioButton) findViewById(R.id.rb_male);
        final RadioButton edt_rb_fmale = (RadioButton) findViewById(R.id.rb_fmale);

        flagg =0;
        edt_rb_mf.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup edt_rb_mf, int checkedId) {
                boolean isChecked = edt_rb_male.isChecked();
                boolean isChecked2 = edt_rb_fmale.isChecked();
                if (isChecked) {
                    edt_rb_fmale.setError(null);
                    flag4 = 1;
                    flagg = 1;
                }
                if (isChecked2) {
                    edt_rb_fmale.setError(null);
                    flag4 = 1;
                    flagg = 2;

                }

            }
        });


        bool = 0;
        edt_cbtos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            //    @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    bool = 0;
                    edt_cbtos.setError("Please accept TOS.");
                    flag5 = 0;
                } else {
                    edt_cbtos.setError(null);
                    bool = 1;
                    flag5 = 1;
                }
            }

        });


        //Sign up and redirect to login activity
        Button btn_sbmt=(Button) findViewById(R.id.sgn_sbmt);
        btn_sbmt.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {


                                            str_name = edt_name.getText().toString();
                                            str_email = edt_email.getText().toString();
                                            str_ph_no = edt_ph_no.getText().toString();
                                            if(flagg == 1)
                                                str_mf = "male";
                                            else
                                                str_mf = "female";
                                            str_pass = edt_pwd.getText().toString();

                                            if (edt_name.getText().toString().length() == 0) {
                                                edt_name.setError("Name is required!");
                                                flag1 = 0;
                                            } else {
                                                flag1 = 1;
                                            }

                                            if (isEmailValid(edt_email.getText().toString())) {
                                                flag2 = 1;
                                            } else {
                                                edt_email.setError("Invalid Email adress!");
                                                flag2 = 0;
                                            }

                                            if (edt_ph_no.getText().toString().length() == 0) {
                                                edt_ph_no.setError("Mobile Number is Required");
                                                flag3 = 0;
                                            } else if (edt_ph_no.getText().toString().length() < 10) {
                                                edt_ph_no.setError("Mobile Number should be 10 digit");
                                                flag3 = 0;
                                            } else {
                                                edt_ph_no.setError(null);
                                                flag3 = 1;
                                            }

                                            if (edt_rb_mf.getCheckedRadioButtonId() == -1) {
                                                edt_rb_fmale.setError("Please select gender");
                                                flag4 = 0;
                                            } else {
                                                flag4 = 1;
                                            }

                                            if (bool == 0) {
                                                edt_cbtos.setError("Please accept TOS.");
                                                flag5 = 0;
                                            } else {
                                                flag5 = 1;
                                            }

                                            if (edt_pwd.getText().toString().length() == 0) {
                                                edt_pwd.setError("Password is Required");
                                                flag6 = 0;
                                            } else {
                                                flag6 = 1;
                                            }
                                            if (edt_rpwd.getText().toString().length() == 0) {
                                                edt_rpwd.setError("Re-type password here");
                                                flag7 = 0;
                                            } else {
                                                flag7 = 1;
                                            }
                                            if (edt_pwd.getText().toString().length() != edt_rpwd.getText().toString().length()) {
                                                edt_rpwd.setError("Passwords doesn't match");
                                                flag8 = 0;
                                            } else {
                                                flag8 = 1;
                                                edt_rpwd.setError(null);
                                            }

                                            /* if (true) {
                                               AsyncTaskRunner runner = new AsyncTaskRunner();
                                                runner.execute();
                                            }*/


                                            if ((flag1 & flag2 & flag3 & flag4 & flag5 & flag6 & flag7 & flag8) == 1) {
                                                AsyncTaskRunner runner = new AsyncTaskRunner();
                                                runner.execute();
                                            }



                   }
               }
            );


    }









    //Async starts here
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String result;

        @Override
        protected String doInBackground(String... params) {

            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), timeout);

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);


                Statement st = con.createStatement();
                String q1 = "select * from login_tbl where email = \'"+str_email+"\'";
                ResultSet rs = st.executeQuery(q1);
                int rowCount = 0;
                while ( rs.next() )
                {
                    rowCount++;
                }

                if(rowCount == 1) {
                    //Email Id already exist
                    result = "a";
                }
                else
                {
                    String q2 = "insert into login_tbl values ('','"+str_email+"','"+str_pass+"');";
                    Integer in = st.executeUpdate(q2);
                    String q3 = "insert into login_tbl_detail values ('','"+str_name+"','"+str_email+"','"+str_pass+"','"+str_mf+"','"+str_ph_no+"');";
                    Integer in2 = st.executeUpdate(q3);
                    con.close();
                    if(in == 1 && in2 == 1)
                        //Registration Successfull
                        result = "b";
                    else
                        //Registration Unsuccessfull, Unknown Error
                        result = "c";
                }

                return result;
                // Do your long operations here and return the result

            }
            catch (UnknownHostException uhe) {
                result="Unable to resolve host, Check Internet connectivity";
            }
            catch (SocketTimeoutException ste) {
                result="Server is down";
            }
            catch (IOException ioe) {
                result="Sudden disconnection,please restart!";
            }
            catch (Exception e) {

                result = e.toString();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == "a")
            {
                Toast.makeText(SignUp.this,"Email ID already exist.Please Login!",Toast.LENGTH_SHORT).show();
                SignUp.this.finish();
                startActivity(new Intent(SignUp.this, MainActivity.class));
            }
            else if(result == "b")
            {
                Toast.makeText(SignUp.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                SignUp.this.finish();
                startActivity(new Intent(SignUp.this, MainActivity.class));
            }
            else if(result == "c")
            {

                Toast.makeText(SignUp.this,"Registration Unsuccessful, Unknown Error",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(SignUp.this,"Exception ocurred: "+result,Toast.LENGTH_SHORT).show();
            }



        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }


    //Async ends here












    //Email validation Fn Start
    public boolean isEmailValid(String email)
    {

        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
//Email validation Fn Ends












//****************************************************************************************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
