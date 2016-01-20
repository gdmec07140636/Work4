package myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wengjunyu.myapplication.R;

public class UpdateContactsActivity extends Activity {
    private EditText nameEditText;
    private EditText mobileEditText;
    private EditText qqEditText;
    private EditText danweiEditText;
    private EditText addressEditText;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        setTitle("�޸���ϵ��");
        nameEditText = (EditText) findViewById(R.id.name);
        mobileEditText = (EditText) findViewById(R.id.mobile);
        danweiEditText = (EditText) findViewById(R.id.danwei);
        qqEditText = (EditText) findViewById(R.id.qq);
        addressEditText = (EditText) findViewById(R.id.address);
        //��Ҫ�޸ĵ���ϵ�����ݸ�ֵ���û����������ʾ
        Bundle localBundle = getIntent().getExtras();
        int id = localBundle.getInt("user_ID");
        ContactsTable ct = new ContactsTable(this);
        user = ct.getUserByID(id);
        nameEditText.setText(user.getName());
        mobileEditText.setText(user.getMobile());
        qqEditText.setText(user.getQq());
        danweiEditText.setText(user.getDanwei());
        addressEditText.setText(user.getAddress());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,1,Menu.NONE,"����");
        menu.add(Menu.NONE,2,Menu.NONE,"����");
        return super.onCreateOptionsMenu(menu);
    }
    //�˵��¼�
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case 1:
                if(!nameEditText.getText().toString().equals(""))
                {
                    user.setName(nameEditText.getText().toString());
                    user.setMobile(mobileEditText.getText().toString());
                    user.setDanwei(danweiEditText.getText().toString());
                    user.setQq(qqEditText.getText().toString());
                    user.setAddress(addressEditText.getText().toString());
                    ContactsTable ct = new ContactsTable(UpdateContactsActivity.this);
                    //�޸����ݿ���ϵ����Ϣ
                    if(ct.updateUser(user))
                    {
                        Toast.makeText(UpdateContactsActivity.this,"�޸ĳɹ���",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(UpdateContactsActivity.this,"�޸�ʧ�ܣ�",Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(UpdateContactsActivity.this,"���ݲ���Ϊ�գ�",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
