package es.ucm.fdi.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Calculator calculator;

    private EditText etX;
    private EditText etY;
    private Button addButton;

    public static final String RESULT_KEY = "RESULT";
    public static final String X_KEY = "X";
    public static final String Y_KEY = "Y";
    public static final int CALCULATOR_RESULT_CODE = 1;
    public static final int OK_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("CALC_INFO", "onCreate iniciado");

        calculator = new Calculator();

        etX = findViewById(R.id.editTextNumberDecimal_X);
        etY = findViewById(R.id.editTextNumberDecimal_Y);
        addButton = findViewById(R.id.button);

        Log.i("CALC_INFO", "Variales de MainActivity inicializadas");
    }

    public void addXandY(View view){
        Log.i("CALC_INFO", "Se ha pulsado el botón de sumar");

        if(isEmpty(etX)){
            Log.i("CALC_INFO", "Edit Text X está vacio, no se puede sumar");
            return;
        }

        if(isEmpty(etY)){
            Log.i("CALC_INFO", "Edit Text Y está vacio, no se puede sumar");
            return;
        }

        Log.i("CALC_INFO", "Asignando valores de los EditText a variables");
        Integer x = Integer.parseInt(etX.getText().toString());
        Integer y = Integer.parseInt(etY.getText().toString());


        Log.i("CALC_INFO", "Se va a realizar la suma");
        Integer result = calculator.add(x, y);

        Intent intent = new Intent(this, CalculatorResultActivity.class);
        intent.putExtra(MainActivity.RESULT_KEY, result);
        intent.putExtra(MainActivity.X_KEY, x);
        intent.putExtra(MainActivity.Y_KEY, y);
        startActivityForResult(intent, MainActivity.OK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == MainActivity.OK_CODE) {

                String result = data.getStringExtra(CalculatorResultActivity.REPLY);

                Log.i("CALC_INFO", "El resultado devuelto por CalculatorResultActivity es " + result);
                Toast.makeText(MainActivity.this, result,
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
}