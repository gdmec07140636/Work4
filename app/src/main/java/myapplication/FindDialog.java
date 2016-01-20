package myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wengjunyu.myapplication.R;

/**
 * Created by wengjunyu on 2016/1/20.
 */
public class FindDialog extends Dialog {
    private Context appcontext;
    public FindDialog(Context context){
        super(context);
        appcontext = context;
    }
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find);
        setTitle("联系人查询");
        final Button find = (Button) findViewById(R.id.find);
        Button cancel = (Button) findViewById(R.id.cancel);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText value = (EditText) findViewById(R.id.value);
                ContactsTable ct = new ContactsTable(appcontext);
                User[] users = ct.findUserByKey(value.getText().toString());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i<users.length; i++)
                {
                    sb.append("姓名是" + users[i].getName() + ",电话是" + users[i].getMobile());
                }
                Toast.makeText(appcontext, sb.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
