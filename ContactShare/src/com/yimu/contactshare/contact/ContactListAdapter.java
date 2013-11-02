package com.yimu.contactshare.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yimu.contactshare.R;
import com.yimu.contactshare.bean.People;

/** ʵ��Filterable�ӿ�,��д���˹��� */
public class ContactListAdapter extends BaseAdapter {
	
	private static final boolean D = true;
	private static final String TAG  = "ContactListAdapter";
	private Context ctx;
	private ViewHolder holder;
	private List<People> list;
	private Map<String, Integer> selector;// ��ֵ�����������ĸ��ֵΪ��Ӧ��listview�е�λ��
	/** ��ĸ�� */
	private String arr_index[];



	/**
	 * ������
	 * 
	 * @param context
	 * @param list
	 * @param index
	 */
	public ContactListAdapter(Context context, List<People> list, String[] index) {
		this.ctx = context;
		this.list = list;
		this.arr_index = index;
		selector = new HashMap<String, Integer>();

		
		// ѭ����ĸ���ҳ�list�ж�Ӧ��ĸ���׸�λ��
		for (int j = 0; j < index.length; j++) {
			for (int i = 0; i < list.size(); i++) {
				String firstchar = list.get(i).index.substring(0, 1);
				if (firstchar.equals(index[j].toLowerCase())) {
					selector.put(index[j], i);
					break;
				}
			}
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(ctx).inflate(
						R.layout.note_item, null);
				holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
				holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
				holder.tv_mid_index = (TextView) convertView
						.findViewById(R.id.tv_index);
				holder.imView = (ImageView) convertView
						.findViewById(R.id.imageView1);
				holder.checkbox = (CheckBox) convertView
						.findViewById(R.id.checkBox1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			/** Ϊcheckbox�󶨼�������������λ�ã��������checkboxѡ���λ������������*/
			holder.checkbox
			.setOnCheckedChangeListener(new myCheckedChangeListener(
					position));
			
			// ������
			People item = list.get(position);
			holder.tv1.setText(item.name);
			holder.tv2.setText(item.phone);
			holder.imView.setImageBitmap(item.bitmap);
			if(ContactList.checkedPos[position]){
				holder.checkbox.setChecked(true);
			}else{
				holder.checkbox.setChecked(false);
			}
			// ��ʾindex
			String currentStr = item.index.substring(0, 1);
			// ��һ���index
			String previewStr = (position - 1) >= 0 ? list.get(position - 1).index
					.substring(0, 1) : " ";
			/**
			 * �ж��Ƿ���һ�εĴ���
			 */
			if (!previewStr.equals(currentStr)) {
				holder.tv_mid_index.setVisibility(View.VISIBLE);
				holder.tv_mid_index.setText(currentStr);// �н���ʾ���ı���ʾ��ǰ��������ĸ
			} else {
				holder.tv_mid_index.setVisibility(View.GONE);
			}
		} catch (OutOfMemoryError e) {
			Runtime.getRuntime().gc();
		} catch (Exception ex) {
			// handler.sendEmptyMessage(CommonMessage.PARSE_ERROR);
			ex.printStackTrace();
		}
		return convertView;
	}

	private class ViewHolder {
		TextView tv1;
		TextView tv2;
		// ������ĸ	
		TextView tv_mid_index;
		// ��ϵ��ͷ��
		ImageView imView;
		CheckBox checkbox;

	}

	/** ѡ�������� */
	class myCheckedChangeListener implements OnCheckedChangeListener {

		private int position;

		public myCheckedChangeListener(int position) {
			this.position = position;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked){
				ContactList.checkedPos[position] = true;
			}
			else {
				ContactList.checkedPos[position] = false;
			}
			
		}
	}

	public Map<String, Integer> getSelector() {
		return selector;
	}
/*
	public String[] getIndex() {
		return arr_index;
	}

	public void setIndex(String[] index) {
		this.arr_index = index;
	}*/
}
