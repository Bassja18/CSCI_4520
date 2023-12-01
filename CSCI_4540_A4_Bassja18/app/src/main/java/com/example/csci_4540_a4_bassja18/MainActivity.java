package com.example.csci_4540_a4_bassja18;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.MotionEvent;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declare UI elements
    private ImageView canvasImageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private Path path;
    private boolean isFreehandMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        canvasImageView = findViewById(R.id.canvasImageView);
        Button drawRectangleButton = findViewById(R.id.drawRectangleButton);
        Button drawCircleButton = findViewById(R.id.drawCircleButton);
        Button drawButton = findViewById(R.id.drawButton);
        Button paintButton = findViewById(R.id.paintButton);
        Button clearButton = findViewById(R.id.clearButton);
        Button randomColorButton = findViewById(R.id.randomColorBtn);
        Button freehandButton = findViewById(R.id.freehandButton);
        TextView notes = findViewById(R.id.textView);
        CheckBox checkBoxFH = findViewById(R.id.checkBoxFreeHand);
        CheckBox checkBoxP = findViewById(R.id.checkBoxPaint);
        CheckBox checkBoxD = findViewById(R.id.checkBoxDraw);

        // Initialize the canvas bitmap
        bitmap = Bitmap.createBitmap(1000, 1800, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvasImageView.setImageBitmap(bitmap);

        // Initialize the paint for drawing and set the default attributes
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        // Initialize the path for freehand drawing
        path = new Path();

        // Set click listeners for buttons

        // Button to draw a random rectangle
        drawRectangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes.setVisibility(View.INVISIBLE);
                if (!isFreehandMode) {
                    // Generate random coordinates and sizes
                    int left = (int) (Math.random() * (canvas.getWidth() - 200));
                    int top = (int) (Math.random() * (canvas.getHeight() - 200));
                    int right = left + (int) (Math.random() * 200);
                    int bottom = top + (int) (Math.random() * 200);

                    // Draw the rectangle on the canvas
                    canvas.drawRect(left, top, right, bottom, paint);
                    canvasImageView.invalidate(); // Refresh the canvas
                }
            }
        });

        // Button to draw a random circle
        drawCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes.setVisibility(View.INVISIBLE);
                if (!isFreehandMode) {
                    // Generate random coordinates and radius
                    int centerX = (int) (Math.random() * canvas.getWidth());
                    int centerY = (int) (Math.random() * canvas.getHeight());
                    int radius = (int) (Math.random() * 200);

                    // Draw the circle on the canvas
                    canvas.drawCircle(centerX, centerY, radius, paint);
                    canvasImageView.invalidate(); // Refresh the canvas
                }
            }
        });

        // Button to switch to drawing mode
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes.setVisibility(View.INVISIBLE);

                // Enable drawing mode
                paint.setStyle(Paint.Style.STROKE);

                if (checkBoxP.isChecked()) {
                    checkBoxP.setChecked(false);
                    checkBoxD.setChecked(true);
                } else {
                    checkBoxD.setChecked(true);
                }
            }
        });

        // Button to switch to painting mode
        paintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes.setVisibility(View.INVISIBLE);

                // Enable painting mode
                paint.setStyle(Paint.Style.FILL);

                if (checkBoxD.isChecked()) {
                    checkBoxD.setChecked(false);
                    checkBoxP.setChecked(true);
                } else {
                    checkBoxP.setChecked(true);
                }
            }
        });

        // Button to clear the canvas
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the canvas
                path.reset();
                canvas.drawColor(Color.WHITE);
                canvasImageView.invalidate(); // Refresh the canvas
                notes.setVisibility(View.VISIBLE);
                isFreehandMode = !isFreehandMode;
                checkBoxFH.setChecked(false);
            }
        });

        // Button to set a random color for drawing
        randomColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randomColor = getRandomColor();
                paint.setColor(randomColor);
            }
        });

        // Button to toggle freehand drawing mode
        freehandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes.setVisibility(View.INVISIBLE);

                // Toggle freehand mode
                isFreehandMode = !isFreehandMode;

                if (isFreehandMode == true) {
                    checkBoxFH.setChecked(true);
                } else {
                    checkBoxFH.setChecked(false);
                }

                // Change the paint style based on the mode
                if (isFreehandMode && checkBoxD.isChecked()) {
                    paint.setStyle(Paint.Style.STROKE);
                } else if (isFreehandMode && checkBoxP.isChecked()){
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isFreehandMode) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    // Start a new path when the touch is pressed or released
                    path.moveTo(x, y);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    // Draw the path as the touch moves
                    path.lineTo(x, y);
                    canvas.drawPath(path, paint);
                    canvasImageView.invalidate(); // Refresh the canvas
                    return true;
                default:
                    return super.onTouchEvent(event);
            }
        } else {
            return super.onTouchEvent(event);
        }
    }

    // Function to generate a random color
    private int getRandomColor() {
        int alpha = 255; // Change the alpha value (transparency) as needed
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);

        return Color.argb(alpha, red, green, blue);
    }
}