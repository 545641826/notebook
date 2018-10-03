package com.example;
//这部分代码压根没调用过，我当时是怎么想的？？
//玄之又玄，不删为好。下次重构把它删了。
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by 54564 on 2018/2/27.
 */
public class WriteActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        final SharedPreferences sp = getSharedPreferences("one", MODE_PRIVATE);
        String context=sp.getString("one",null);
        final EditText text = (EditText) findViewById(R.id.textView);
        Button button= (Button) findViewById(R.id.button);
        if(context==null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String gettext = text.getText().toString();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("one", gettext);
                    editor.commit();
                    Toast.makeText(WriteActivity.this, "save successfully", Toast.LENGTH_LONG).show();

                }
            });
        }
        else {
            text.setText(context);
        }
    }
}
