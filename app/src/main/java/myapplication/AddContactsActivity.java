package myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wengjunyu.myapplication.R;

public class AddContactsActivity extends Activity {
    private EditText nameEditText;
    private EditText mobileEditText;
    private EditText qqEditText;
    private EditText danweiEitText;
    private EditText addressEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        setTitle("�����ϵ��");
        nameEditText = (EditText) findViewById(R.id.name);
        mobileEditText = (EditText) findViewById(R.id.mobile);
        danweiEitText = (EditText) findViewById(R.id.danwei);
        qqEditText = (EditText) findViewById(R.id.qq);
        addressEditText = (EditText) findViewById(R.id.address);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, "����");
        menu.add(Menu.NONE,2,Menu.NONE,"����");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case 1://����
                if(!nameEditText.getText().toString().equals("")){
                    User user = new User();
                    user.setName(nameEditText.getText().toString());
                    user.setMobile(mobileEditText.getText().toString());
                    user.setDanwei(danweiEitText.getText().toString());
                    user.setQq(qqEditText.getText().toString());
                    user.setAddress(addressEditText.getText().toString());
                    ContactsTable ct = new ContactsTable(AddContactsActivity.this);
                    if(ct.addData(user))
                    {
                        Toast.makeText(AddContactsActivity.this,"��ӳɹ���",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {

                        Toast.makeText(AddContactsActivity.this,"���ʧ�ܣ�",Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(AddContactsActivity.this,"��������������",Toast.LENGTH_SHORT).show();

                }
                break;
            case 2:
                finish();
                break;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
