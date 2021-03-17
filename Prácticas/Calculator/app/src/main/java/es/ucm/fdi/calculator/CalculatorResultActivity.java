package es.ucm.fdi.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CalculatorResultActivity extends AppCompatActivity {

    private TextView tvResult;
    private TextView tvDesc;
    private Button turnBackButton;

    public static final String REPLY = "REPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_result);

        Log.i("CALC_ACT_INFO", "Se esta creando el resultActivity");

        tvResult = findViewById(R.id.tvResult);
        tvDesc = findViewById(R.id.tvPresentacion);
        turnBackButton = findViewById(R.id.button2);

        Intent intent = getIntent();

        if(intent != null){
            onCreateByMain(intent);
            return;
        }
        tvResult.setText("????");
    }

    public void onCreateByMain(Intent intent){

        String x = "" + intent.getIntExtra(MainActivity.X_KEY, -1);
        String y = "" + intent.getIntExtra(MainActivity.Y_KEY, -1);
        String desc = "La suma de " + x + "+" + y + " es...";
        tvDesc.setText(desc);

        String result = "" + intent.getIntExtra(MainActivity.RESULT_KEY, -1);
        tvResult.setText(result);

        Log.i("CALC_ACT_INFO", "El resultado es " + result);

        Intent replyIntent = new Intent();
        replyIntent.putExtra(MainActivity.RESULT_KEY, result);
        setResult(MainActivity.OK_CODE, replyIntent);

        Log.i("CALC_ACT_INFO", "El intent de respuesta se ha creado");
    }

    public void onSumarOtraVez(View view){
        Log.i("CALC_ACT_INFO", "Llamada a onSumarOtraVez, se va a cerrar la activity");
        finish(); //terminar la actividad (volver a MainActivity)
    }

}
