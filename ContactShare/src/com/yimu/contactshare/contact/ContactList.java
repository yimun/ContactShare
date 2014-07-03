package com.yimu.contactshare.contact;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.yimu.contactshare.R;
import com.yimu.contactshare.bean.People;

public class ContactList extends Activity {

	private static final boolean D = true;
	private static final String TAG = "ContactList";

	/** UI */
	private LinearLayout layoutIndex;
	private ListView listView;
	private ContactListAdapter adapter;
	private TextView tv_show;// 中间显示标题的文本
	private CheckBox checkAll;
	private Button btn_OK;

	/** 字母索引表 */
	private String[] arr_index = { "#", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "U", "V", "W", "X",
			"Y", "Z" };

	private int height; // 边栏字体的高度
	private List<People> listData; // 联系人总表
	private static boolean isCheckAll = false;
	public static boolean[] checkedPos; // 位置是否选择

	/** 获取库Phone表字段 **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };
	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 1;
	/** 头像ID **/
	private static final int PHONES_PHOTO_ID_INDEX = 2;
	/** 联系人的ID **/
	private static final int PHONES_CONTACT_ID_INDEX = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (D)
			Log.i(TAG, " ++ onCreate ++");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// UI Get
		setContentView(R.layout.contact_list);
		layoutIndex = (LinearLayout) this.findViewById(R.id.layout);
		layoutIndex.setBackgroundColor(Color.parseColor("#00ffffff"));
		listView = (ListView) findViewById(R.id.listView1);
		tv_show = (TextView) findViewById(R.id.tv);
		checkAll = (CheckBox) findViewById(R.id.checkBox1);
		btn_OK = (Button) findViewById(R.id.button1);

		// 获取联系人信息
		getPhoneContacts();

		adapter = new ContactListAdapter(this, listData, this.arr_index);
		listView.setAdapter(adapter);
		

		tv_show.setVisibility(View.INVISIBLE);
		checkedPos = new boolean[listData.size()];

		// 全选框监听
		checkAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					isCheckAll = true;
				} else {
					isCheckAll = false;
				}
				for (int i = 0; i < listData.size(); i++)
					checkedPos[i] = isCheckAll;
				// 刷新类表
				listView.setAdapter(adapter);
			}
		});
		btn_OK.setOnClickListener(new myOnClickListener());
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/** 得到手机通讯录联系人信息 **/
	private void getPhoneContacts() {
		listData = new ArrayList<People>();
		ContentResolver resolver = this.getContentResolver();
		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				People people = new People();
				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				// 得到联系人名称
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);
				// 得到联系人ID
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
				// 得到联系人头像ID
				Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
				// 得到联系人头像Bitamp
				Bitmap contactPhoto = null;
				// photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
				if (photoid > 0) {
					Uri uri = ContentUris.withAppendedId(
							ContactsContract.Contacts.CONTENT_URI, contactid);
					InputStream input = ContactsContract.Contacts
							.openContactPhotoInputStream(resolver, uri);
					contactPhoto = BitmapFactory.decodeStream(input);
				} else {
					contactPhoto = BitmapFactory.decodeResource(getResources(),
							R.drawable.ic_launcher);
				}
				people.setName(contactName);
				people.setIndex(Pinyin4j.getHanyuPinyin(people.getName()).substring(
						0, 1)
						+ people.getName().substring(0, 1));
				people.setPhone(phoneNumber);
				people.setBitmap(contactPhoto);
				listData.add(people);
			}
			phoneCursor.close();
		}
		mSort(listData);
	}

	// 集合拼音按照index排序
	private void mSort(List<People> list) {
		// TODO Auto-generated method stub
		Collections.sort(list, new Comparator<People>() {
			@Override
			public int compare(People arg0, People arg1) {

				char char0 = arg0.getIndex().charAt(0);
				char char1 = arg1.getIndex().charAt(0);
				if (char0 > char1)
					return 1;
				else
					return -1;
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// 在oncreate里面执行下面的代码没反应，因为oncreate里面得到的getHeight=0
		System.out
				.println("layoutIndex.getHeight()=" + layoutIndex.getHeight());
		height = layoutIndex.getHeight() / arr_index.length;
		getIndexView();
	}

	/** 绘制索引列表 */
	public void getIndexView() {
		LinearLayout.LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, height);
		// params.setMargins(10, 5, 10, 0);
		for (int i = 0; i < arr_index.length; i++) {
			final TextView tv = new TextView(this);
			tv.setLayoutParams(params);
			tv.setText(arr_index[i]);
			// tv.setTextColor(Color.parseColor("#606060"));
			// tv.setTextSize(16);
			tv.setPadding(10, 0, 10, 0);
			layoutIndex.addView(tv);
			layoutIndex.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					float y = event.getY();
					int index = (int) (y / height);
					if (index > -1 && index < arr_index.length) {// 防止越界
						String key = arr_index[index];
						if (adapter.getSelector().containsKey(key)) {
							int pos = adapter.getSelector().get(key);
							if (listView.getHeaderViewsCount() > 0) {// 防止ListView有标题栏，本例中没有。
								listView.setSelectionFromTop(
										pos + listView.getHeaderViewsCount(), 0);
							} else {
								listView.setSelectionFromTop(pos, 0);// 滑动到第一项
							}
							tv_show.setVisibility(View.VISIBLE);
							tv_show.setText(listData.get(listView
									.getFirstVisiblePosition()).getIndex()
									.substring(0, 1).toUpperCase());
						}
					}
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						layoutIndex.setBackgroundColor(Color
								.parseColor("#606060"));
						break;

					case MotionEvent.ACTION_MOVE:

						break;
					case MotionEvent.ACTION_UP:
						layoutIndex.setBackgroundColor(Color
								.parseColor("#00ffffff"));
						tv_show.setVisibility(View.INVISIBLE);
						break;
					}
					return true;
				}
			});
		}
	}

	/**
	 * 发送已选择的联系人
	 */
	class myOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			ArrayList<People> checkedList = new ArrayList<People>();
			for(int i =0;i<checkedPos.length;i++){
				if(checkedPos[i]){
					checkedList.add(listData.get(i));
				}
			}
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("CHECKED_LIST", checkedList);
			Intent in = new Intent();
			in.putExtras(bundle);
			setResult(RESULT_OK, in);
			finish();
		}

	}

}
