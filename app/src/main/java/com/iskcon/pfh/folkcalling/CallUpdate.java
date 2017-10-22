package com.iskcon.pfh.folkcalling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by i308830 on 6/24/17.
 */

public class CallUpdate {
    public String name,number;
    public static final List<SadhanaItem> ITEMS = new ArrayList<SadhanaItem>();
    public static final Map<String, SadhanaItem> ITEM_MAP = new HashMap<String, SadhanaItem>();
    public CallUpdate(String name,String number) {
        this.name = name;
        this.number = number;

    }

    public static class SadhanaItem {
        public final String name;
        public final String number;


        private static void addItem(SadhanaItem item) {
            ITEMS.add(item);
//            ITEM_MAP.put(item.Date, item.Date);

        }
        public SadhanaItem(String name,String number) {
            this.name = name;
            this.number = number;
            this.addItem(this);

        }
}

    public ArrayList<CallUpdate> getList() {

        ArrayList<CallUpdate> call = new ArrayList<>();
        return call;

    }
}
