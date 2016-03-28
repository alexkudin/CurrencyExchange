package com.example.alex.converter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

class	Currency
{
    // ----- Class members -------------------------------------------------
    public 				String		name;
    public              String      typeCourse;
    public				double		curse1;
    public				double		curse2;
    public				double		curse3;

    // ----- Class methods -------------------------------------------------
    public	Currency(String name)
    {
        this.name	= name;
        //this.price	= price;
    }
    public String toString()
    {
        return this.name ; //+ " " + this.price
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setCur(double course) {
        this.curse1 = course;
    }
    public double getCur() {
        return curse1;
    }
}

public class MainActivity extends AppCompatActivity
{
    private TextView result;
    private EditText course;
    private EditText summaIn;
    private String[] arrTypeCurrency = {"UAH - гривна" , "USD - доллар США" , "EUR - евро" };
    private Spinner from;
    private Spinner to;
    private ArrayAdapter<Currency> adapter;
    private ArrayList<Currency> currencyTypes = new ArrayList<>();
    private HashMap<String , Double> NBU = new HashMap<>();
    private HashMap<String , Double> Pokupka = new HashMap<>();
    private HashMap<String , Double> Prodazha = new HashMap<>();
    private RadioButton nbu;
    private RadioButton pokupka;
    private RadioButton prodazha;
    private RadioGroup radioGroup;
    private double summaToChange;
    private double summaResult;
    private String [] arrayTmp = new String [2];

    private final static String NBU_HASHMAP_KEY         = "nbu";
    private final static String POKUPKA_HASHMAP_KEY     = "pokupka";
    private final static String PRODAZHA_HASHMAP_KEY    = "prodazha";

    /**
     * Метод восстановления состояния активности
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null)
        {
            this.summaIn.setText(savedInstanceState.getString("summainEx"));
            this.course.setText(savedInstanceState.getString("courseKey"));
            this.result.setText(savedInstanceState.getString("result"));
            this.from.getChildAt(savedInstanceState.getInt("spinnerFrom"));
            this.to.getChildAt(savedInstanceState.getInt("spinnerTo"));
            //this.radioGroup.getChildAt(savedInstanceState.getInt("rg"));
            this.nbu.setChecked(savedInstanceState.getBoolean("nbuRB"));
            this.pokupka.setChecked(savedInstanceState.getBoolean("pokRB"));
            this.prodazha.setChecked(savedInstanceState.getBoolean("proRB"));
            this.arrayTmp = savedInstanceState.getStringArray("arrayTmp");
        }
    }

    /**
     * Метод сохранения состояния активности
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("summainEx", this.summaIn.getText().toString());
        outState.putString("courseKey", this.course.getText().toString());
        outState.putString("result", this.result.getText().toString());

        outState.putInt("spinnerFrom", this.from.getSelectedItemPosition());
        outState.putInt("spinnerTo" , this.to.getSelectedItemPosition());
        //outState.putInt("rg",this.radioGroup.getCheckedRadioButtonId());
        outState.putBoolean("nbuRB",this.nbu.isChecked());
        outState.putBoolean("pokRB",this.pokupka.isChecked());
        outState.putBoolean("proRB",this.prodazha.isChecked());
        outState.putStringArray("arrayTmp",this.arrayTmp);

   }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.result     = (TextView)this.findViewById(R.id.sumOut);
        this.course     = (EditText)this.findViewById(R.id.course);
        this.summaIn    = (EditText)this.findViewById(R.id.sumIn);
        this.from       = (Spinner)this.findViewById(R.id.CurrencyFromSpinner);
        this.to         = (Spinner)this.findViewById(R.id.CurrencyToSpinner);
        //this.radioGroup = (RadioGroup)this.findViewById(R.id.rg);
        this.nbu        = (RadioButton)this.findViewById(R.id.nbu);
        this.pokupka    = (RadioButton)this.findViewById(R.id.pokupka);
        this.prodazha   = (RadioButton)this.findViewById(R.id.prodazha);
        //================NBU=======================================================================
        this.NBU.put("uaua", 1.0);      this.NBU.put("uaus", 0.04);   this.NBU.put("uaeu", 0.0357);
        this.NBU.put("usua", 25.0);     this.NBU.put("usus", 1.0);    this.NBU.put("useu", 0.893);
        this.NBU.put("euua", 28.0);     this.NBU.put("euus", 1.12);   this.NBU.put("eueu", 1.0);
        //================Pokupka===================================================================
        this.Pokupka.put("uaua", 1.0);  this.Pokupka.put("uaus", 0.0384); this.Pokupka.put("uaeu", 0.0345);
        this.Pokupka.put("usua", 26.0); this.Pokupka.put("usus", 1.0);    this.Pokupka.put("useu", 0.896);
        this.Pokupka.put("euua", 29.0); this.Pokupka.put("euus", 1.115);  this.Pokupka.put("eueu", 1.0);
        //================Prodazha==================================================================
        this.Prodazha.put("uaua", 1.0);   this.Prodazha.put("uaus", 0.037); this.Prodazha.put("uaeu", 0.0333);
        this.Prodazha.put("usua", 27.0);  this.Prodazha.put("usus", 1.0);   this.Prodazha.put("useu", 0.90);
        this.Prodazha.put("euua", 30.0);  this.Prodazha.put("euus", 1.111); this.Prodazha.put("eueu", 1.0);


        for (int i = 0; i < this.arrTypeCurrency.length; i++)
        {
            this.currencyTypes.add(new Currency(this.arrTypeCurrency[i]));
        }

        this.adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,this.currencyTypes);
        this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.from.setAdapter(this.adapter);
        this.to.setAdapter(this.adapter);

        from.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (parent.getId() == R.id.CurrencyFromSpinner) {
                            int index = position;
                            switch (index) {
                                case 0:
                                    MainActivity.this.arrayTmp[0] = "ua";
                                    MainActivity.this.takeCourse();
                                    break;
                                case 1:
                                    MainActivity.this.arrayTmp[0] = "us";
                                    MainActivity.this.takeCourse();
                                    break;
                                case 2:
                                    MainActivity.this.arrayTmp[0] = "eu";
                                    MainActivity.this.takeCourse();
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(MainActivity.this, "NothingSelected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        to.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (parent.getId() == R.id.CurrencyToSpinner)
                        {
                            int index = position;
                            switch(index)
                            {
                                case 0:
                                    MainActivity.this.arrayTmp[1] = "ua";
                                    MainActivity.this.takeCourse();
                                    break;
                                case 1:
                                    MainActivity.this.arrayTmp[1] = "us";
                                    MainActivity.this.takeCourse();
                                    break;
                                case 2:
                                    MainActivity.this.arrayTmp[1] = "eu";
                                    MainActivity.this.takeCourse();
                                    break;
                            }
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(MainActivity.this, "NothingSelected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }



    public void btnClick(View v)
    {
        this.summaToChange = parceDouble(String.valueOf(this.summaIn.getText()));
        double courseTmp = parceDouble(String.valueOf(this.course.getText()));
        this.summaResult = this.summaToChange * courseTmp;
        this.result.setText(String.valueOf(this.summaResult));

        //============================ Saving New Course ===========================================
        if(this.nbu.isChecked())
        {
            this.NBU.put(this.arrayTmp[0].concat(this.arrayTmp[1]), courseTmp);
        }
        if(this.pokupka.isChecked())
        {
            this.Pokupka.put(this.arrayTmp[0].concat(this.arrayTmp[1]), courseTmp);
        }
        if(this.prodazha.isChecked())
        {
            this.Prodazha.put(this.arrayTmp[0].concat(this.arrayTmp[1]), courseTmp);
        }
        //============================ Obnulenie Polya Result ======================================
        this.summaResult = 0;
    }

    public void checkBoxClick(View v)
    {
        //this.takeCourse();
        if(this.nbu.isChecked())
        {
            String key = this.arrayTmp[0].concat(this.arrayTmp[1]);
            this.NBU.get(key);
            this.course.setText(String.valueOf(this.NBU.get(key)));
        }
        else
        if(this.pokupka.isChecked())
        {
            String key = this.arrayTmp[0].concat(this.arrayTmp[1]);
            this.Pokupka.get(key);
            this.course.setText(String.valueOf(this.Pokupka.get(key)));
        }
        else
        if(this.prodazha.isChecked())
        {
            String key = this.arrayTmp[0].concat(this.arrayTmp[1]);
            this.Prodazha.get(key);
            this.course.setText(String.valueOf(this.Prodazha.get(key)));
        }
    }

    public void takeCourse()
    {
        if(this.nbu.isChecked())
        {
            String key = this.arrayTmp[0].concat(this.arrayTmp[1]);
            this.NBU.get(key);
            this.course.setText(String.valueOf(this.NBU.get(key)));
        }
        else
        if(this.pokupka.isChecked())
        {
            String key = this.arrayTmp[0].concat(this.arrayTmp[1]);
            this.Pokupka.get(key);
            this.course.setText(String.valueOf(this.Pokupka.get(key)));
        }
        else
        if(this.prodazha.isChecked())
        {
            String key = this.arrayTmp[0].concat(this.arrayTmp[1]);
            this.Prodazha.get(key);
            this.course.setText(String.valueOf(this.Prodazha.get(key)));
        }
    }

    public static double parceDouble(String s)
    {
        try
        {
            return Double.parseDouble(s);
        }
        catch(Exception e)
        {
            return 0;
        }
    }
}
