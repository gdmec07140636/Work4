package myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.wengjunyu.myapplication.R;

public class ContactsMessageActivity extends Activity {
    private TextView nameTextView;
    private TextView mobileTextView;
    private TextView qqTextView;
    private TextView danweiTextView;
    private TextView addressTextView;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        setTitle("��ϵ����Ϣ");
        nameTextView = (TextView) findViewById(R.id.name);
        mobileTextView = (TextView) findViewById(R.id.mobile);
        danweiTextView = (TextView) findViewById(R.id.danwei);
        qqTextView = (TextView) findViewById(R.id.qq);
        addressTextView = (TextView) findViewById(R.id.address);

        Bundle localBundle = getIntent().getExtras();
        int id = localBundle.getInt("user_ID");
        ContactsTable ct = new ContactsTable(this);
        user = ct.getUserByID(id);
        nameTextView.setText("������"+user.getName());
        mobileTextView.setText("�绰��"+user.getMobile());
        qqTextView.setText("QQ��"+user.getQq());
        danweiTextView.setText("��λ��"+user.getDanwei());
        addressTextView.setText("��ַ��"+user.getAddress());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,1,Menu.NONE,"����");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case 1:
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
