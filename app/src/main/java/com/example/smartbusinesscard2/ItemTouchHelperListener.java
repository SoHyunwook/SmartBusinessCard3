package com.example.smartbusinesscard2;

/**
 * Created by 현욱 on 2016-11-28.
 */
public interface ItemTouchHelperListener {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemRemove(int position);
}
