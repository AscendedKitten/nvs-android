package at.spengergasse.nvssoapconsumer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    private static String SOAP_ACTION = "http://localhost:8080/countries/getCountryGrowthComparisonRequest";
    private static String NAMESPACE = "http://localhost:8080/countries";
    private static String METHOD_NAME = "getCountryGrowthComparisonRequest";

    private static String URL = "http://10.0.2.2:8080/ws";

    private Button sendBtn;
    private TextView resultContent;
    private EditText inputCountry1;
    private EditText inputCountry2;
    private EditText inputYears;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendBtn = findViewById(R.id.sendBtn);
        resultContent = findViewById(R.id.resultContent);
        inputCountry1 = findViewById(R.id.inputCountry1);
        inputCountry2 = findViewById(R.id.inputCountry2);
        inputYears = findViewById(R.id.years);

        sendBtn.setOnClickListener((v -> new SoapRequestTask().execute()));
    }

    class SoapRequestTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            SoapObject result = null;
            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                PropertyInfo countryOne = new PropertyInfo();
                countryOne.setName("nameCountry1");
                countryOne.setValue(inputCountry1.getText().toString());
                countryOne.setType(String.class);
                request.addProperty(countryOne);

                PropertyInfo countryTwo = new PropertyInfo();
                countryTwo.setName("nameCountry2");
                countryTwo.setValue(inputCountry2.getText().toString());
                countryTwo.setType(String.class);
                request.addProperty(countryTwo);

                PropertyInfo years = new PropertyInfo();
                years.setName("years");
                years.setValue(inputYears.getText().toString());
                years.setType(Integer.class);
                request.addProperty(years);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.debug = true;

                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    result = (SoapObject) envelope.getResponse();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Cannot access the web service" + e.toString(),
                        Toast.LENGTH_LONG).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Object o) {
            SoapObject result = (SoapObject) o;

            resultContent.setText(String.format("Winner: %s", result.getPrimitivePropertyAsString("name")));

        }
    }
}
