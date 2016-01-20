package myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wengjunyu.myapplication.R;

public class MyContactsActivity extends Activity {
    private ListView listView;
    private BaseAdapter listViewAdapter;
    private User users[];
    private int selecteItem = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("ͨѶ¼");
        listView = (ListView) findViewById(R.id.listView);
        loadContacts();
    }

    private void loadContacts() {
        ContactsTable ct = new ContactsTable(this);
        users = ct.getAllUser();
        listViewAdapter=new BaseAdapter() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                {
                    TextView textView=new TextView(MyContactsActivity.this);
                    textView.setTextSize(22);
                    convertView=textView;
                }
                String mobile=users[position].getMobile()==null?"" :users[position].getMobile();
                ((TextView)convertView).setText(users[position].getName()+"---"+mobile);
                if (position==selecteItem)
                {
                    convertView.setBackgroundColor(Color.YELLOW);
                }else
                {
                    convertView.setBackgroundColor(0);
                }
                return convertView;
            }
            @Override
            public Object getItem(int position) {
                return users[position];
            }

            @Override
            public int getCount() {
                return users.length;
            }


            @Override
            public long getItemId(int position) {
                return position;
            }


        };
        //����listView�ؼ���������
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //��¼�����е�λ��
                selecteItem = arg2;
                //ˢ���б�
                listViewAdapter.notifyDataSetChanged();
            }
        });
    }
    //�����˵�
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,1,Menu.NONE,"���");
        menu.add(Menu.NONE,2,Menu.NONE,"�༭");
        menu.add(Menu.NONE,3,Menu.NONE,"�鿴��Ϣ");
        menu.add(Menu.NONE,4,Menu.NONE,"ɾ��");
        menu.add(Menu.NONE,5,Menu.NONE,"��ѯ");
        menu.add(Menu.NONE, 6, Menu.NONE, "���뵽�ֻ��绰��");
        menu.add(Menu.NONE, 7, Menu.NONE, "�˳�");
        return super.onCreateOptionsMenu(menu);
    }
    //�˵��¼�
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case 1://���
                Intent intent = new Intent(MyContactsActivity.this, AddContactsActivity.class);
                startActivity(intent);
                break;
            case 2://�༭
                if (users[selecteItem].getId_DB() > 0) {
                    intent = new Intent(MyContactsActivity.this, UpdateContactsActivity.class);
                    intent.putExtra("user_ID", users[selecteItem].getId_DB());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "�޽����¼���޷�������", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3://�鿴��Ϣ
                if (users[selecteItem].getId_DB() > 0) {
                    intent = new Intent(MyContactsActivity.this, ContactsMessageActivity.class);
                    intent.putExtra("user_ID", users[selecteItem].getId_DB());
                    startActivity(intent);
                } else
                {
                    Toast.makeText(this,"�޽����¼���޷�������",Toast.LENGTH_SHORT).show();
                }
                break;
            case 4://ɾ��
                if (users[selecteItem].getId_DB() > 0 )
                {
                    delete();
                }else
                {
                    Toast.makeText(this,"�޽����¼���޷�������",Toast.LENGTH_SHORT).show();
                }
                break;
            case 5://��ѯ
                new FindDialog(this).show();
                break;
            case 6://���뵽�ֻ��绰��
                if (users[selecteItem].getId_DB() > 0)
                {
                    importPhone(users[selecteItem].getName(), users[selecteItem].getMobile());
                    Toast.makeText(this,"�Ѿ��ɹ������ֻ��绰����",Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(this,"�޽����¼���޷�������",Toast.LENGTH_SHORT).show();
                }
                break;
            case 7://�˳�
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //���¼�������
        ContactsTable ct = new ContactsTable(this);
        users = ct.getAllUser();
        //ˢ���б�
        listViewAdapter.notifyDataSetChanged();
    }
    public void delete(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("ϵͳ��Ϣ");
        alert.setMessage("�Ƿ�ɾ����ϵ�ˣ�");
        alert.setPositiveButton("��", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                ContactsTable ct = new ContactsTable(MyContactsActivity.this);
                if (ct.deleteByUser(users[selecteItem])) {
                    //���»�ȡ�ֻ�
                    users = ct.getAllUser();
                    //ˢ���б�
                    listViewAdapter.notifyDataSetChanged();
                    selecteItem = 0;
                    Toast.makeText(MyContactsActivity.this, "ɾ���ɹ���", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyContactsActivity.this, "ɾ��ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("��", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }
    //���뵽�绰��
    public void importPhone(String name,String phone)
    {
        //ϵͳͨѶ¼ContentProvider��URI;
        Uri phoneURL = ContactsContract.Data.CONTENT_URI;
        ContentValues values = new ContentValues();
        //������RawContacts.CONTENT_URIִ��һ��ֵ����
        //�õ��ǻ�ȡϵͳ���ص�rawContactId
        Uri rawContactUri = this.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        //��data�������������
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,name);
        this.getContentResolver().insert(phoneURL,values);
        //��data�������绰����
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,phone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        this.getContentResolver().insert(phoneURL,values);

    }
}


