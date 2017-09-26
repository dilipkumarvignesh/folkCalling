package com.iskcon.pfh.folkcalling;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.ListView;

/**
 * Created by i308830 on 9/26/17.
 */

public class ProgramSelect extends DialogFragment {
    public int[] selectedPrograms = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String[] items = {"Español", "Inglés", "Francés"};

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setTitle("Selección")
                .setMultiChoiceItems(items, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                Log.i("Dialogos", "Opción elegida: " + items[item]);
                                selectedPrograms[item] = 1;
                            }
                        });

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ListView list = ((android.app.AlertDialog) dialog).getListView();
                        //ListView has boolean array like {1=true, 3=true}, that shows checked items
                        //
                    }
                });
        return builder.create();
    }

    public int[] getSelectedPrograms()
    {
        return selectedPrograms;
    }
}

