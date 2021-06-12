package com.jkj.parky;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



public class ChooseLocationActivity extends AppCompatActivity {


   static final String host = "192.168.1.2";
    private static final String url = "jdbc:mysql://"+host+":3306/parkydb";
    private static final
    String user = "parky";
    private static final String pass = "password";
    String[] loc;
    Socket socket;
    final int port = 3306;
    final int timeout = 5000;
    int i;
    TextView tt;
    public static ChooseLocationActivity instance = null;

    ArrayAdapter<String> adapter;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_choose_location);
        tt = (TextView)this.findViewById(R.id.tv);
        listView = (ListView) findViewById(R.id.mobile_list);



        AsyncTaskRunner runner = new AsyncTaskRunner();
         runner.execute();




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //  String j = Integer.toString(position);
                Intent i = new Intent(getApplicationContext(), LocationViewActivity.class);
                i.putExtra("location", loc[position]);
                startActivity(i);
               // ChooseLocationActivity.this.finish();
                // Toast.makeText(ChooseLocationActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }

        });



        Button btn_logout = (Button) findViewById(R.id.btnlogout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {

                                               ChooseLocationActivity.this.finish();
                                               startActivity(new Intent(ChooseLocationActivity.this, MainActivity.class));


                                           }
                                       }
        );
    }


    @Override
    public void finish() {
        super.finish();
        instance = null;
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
                String query = "select Location from location_list";
                ResultSet rs = st.executeQuery(query);


                int rowcount = 0;
                if (rs.last()) {
                    rowcount = rs.getRow();
                    rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
                }


                loc = new String[rowcount];

                i = 0;
                while ( rs.next() ) {
                 loc[i] = rs.getString("Location");
                    i++;
                }

               result = "Y";

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
            if (result == "Y") {
                adapter = new ArrayAdapter<String>(com.jkj.parky.ChooseLocationActivity.this, R.layout.activity_listview, loc);
                listView.setAdapter(adapter);
            }
            else
            {
                Toast.makeText(ChooseLocationActivity.this,result,Toast.LENGTH_SHORT).show();
                tt.setText("");

            }

//            tt.setText(result);
          //  Toast.makeText(ChooseLocationActivity.this,i,Toast.LENGTH_SHORT).show();

//            if (result == "Y")
//            {
//                Toast.makeText(ChooseLocationActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
//               // startActivity(new Intent(ChooseLocationActivity.this, ChooseLocationActivity.class));
//            }
//            else if (result == "N")
//                Toast.makeText(ChooseLocationActivity.this,"Invalid Email / Password", Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(ChooseLocationActivity.this,"Database Error Occured", Toast.LENGTH_SHORT).show();
            // execution of result of Long time consuming operation
            //  tv.setText(result);
        }



         /* (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */

        @Override
        protected void onPreExecute() {
            // tv.setText("Loading");
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        /*
         *
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


    //Async ends here






    //*******************************************************************************************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_view, menu);
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
