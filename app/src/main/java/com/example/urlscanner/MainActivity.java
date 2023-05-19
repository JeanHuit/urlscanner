package com.example.urlscanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.urlscanner.ml.Model;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import org.tensorflow.lite.Interpreter;

// Load the TensorFlow Lite model from the assets folder



public class MainActivity extends AppCompatActivity {


    Button scan_btn, predict_btn, train_btn;
    TextView result_view, training_view, predict_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan_btn = findViewById(R.id.scanner_btn);
        result_view = findViewById(R.id.result_view);

        predict_btn = findViewById(R.id.predict_btn);
        predict_view = findViewById(R.id.predict_view);

        training_view = findViewById(R.id.training_view);
        train_btn =  findViewById(R.id.train_btn);

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setPrompt("Scan a QR code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });

        predict_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });


        train_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Interpreter.Options options = new Interpreter.Options();
                try {
                    Interpreter interpreter = new Interpreter(loadModelFile(MainActivity.this, "model.tflite"), options);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult != null){
            String contents = intentResult.getContents();
            if(contents != null){
                result_view.setText(contents);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


    private ByteBuffer loadModelFile(Context context, String filename) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(filename);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


}