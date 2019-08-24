package ru.startandroid.sfwef;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    TextView currentExpression, currentResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentExpression = findViewById(R.id.currentExpression);
        currentResult = findViewById(R.id.currentResult);
    }

    public void clicked(View v) {
        currentExpression.append(((Button)v).getText().toString());
    }

    public void backspaced(View v) {
        if (currentExpression.getText().length() != 0) {
            CharSequence res = currentExpression.getText().toString().subSequence(0, currentExpression.getText().length() - 1);
            currentExpression.setText(res);
        } else currentExpression.setText("");
        currentResult.setText("");
    }

    public void clear(View v) {
        currentExpression.setText("");
        currentResult.setText("");
    }

    public void calculate(View v) {
        try {
           BigDecimal res = new Expression(currentExpression.getText().toString()).eval();
            currentResult.setText(res.toString());
        } catch (Exception e) {
            currentResult.setText("Failed");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("1", currentExpression.getText().toString());
        outState.putString("2", currentResult.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentExpression.setText(savedInstanceState.getString("1"));
        currentResult.setText(savedInstanceState.getString("2"));
    }
}
