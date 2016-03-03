package com.xuf.www.gobang.util;

import com.xuf.www.gobang.bean.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenov0 on 2016/3/3.
 */
public class OperationQueue {

    private List<Point> mOperations = new ArrayList<>();

    public void clear() {
        mOperations.clear();
    }

    public void addOperation(Point point) {
        mOperations.add(point);
    }

    public void removeLastOperation(){
        mOperations.remove(mOperations.size() - 1);
    }

    public Point getLastOperation(){
        if (!mOperations.isEmpty()) {
            return mOperations.get(mOperations.size() - 1);
        } else {
            return null;
        }
    }
}
