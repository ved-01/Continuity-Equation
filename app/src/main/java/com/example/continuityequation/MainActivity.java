package com.example.continuityequation;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    String[] dom =  {"Electromagnetism","Fluid Dynamics","Energy and Heat", "Computer Vision"};
    AutoCompleteTextView autoCompleteTxt;
    int pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn=findViewById(R.id.button);
        TextView tv=findViewById(R.id.tv);
        AutoCompleteTextView x=findViewById(R.id.x);
        AutoCompleteTextView y=findViewById(R.id.y);
        AutoCompleteTextView z=findViewById(R.id.z);
        AutoCompleteTextView t=findViewById(R.id.t);

        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }

        Python py=Python.getInstance();
        final PyObject pyobj1=py.getModule("divergence");
        final PyObject pyobj2=py.getModule("derivative");
        final PyObject pyobj3=py.getModule("subs");
        final PyObject pyobj4=py.getModule("mul_add");

        autoCompleteTxt=findViewById(R.id.act);
        ArrayAdapter<String> adapterItems= new ArrayAdapter<>(this, R.layout.dropitem, dom);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener((parent, view, position, id) -> {
            String domain_nam = parent.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(),"Domain: "+domain_nam,Toast.LENGTH_SHORT).show();
            pos=position;

            TextInputEditText tiet1=findViewById(R.id.tiet1);
            TextInputEditText tiet2=findViewById(R.id.tiet2);
            tv.setText("");
            clear(tiet1,tiet2,x,y,z,t);
            switch (position){
                case 0:
                    tiet1.setHint("Current Density ");
                    tiet2.setHint("Charge Density");
                    break;
                case 1:
                    tiet2.setHint("Density of Fluid");
                    tiet1.setHint("Fluid Velocity ");
                    break;
                case 2:
                    tiet1.setHint("Energy Flux ");
                    tiet2.setHint("Energy Density");

                    break;
                case 3:
                    tiet2.setHint("Image Intensity");
                    tiet1.setHint("Optical Velocity ");
                    break;
            }
        });

        btn.setOnClickListener(view -> {try {
                TextInputEditText tiet1 = findViewById(R.id.tiet1);
                TextInputEditText tiet2 = findViewById(R.id.tiet2);
                String gtiet1 = Objects.requireNonNull(tiet1.getText()).toString();
                String gtiet2 = Objects.requireNonNull(tiet2.getText()).toString();

                int valx = Integer.parseInt(x.getText().toString());
                int valy = Integer.parseInt(y.getText().toString());
                int valz = Integer.parseInt(z.getText().toString());
                int valt = Integer.parseInt(t.getText().toString());

                PyObject obj1 = pyobj1.callAttr("divergence", gtiet1, valx, valy, valz);
                PyObject obj2 = pyobj2.callAttr("derivative", gtiet2, valt);
                PyObject obj3 = pyobj3.callAttr("sub", gtiet2, valt);
                PyObject obj4= pyobj4.callAttr("mul_add", obj3,obj1,obj2);

                switch (pos) {
                    case 1:
                    case 3:
                        tv.setText(obj4.toString());
                        break;
                    case 0:
                    case 2:
                        System.out.println(obj2.toString()+" "+obj1.toString());
                        if ( obj1.toInt() + obj2.toInt()== 0) tv.setText("Continuous");
                        else tv.setText("Not Continuous");
                        break;
                }
            }
            catch (Exception e){
                tv.setText("Enter a Valid Input!!");
            }
        });
    }
    public void clear(TextInputEditText v1,TextInputEditText v2,AutoCompleteTextView a1,AutoCompleteTextView a2,AutoCompleteTextView a3,AutoCompleteTextView a4){
        v1.setText("");
        v2.setText("");
        a1.setText("");
        a2.setText("");
        a3.setText("");
        a4.setText("");
    }
}