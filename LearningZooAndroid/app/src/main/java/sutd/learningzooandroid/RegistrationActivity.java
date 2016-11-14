package sutd.learningzooandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;

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
        System.out.println(email.getText().toString());//.replace("@","\\@"));
        params.put("password",password.getText().toString());
        params.put("subject_id",subjectId.getText().toString());
        ClientUsage register = new ClientUsage();
        register.registerTeacher(params);

    }
    //fields required for user sign_up: email, password, subject_id
}
