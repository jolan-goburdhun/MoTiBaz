package jolan.example.com.navigationdrawer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class advertiseService1 extends AppCompatActivity implements View.OnClickListener{

    private EditText serviceName;
    private EditText serviceDesc;
    private Button next;
    private Spinner dropdown;

    public static String Name;
    public static String Description;
    public static String Category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise_service1);

        serviceName = (EditText) findViewById(R.id.serviceNameTxt);
        serviceDesc = (EditText) findViewById(R.id.serviceDescTxt);

        dropdown = (Spinner) findViewById(R.id.spinner);
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);

        String[] items = new String[]{"Appliances", "Arts",  "Baby Goods/Kids Goods", "Bar", "Bookshop", "Building Materials", "Cars and Parts",
                "Cinema", "Clothing", "Commercial Equipment", "Computers", "Concert Venue", "Electronics",  "Food/Groceries", "Furniture",
                "Games/Toys", "Home Decor", "Hotel", "Jewellery Watches", "Nightlife",  "Outdoor Gear/Sporting Goods", "Pet Services",
                "Phone/Tablet", "Public Figure", "Restaurant/Cafe", "Shopping/Retail", "Sports/Recreation/Activities", "Travel/Leisure", "Wine/Spirits"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    public void next(){
        String name = serviceName.getText().toString().trim();
        String desc = serviceDesc.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter a name for the service", Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(desc)) {
            Toast.makeText(this, "Please enter a description for the service", Toast.LENGTH_LONG).show();
        }else {
            startActivity(new Intent(this, advertiseService2.class));
            Name = serviceName.getText().toString().trim();
            Description = serviceDesc.getText().toString().trim();
            Category = dropdown.getSelectedItem().toString();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == next){
            next();
        }
    }
}
