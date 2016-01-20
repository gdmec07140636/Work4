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
        setTitle("通讯录");
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
        //设置listView控件的适配器
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //记录单击列的位置
                selecteItem = arg2;
                //刷新列表
                listViewAdapter.notifyDataSetChanged();
            }
        });
    }
    //创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,1,Menu.NONE,"添加");
        menu.add(Menu.NONE,2,Menu.NONE,"编辑");
        menu.add(Menu.NONE,3,Menu.NONE,"查看信息");
        menu.add(Menu.NONE,4,Menu.NONE,"删除");
        menu.add(Menu.NONE,5,Menu.NONE,"查询");
        menu.add(Menu.NONE, 6, Menu.NONE, "导入到手机电话簿");
        menu.add(Menu.NONE, 7, Menu.NONE, "退出");
        return super.onCreateOptionsMenu(menu);
    }
    //菜单事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case 1://添加
                Intent intent = new Intent(MyContactsActivity.this, AddContactsActivity.class);
                startActivity(intent);
                break;
            case 2://编辑
                if (users[selecteItem].getId_DB() > 0) {
                    intent = new Intent(MyContactsActivity.this, UpdateContactsActivity.class);
                    intent.putExtra("user_ID", users[selecteItem].getId_DB());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "无结果记录，无法操作！", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3://查看信息
                if (users[selecteItem].getId_DB() > 0) {
                    intent = new Intent(MyContactsActivity.this, ContactsMessageActivity.class);
                    intent.putExtra("user_ID", users[selecteItem].getId_DB());
                    startActivity(intent);
                } else
                {
                    Toast.makeText(this,"无结果记录，无法操作！",Toast.LENGTH_SHORT).show();
                }
                break;
            case 4://删除
                if (users[selecteItem].getId_DB() > 0 )
                {
                    delete();
                }else
                {
                    Toast.makeText(this,"无结果记录，无法操作！",Toast.LENGTH_SHORT).show();
                }
                break;
            case 5://查询
                new FindDialog(this).show();
                break;
            case 6://导入到手机电话簿
                if (users[selecteItem].getId_DB() > 0)
                {
                    importPhone(users[selecteItem].getName(), users[selecteItem].getMobile());
                    Toast.makeText(this,"已经成功导到手机电话簿！",Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(this,"无结果记录，无法操作！",Toast.LENGTH_SHORT).show();
                }
                break;
            case 7://退出
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
        //重新加载数据
        ContactsTable ct = new ContactsTable(this);
        users = ct.getAllUser();
        //刷新列表
        listViewAdapter.notifyDataSetChanged();
    }
    public void delete(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("系统消息");
        alert.setMessage("是否删除联系人？");
        alert.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                ContactsTable ct = new ContactsTable(MyContactsActivity.this);
                if (ct.deleteByUser(users[selecteItem])) {
                    //重新获取手机
                    users = ct.getAllUser();
                    //刷新列表
                    listViewAdapter.notifyDataSetChanged();
                    selecteItem = 0;
                    Toast.makeText(MyContactsActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyContactsActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }
    //导入到电话簿
    public void importPhone(String name,String phone)
    {
        //系统通讯录ContentProvider的URI;
        Uri phoneURL = ContactsContract.Data.CONTENT_URI;
        ContentValues values = new ContentValues();
        //首先向RawContacts.CONTENT_URI执行一空值插入
        //久等是获取系统返回的rawContactId
        Uri rawContactUri = this.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        //往data表插入姓名数据
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,name);
        this.getContentResolver().insert(phoneURL,values);
        //往data表里插入电话数据
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,phone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        this.getContentResolver().insert(phoneURL,values);

    }
}


