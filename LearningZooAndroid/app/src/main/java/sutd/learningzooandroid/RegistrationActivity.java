package sutd.learningzooandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void registerNewUser(View v){
        EditText email = (EditText) findViewById(R.id.reg_email);
        EditText password = (EditText) findViewById(R.id.reg_password);
        EditText subjectId = (EditText) findViewById(R.id.reg_subId);

        RequestParams params =  new RequestParams();
        params.put("email",email.getText().toString());
        params.put("subject_id",subjectId.getText().toString());
        params.put("password",password.getText().toString());
        ClientUsage register = ClientUsage.getClientUsage();
        register.registerTeacher(params, new OnJSONResponseCallback() {
//            private ClientUsage register;
//            public OnJSONResponseCallback(ClientUsage register) {
//                this.register = register;
//            }
            @Override
            public void onJSONResponse(boolean success, JSONObject response) {
                if(success){
                    Toast.makeText(getApplicationContext(),"Succeeded at registering user",Toast.LENGTH_SHORT).show();
                    Intent switchback = new Intent(getBaseContext(),LoginActivity.class);
                    startActivity(switchback);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Failed to register user",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //fields required for user sign_up: email, password, subject_id
}
