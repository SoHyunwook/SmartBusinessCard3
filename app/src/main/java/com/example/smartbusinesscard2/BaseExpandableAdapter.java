package com.example.smartbusinesscard2;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yi_te on 2016-11-30.
 */

public class BaseExpandableAdapter extends BaseExpandableListAdapter{

    private ArrayList<String> groupList = null;
    private ArrayList<ArrayList<String>> childList = null;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;

    public BaseExpandableAdapter(Context c, ArrayList<String> groupList,
                                 ArrayList<ArrayList<String>> childList){
        super();
        this.inflater = LayoutInflater.from(c);
        this.groupList = groupList;
        this.childList = childList;
    }

    // 그룹 포지션을 반환한다.
    @Override
    public String getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    // 그룹 사이즈를 반환한다.
    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    // 그룹 ID를 반환한다.
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // 그룹뷰 각각의 ROW
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.list_head, parent, false);
            viewHolder.tv_title = (TextView) v.findViewById(R.id.title);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        // 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
        if(isExpanded){
//            viewHolder.checkBox.setChecked(true);
            //          viewHolder.iv_image.setBackgroundColor(Color.GREEN);
        }else{
//            viewHolder.checkBox.setChecked(false);
            //         viewHolder.iv_image.setBackgroundColor(Color.WHITE);
        }

        viewHolder.tv_title.setText(getGroup(groupPosition));

        return v;
    }

    // 차일드뷰를 반환한다.
    @Override
    public String getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    // 차일드뷰 사이즈를 반환한다.
    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    // 차일드뷰 ID를 반환한다.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // 차일드뷰 각각의 ROW
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.list_item, null);
            viewHolder.tv_name = (TextView) v.findViewById(R.id.s_name);
//            viewHolder.i_checkBox = (CheckBox) v.findViewById(R.id.i_checkBox);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        viewHolder.tv_name.setText(getChild(groupPosition, childPosition));
        //viewHolder.tv_mail.setText(getChild(groupPosition, childPosition));

        return v;
    }

    @Override
    public boolean hasStableIds() {   return true; }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }

    class ViewHolder {
        public CheckBox t_checkBox;
        public CheckBox i_checkBox;
        // public ImageView iv_image;
        public TextView tv_title;
        public TextView tv_name;
        public TextView tv_mail;
    }

}