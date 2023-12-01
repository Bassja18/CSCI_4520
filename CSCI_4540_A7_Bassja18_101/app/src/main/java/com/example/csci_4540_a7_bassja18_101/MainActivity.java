package com.example.csci_4540_a7_bassja18_101;

// Import necessary libraries
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button predictButton;
    private TextView resultTextView;
    private Interpreter tflite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        imageView = findViewById(R.id.imageView);
        predictButton = findViewById(R.id.predictButton);
        resultTextView = findViewById(R.id.resultTextView);

        // Load the TensorFlow Lite model
        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set click listener for the "Predict" button
        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classifyImage();
            }
        });
    }

    // Load the TensorFlow Lite model from the assets folder
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd("flower_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // Classify the loaded image using the TensorFlow Lite model
    private void classifyImage() {
        // Load the image from the resources (replace with your image loading logic)
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_flower);

        // Preprocess the image (resize, normalize, etc.)
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);
        int[] intValues = new int[INPUT_SIZE * INPUT_SIZE];
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
        // Normalize the pixel values if necessary
        float[] floatValues = new float[INPUT_SIZE * INPUT_SIZE * 3];
        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            floatValues[i * 3 + 0] = ((val >> 16) & 0xFF) / 255.0f;
            floatValues[i * 3 + 1] = ((val >> 8) & 0xFF) / 255.0f;
            floatValues[i * 3 + 2] = (val & 0xFF) / 255.0f;
        }

        // Run inference
        float[][] outputScores = new float[1][NUM_CLASSES];
        tflite.run(floatValues, outputScores);

        // Post-process the inference results
        // Find the index of the class with the highest score
        int predictedClassIndex = 0;
        for (int i = 1; i < NUM_CLASSES; i++) {
            if (outputScores[0][i] > outputScores[0][predictedClassIndex]) {
                predictedClassIndex = i;
            }
        }

        // Map the index to the corresponding class label (replace with your own labels)
        String[] labels = {"Rose", "Sunflower"};
        String predictedClass = labels[predictedClassIndex];

        // Display the result in the TextView
        resultTextView.setText("Predicted: " + predictedClass);
    }


    @Override
    protected void onDestroy() {
        // Release resources when the activity is destroyed
        if (tflite != null) {
            tflite.close();
            tflite = null;
        }
        super.onDestroy();
    }
}
