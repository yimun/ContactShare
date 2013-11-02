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
	private TextView tv_show;// �м���ʾ������ı�
	private CheckBox checkAll;
	private Button btn_OK;

	/** ��ĸ������ */
	private String[] arr_index = { "#", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "U", "V", "W", "X",
			"Y", "Z" };

	private int height; // ��������ĸ߶�
	private List<People> listData; // ��ϵ���ܱ�
	private static boolean isCheckAll = false;
	public static boolean[] checkedPos; // λ���Ƿ�ѡ��

	/** ��ȡ��Phone���ֶ� **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };
	/** ��ϵ����ʾ���� **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
	/** �绰���� **/
	private static final int PHONES_NUMBER_INDEX = 1;
	/** ͷ��ID **/
	private static final int PHONES_PHOTO_ID_INDEX = 2;
	/** ��ϵ�˵�ID **/
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

		// ��ȡ��ϵ����Ϣ
		getPhoneContacts();

		adapter = new ContactListAdapter(this, listData, this.arr_index);
		listView.setAdapter(adapter);
		

		tv_show.setVisibility(View.INVISIBLE);
		checkedPos = new boolean[listData.size()];

		// ȫѡ�����
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
				// ˢ�����
				listView.setAdapter(adapter);
			}
		});
		btn_OK.setOnClickListener(new myOnClickListener());
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/** �õ��ֻ�ͨѶ¼��ϵ����Ϣ **/
	private void getPhoneContacts() {
		listData = new ArrayList<People>();
		ContentResolver resolver = this.getContentResolver();
		// ��ȡ�ֻ���ϵ��
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				People people = new People();
				// �õ��ֻ�����
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// ���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				// �õ���ϵ������
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);
				// �õ���ϵ��ID
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
				// �õ���ϵ��ͷ��ID
				Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
				// �õ���ϵ��ͷ��Bitamp
				Bitmap contactPhoto = null;
				// photoid ����0 ��ʾ��ϵ����ͷ�� ���û�и���������ͷ�������һ��Ĭ�ϵ�
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
				people.name = contactName;
				people.index = Pinyin4j.getHanyuPinyin(people.name).substring(
						0, 1)
						+ people.name.substring(0, 1);
				people.phone = phoneNumber;
				people.bitmap = contactPhoto;
				listData.add(people);
			}
			phoneCursor.close();
		}
		mSort(listData);
	}

	// ����ƴ������index����
	private void mSort(List<People> list) {
		// TODO Auto-generated method stub
		Collections.sort(list, new Comparator<People>() {
			@Override
			public int compare(People arg0, People arg1) {

				char char0 = arg0.index.charAt(0);
				char char1 = arg1.index.charAt(0);
				if (char0 > char1)
					return 1;
				else
					return -1;
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// ��oncreate����ִ������Ĵ���û��Ӧ����Ϊoncreate����õ���getHeight=0
		System.out
				.println("layoutIndex.getHeight()=" + layoutIndex.getHeight());
		height = layoutIndex.getHeight() / arr_index.length;
		getIndexView();
	}

	/** ���������б� */
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
					if (index > -1 && index < arr_index.length) {// ��ֹԽ��
						String key = arr_index[index];
						if (adapter.getSelector().containsKey(key)) {
							int pos = adapter.getSelector().get(key);
							if (listView.getHeaderViewsCount() > 0) {// ��ֹListView�б�������������û�С�
								listView.setSelectionFromTop(
										pos + listView.getHeaderViewsCount(), 0);
							} else {
								listView.setSelectionFromTop(pos, 0);// ��������һ��
							}
							tv_show.setVisibility(View.VISIBLE);
							tv_show.setText(listData.get(listView
									.getFirstVisiblePosition()).index
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
	 * ������ѡ�����ϵ��
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
