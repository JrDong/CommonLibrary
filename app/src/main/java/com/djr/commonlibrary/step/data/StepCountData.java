package com.djr.commonlibrary.step.data;


import com.djr.commonlibrary.main.UserInfoCenter;
import com.djr.commonlibrary.step.StepDBModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by DongJr on 2016/5/30.
 */
public class StepCountData {


    public static List<StepDBModel> getData() {
        StepDBManager manager = StepDBManager.getIns();
//        stepDBManager.writeStepToDb();
        ArrayList<StepDBModel> monthData = manager.getMonthData();
        LinkedHashMap<String, StepDBModel> map = new LinkedHashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long currentTime = System.currentTimeMillis();
        int dateCount = manager.getDateCount();
        for (int i = dateCount; i >= 0; i--) {
            Date d = new Date(currentTime - i * (1000L * 60 * 60 * 24));
            StepDBModel model = new StepDBModel();
            model.setUserid(UserInfoCenter.getUserId());
            model.setStepCount(0);
            model.setDate(dateFormat.format(d));
            map.put(dateFormat.format(d), model);
        }
        Set key = map.keySet();
        for (StepDBModel stepDBModel : monthData) {
            if (key.contains(stepDBModel.getDate())) {
                map.put(stepDBModel.getDate(), stepDBModel);
            }
        }
        ArrayList<StepDBModel> newMonthDate = new ArrayList<>();
        newMonthDate.addAll(map.values());
        return newMonthDate;
    }
}
