package com.iskcon.pfh.folkcalling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by i308830 on 6/24/17.
 */

public class CallUpdate {
    public String updateDate, MA, SB, DA, Japa, RM;
    public static final List<SadhanaItem> ITEMS = new ArrayList<SadhanaItem>();
    public static final Map<String, SadhanaItem> ITEM_MAP = new HashMap<String, SadhanaItem>();
    public CallUpdate(String updateDate, String MA, String DA, String SB, String Japa, String RM ) {
        this.updateDate = updateDate;
        this.MA = MA;
        this.SB = SB;
        this.DA = DA;
        this.Japa = Japa;
        this.RM = RM;
    }

    public static class SadhanaItem {
        public final String Date;
        public final String MA;
        public final String DA;
        public final String SB;
        public final String Japa;

        private static void addItem(SadhanaItem item) {
            ITEMS.add(item);
//            ITEM_MAP.put(item.Date, item.Date);

        }
        public SadhanaItem(String Date, String MA, String DA, String SB, String Japa) {
            this.Date = Date;
            this.MA = MA;
            this.DA = DA;
            this.SB = SB;
            this.Japa = Japa;
            this.addItem(this);

        }
}

    public ArrayList<CallUpdate> getList() {

        ArrayList<CallUpdate> call = new ArrayList<>();
        return call;

    }
}
