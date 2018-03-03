package com.example.marvinjason.huffmancoding;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    private EditText editText;
    private Button button;
    private TextView textViewUncompressed;
    private TextView textViewCompressed;
    private LinearLayout linearLayout;
    private HuffmanCoding huffmanCoding;
    private Paint paintCircle;
    private Paint paintText;
    private ScrollView scrollView;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout.LayoutParams params1;
    private LinearLayout.LayoutParams params2;
    private LinearLayout.LayoutParams params3;
    private LinearLayout.LayoutParams params4;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        textViewUncompressed = (TextView) findViewById(R.id.textView2);
        textViewCompressed = (TextView) findViewById(R.id.textView3);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        paintCircle = new Paint();
        paintCircle.setColor(Color.WHITE);
        paintText = new Paint();
        paintText.setColor(Color.parseColor("#00770e"));
        paintText.setTextSize(30);

        params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int dp = (int) (50 * (float) MainActivity.this.getResources().getDisplayMetrics().density);
        params1.setMargins(dp, 0, 0, 0);
        params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        horizontalScrollView = new HorizontalScrollView(MainActivity.this);
        horizontalScrollView.setLayoutParams(params3);

        button.setOnClickListener(new View.OnClickListener(){
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                if (!editText.getText().toString().isEmpty())
                {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    if (getCurrentFocus() != null)
                    {
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    huffmanCoding = new HuffmanCoding(editText.getText().toString());
                    huffmanCoding.compress();

                    textViewUncompressed.setText("Uncompressed: " + huffmanCoding.getUncompressedSize() + " bits");
                    textViewCompressed.setText("Compressed: " + huffmanCoding.getCompressedSize() + " bits");

                    linearLayout.removeAllViews();
                    horizontalScrollView.removeAllViews();

                    params2 = new LinearLayout.LayoutParams(huffmanCoding.getRoot().getWidth(), huffmanCoding.getRoot().getHeight() + 100);

                    TextView temp;

                    for (String str: huffmanCoding.getDictionary())
                    {
                        temp = new TextView(MainActivity.this);
                        temp.setText(str);
                        temp.setTypeface(Typeface.MONOSPACE);
                        temp.setPadding(0, 5, 0, 5);
                        temp.setLayoutParams(params1);
                        linearLayout.addView(temp);
                    }

                    temp = new TextView(MainActivity.this);
                    temp.setText("Compression Rate: " + ((int) (100 - (double) huffmanCoding.getCompressedSize() / huffmanCoding.getUncompressedSize() * 100)) + "% more efficient");
                    temp.setTypeface(Typeface.MONOSPACE);
                    temp.setPadding(0, 30, 0, 10);
                    temp.setLayoutParams(params1);
                    linearLayout.addView(temp);

                    temp = new TextView(MainActivity.this);
                    temp.setText("Compressed Text:\n\n" + huffmanCoding.getCompressedString());
                    temp.setTypeface(Typeface.MONOSPACE);
                    temp.setPadding(0, 0, 50, 50);
                    temp.setLayoutParams(params1);
                    linearLayout.addView(temp);

                    ImageView imageView = new ImageView(MainActivity.this);
                    imageView.setLayoutParams(params2);

                    Bitmap bmp = Bitmap.createBitmap(huffmanCoding.getRoot().getWidth() + 60, huffmanCoding.getRoot().getHeight() + 80, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bmp);

                    drawTree(huffmanCoding.getRoot(), canvas, huffmanCoding.getRoot().getMiddle() + (huffmanCoding.getRoot().getHeight() / 4) + 30, 30);

                    imageView.setImageBitmap(bmp);
                    LinearLayout layout = new LinearLayout(MainActivity.this);
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    layout.addView(imageView);
                    horizontalScrollView.addView(layout);
                    params4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params4.gravity = Gravity.CENTER_HORIZONTAL;
                    horizontalScrollView.setLayoutParams(params4);
                    linearLayout.addView(horizontalScrollView);
                }
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void drawTree(Node node, Canvas canvas, int xAxisCircle, int yAxisCircle) {
        canvas.drawCircle(xAxisCircle, yAxisCircle, 30, paintCircle);
        canvas.drawText(String.valueOf(node.getData()), xAxisCircle - 12, yAxisCircle + 8, paintText);

        if (node.getLeftChild() != null) {
            int x = xAxisCircle - (node.getLeftChild().getWidth() - node.getLeftChild().getMiddle()) - 30;
            int y = yAxisCircle + 80;

            canvas.drawLine(xAxisCircle, yAxisCircle, x, y, paintCircle);
            drawTree(node.getLeftChild(), canvas, x, y);
        }

        if (node.getRightChild() != null) {
            int x = xAxisCircle + (node.getRightChild().getWidth() - (node.getRightChild().getWidth() - node.getRightChild().getMiddle())) + 30;
            int y = yAxisCircle + 80;

            canvas.drawLine(xAxisCircle, yAxisCircle, x, y, paintCircle);
            drawTree(node.getRightChild(), canvas, x, y);
        }

        if (node.getCharacter() != '\0') {
            canvas.drawCircle(xAxisCircle, yAxisCircle + 80, 30, paintCircle);
            canvas.drawText(Character.toString(node.getCharacter()), xAxisCircle - 12, yAxisCircle + 88, paintText);
        }
    }
}
