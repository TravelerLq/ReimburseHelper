package com.sas.rh.reimbursehelper.Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Bean.PersonnameAndHeadimageEntity;
import com.sas.rh.reimbursehelper.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectMembersSelectGridviewAdpter extends BaseAdapter {
	//private int[] image;
	private boolean isChice[];
	private Context context;
	private PersonnameAndHeadimageEntity pnhe;
	private List<PersonnameAndHeadimageEntity> members;
    private Map<String,PersonnameAndHeadimageEntity> selectedmembers = new  HashMap<String,PersonnameAndHeadimageEntity>();

	public ProjectMembersSelectGridviewAdpter(List<PersonnameAndHeadimageEntity> members, Context context) {
		this.members = members;
		Log.i("hck", members.size()+"lenght");
		isChice=new boolean[members.size()];
		for (int i = 0; i < members.size(); i++) {
			isChice[i]=false;
		}
		this.context = context;
	}

	@Override
	public int getCount() {
		return members.size();
	}

	@Override
	public Object getItem(int arg0) {
		return members.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

    public Map<String,PersonnameAndHeadimageEntity> getSelectedMembers() {
        return selectedmembers;
    }
	//初始化组件
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view = arg1;
		GetView getView=null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_select_project_member, null);
			getView = new GetView();
			getView.imageView=(ImageView) view.findViewById(R.id.image_item);
            getView.username_item=(TextView) view.findViewById(R.id.username_item);
			view.setTag(getView);
		} else {
			getView = (GetView) view.getTag();
		}
		getView.imageView.setImageDrawable(getView(arg0));
        getView.username_item.setText(members.get(arg0).getPname());

		
		return view;
	}
	//定义组件
	static class GetView {
		ImageView imageView;
		TextView username_item;
	}

	private LayerDrawable getView(int post) {
		
		Bitmap bitmap = ((BitmapDrawable)context.getResources().getDrawable(Integer.parseInt(members.get(post).getImagepath()))).getBitmap();
		Bitmap bitmap2 = null;
		LayerDrawable la = null;
		if (isChice[post]== true){   
			bitmap2 = BitmapFactory.decodeResource(context.getResources(),
					R.mipmap.editable_mode_checked_tag);
		}
		if (bitmap2!=null) {
			//pnhe
			Drawable[] array = new Drawable[2];
			array[0] = new BitmapDrawable(bitmap);
			array[1] = new BitmapDrawable(bitmap2);
			la= new LayerDrawable(array);
			la.setLayerInset(0, 0, 0, 0, 0);
			la.setLayerInset(1, 0, 120, 120, 0);

		}
		else {
			Drawable[] array = new Drawable[1];
			array[0] = new BitmapDrawable(bitmap);
			la= new LayerDrawable(array);
			la.setLayerInset(0, 0, 0, 0, 0);

		}
		return la;
	}
	public void chiceState(int post)
	{
		isChice[post]=isChice[post]==true?false:true;
		if(isChice[post]==true){
            selectedmembers.put(""+post,members.get(post));
        }else{
            selectedmembers.remove(""+post);
        }
		this.notifyDataSetChanged();
	}
}
