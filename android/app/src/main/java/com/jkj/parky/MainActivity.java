package com.jkj.parky;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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

public class MainActivity extends AppCompatActivity {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    static final String host = "192.168.1.2";
    private static final String url = "jdbc:mysql://"+host+":3306/parkydb";
    private static final String user = "parky";
    private static final String pass = "password";
    private EditText edt_email;
    private EditText edt_pass;
    private String str_email;
    private String str_pass;
    Socket socket;
    final int port = 3306;
    final int timeout = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_email = (EditText)findViewById(R.id.usr);
        edt_pass = (EditText)findViewById(R.id.pwd);

        //SignUp click listner
        Button btn_sgnup = (Button) findViewById(R.id.sgnup);
        btn_sgnup.setOnClickListener(new View.OnClickListener() {

                                          public void onClick(View v) {
                                              MainActivity.this.finish();
                                              startActivity(new Intent(MainActivity.this, SignUp.class));
                                          }

                                      }
        );

        Button btn_lgn = (Button)findViewById(R.id.lgn);
        btn_lgn.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View v) {
                                           str_email = edt_email.getText().toString();
                                           str_pass = edt_pass.getText().toString();

                                           AsyncTaskRunner runner = new AsyncTaskRunner();
                                           runner.execute();
                                       }

                                   }
        );
        //Asyntask function






        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
                String query = "select * from login_tbl where email = \'"+str_email+"\' and password = \'"+str_pass+"\'";
                ResultSet rs = st.executeQuery(query);


                int rowCount = 0;
                while ( rs.next() )
                {
                    // Process the row.
                    rowCount++;
                }

                if(rowCount == 0)
                    result = "N";
                else
                    result = "Y";
                con.close();
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
            if (result == "Y")
            {
                Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
                startActivity(new Intent(MainActivity.this, ChooseLocationActivity.class));
            }
            else if (result == "N")
                Toast.makeText(MainActivity.this,"Invalid Email / Password", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity.this,"Database Error Occured", Toast.LENGTH_SHORT).show();
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



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(1);
                        MainActivity.this.finish();

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    //****************************************************************************************************************************


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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




    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.jkj.parky/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.jkj.parky/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
