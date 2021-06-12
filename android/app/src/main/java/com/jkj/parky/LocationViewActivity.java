package com.jkj.parky;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

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

public class LocationViewActivity extends AppCompatActivity {

    static final String host = "192.168.1.2";
    private static final String url = "jdbc:mysql://"+host+":3306/parkydb";
    private static final String user = "parky";
    private static final String pass = "password";

    Socket socket;
    final int port = 3306;
    final int timeout = 5000;
    String value;
    private TextView tv_loc_name;
    private TextView tv_loc_free_space;
    private TextView tv_loc_address;
    private  TextView tv;
    private TextView tt;
    int id;
    String add;
    int count;
    int[] arr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_view);
        tv_loc_name = (TextView)this.findViewById(R.id.tv_locname);
        tv_loc_free_space = (TextView)this.findViewById(R.id.tv_free_space);
        tv_loc_address = (TextView)this.findViewById(R.id.tv_address);
        tv = (TextView)this.findViewById(R.id.textView4);
        tt = (TextView)this.findViewById(R.id.tv_fsp_count);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("location");
        }
        tv_loc_name.setText(value);
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();



        Button btn_refresh = (Button) findViewById(R.id.btnrefresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {


                                               finish();
                                               startActivity(getIntent());


                                           }
                                       }
        );

        Button btn_logout = (Button) findViewById(R.id.btnlogout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {


                                               if(ChooseLocationActivity.instance != null) {
                                                   try {
                                                       ChooseLocationActivity.instance.finish();
                                                   } catch (Exception e) {}
                                               }
                                              // ChooseLocationActivity.finish();
                                               LocationViewActivity.this.finish();
                                               startActivity(new Intent(LocationViewActivity.this, MainActivity.class));


                                           }
                                       }
        );

    }



    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String result;

        @Override
        protected String doInBackground(String... params) {


            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), timeout);

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,pass);

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select * from "+value);


                int rowcount = 0;
                if (rs.last()) {
                    rowcount = rs.getRow();
                    rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
                }
                result = Integer.toString(rowcount);
                while(rs.next()) {
                    add = rs.getString("Adress");
                    count = rs.getInt("count");

                }
                arr = new int[count];
                rs = st.executeQuery("select * from "+value);


                String slot;
                while(rs.next()) {
                    for(int i =0;i<count;i++)
                    {
                        slot = "S"+(i+1);
                        arr[i] = rs.getInt(slot);
                    }
                }
          result="Y";
//                while(rs.next()) {
//                    result  = rs.getInt(1) + "\n";
//                    result += rs.getInt(2) + "\n";
//                    result += rs.getInt(3) + "\n";
//                    result += rs.getInt(4) + "\n";
//
//                }

                return result;
                // Do your long operations here and return the result

                //int time = Integer.parseInt(params[0]);
                // Sleeping for given time period

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

                result = "Database Connection Error";
            }
            return result;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {

            if(result != "Y")
            {
              tv_loc_free_space.setText("Location Not Available");
                tv_loc_address.setText("");
                tv.setText("");
            }
            else
            {
                tv.setText("Address");
                tv_loc_address.setText(add);
                String st;
                st = "";
                int fspcount=0;
                for (int i = 0; i < count; i++) {
                    st = st + "S" + (i + 1) + ":" + arr[i] + " ";
                    if(arr[i] == 0)
                        fspcount++;
                }
              //  tv_loc_free_space.setText(st);
                tt.setText(fspcount+"/"+count+" Available");

          }
        }


        @Override
        protected void onPreExecute() {
         //   tv.setText("Loading");
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(String... text) {

            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
    }
}











