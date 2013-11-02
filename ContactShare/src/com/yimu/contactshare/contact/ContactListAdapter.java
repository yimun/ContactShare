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

/** 实现Filterable接口,编写过滤规则 */
public class ContactListAdapter extends BaseAdapter {
	
	private static final boolean D = true;
	private static final String TAG  = "ContactListAdapter";
	private Context ctx;
	private ViewHolder holder;
	private List<People> list;
	private Map<String, Integer> selector;// 键值是索引表的字母，值为对应在listview中的位置
	/** 字母表 */
	private String arr_index[];



	/**
	 * 构造器
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

		
		// 循环字母表，找出list中对应字母的首个位置
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
			/** 为checkbox绑定监听必须在如下位置，否则将造成checkbox选项错位或者其他出错*/
			holder.checkbox
			.setOnCheckedChangeListener(new myCheckedChangeListener(
					position));
			
			// 绑定数据
			People item = list.get(position);
			holder.tv1.setText(item.name);
			holder.tv2.setText(item.phone);
			holder.imView.setImageBitmap(item.bitmap);
			if(ContactList.checkedPos[position]){
				holder.checkbox.setChecked(true);
			}else{
				holder.checkbox.setChecked(false);
			}
			// 显示index
			String currentStr = item.index.substring(0, 1);
			// 上一项的index
			String previewStr = (position - 1) >= 0 ? list.get(position - 1).index
					.substring(0, 1) : " ";
			/**
			 * 判断是否上一次的存在
			 */
			if (!previewStr.equals(currentStr)) {
				holder.tv_mid_index.setVisibility(View.VISIBLE);
				holder.tv_mid_index.setText(currentStr);// 中奖提示的文本显示当前滑动的字母
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
		// 索引字母	
		TextView tv_mid_index;
		// 联系人头像
		ImageView imView;
		CheckBox checkbox;

	}

	/** 选择框监听器 */
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
