/* In this activity the user enters detail for a car they wish to see a predicted price for.
 * Author: Sean Coll
 * Date Created: 23/12/21
 * Last Modified: 03/1/22
 */
package ie.tudublin.carml;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PricePredictionActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton back_arrow;
    ImageButton submit;
    Spinner manufacturerDD;
    Spinner modelDD;
    Spinner yearDD;
    // Create ArrayLists that hold Manufacturer and Model
    private final List<String> manufacturers = new ArrayList<>();
    private final List<String> models = new ArrayList<>();
    private final List<String> years = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_prediction_activity);

        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);
        submit = findViewById(R.id.submit_button);
        submit.setOnClickListener(this);
        prepSpinners();
        manufacturerDD = findViewById(R.id.manufacturer);
        modelDD = findViewById(R.id.model);
        yearDD = findViewById(R.id.year);

        ArrayAdapter<String> manAdapter =
                new ArrayAdapter<>(this, R.layout.spinner_item, manufacturers);
        manAdapter.setDropDownViewResource(R.layout.spinner_item);
        manufacturerDD.setAdapter(manAdapter);

        ArrayAdapter<String> modAdapter =
                new ArrayAdapter<>(this, R.layout.spinner_item, models);
        modAdapter.setDropDownViewResource(R.layout.spinner_item);
        modelDD.setAdapter(modAdapter);

        ArrayAdapter<String> yearAdapter =
                new ArrayAdapter<>(this, R.layout.spinner_item, years);
        modAdapter.setDropDownViewResource(R.layout.spinner_item);
        yearDD.setAdapter(yearAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        submit.setAlpha(1F);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case(R.id.back_arrow): {
                finish();
                break;
            }
            case(R.id.submit_button): {
                submit.setAlpha(0.5F);
                submit.setOnClickListener(null);
                String user_car =   manufacturerDD.getSelectedItem() + "," +
                                    modelDD.getSelectedItem() + "," +
                                    yearDD.getSelectedItem();
                Intent result = new Intent(PricePredictionActivity.this, ResultActivity.class);
                result.putExtra("user_car", user_car);
                startActivity(result);
                break;
            }
            default: {
                break;
            }
        }
    }

    public void prepSpinners() {
        // Add in the first value for the dropdown menus
        manufacturers.add("Select One");
        models.add("Select One");

        DatabaseAccess DBA = new DatabaseAccess();

        String manufacturersData = DBA.runThread("manufacturers", "Acura,CL,2001");

        // Add in values for years
        for (int i = 1990; i <= 2017; i++) {
            years.add(String.valueOf(i));
        }

        // Get InputStream for data
        InputStream inStream = getResources().openRawResource(R.raw.descriptive_data);
        // Create Buffered Reader for stream
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inStream, StandardCharsets.UTF_8));

        // i is used to check if the previous item in the list is the same as the current
        int ma = 1;
        int mo = 1;
        String row;
        // Read each row and parse the correct parts into the correct lists
        try {
            // Skip over the column headers
            reader.readLine();
            while((row = reader.readLine()) != null)
            {
                // Split each row on the commas
                String[] splits = row.split(",");

                if (ma > 0) {
                    if (!manufacturers.get(ma-1).equals(splits[0])) {
                        manufacturers.add(splits[0]);
                        ma++;
                    }
                }

                if (mo > 0) {
                    if (!models.get(mo-1).equals(splits[1])) {
                        models.add(splits[1]);
                        mo++;
                    }
                }
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
