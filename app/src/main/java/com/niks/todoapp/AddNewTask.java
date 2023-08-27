package com.niks.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.niks.todoapp.Model.ToDoModel;
import com.niks.todoapp.Utils.DataBaseHelper;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";

    private EditText edit_text;
    private Button save_task;
    private DataBaseHelper myDb;
    public static AddNewTask newInstance(){
        return  new AddNewTask();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_newtask, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edit_text = view.findViewById(R.id.edit_text);
        save_task = view.findViewById(R.id.save_task);
        myDb = new DataBaseHelper(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if(bundle !=null){
            isUpdate =true;
            String task = bundle.getString("task");
            edit_text.setText(task);

            if(task.length()>0){
                save_task.setEnabled(false);
            }
        }
        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    save_task.setEnabled(false);
                    save_task.setBackgroundColor(Color.GRAY);
                }else{
                    save_task.setEnabled(true);
                    save_task.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        save_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edit_text.getText().toString();
                if (text.equals("")) {
                    Toast.makeText(getActivity(), "Enter the task details first!", Toast.LENGTH_SHORT).show();
                }else{
                    if (finalIsUpdate) {
                        myDb.updateTask(bundle.getInt("id"), text);
                    } else {
                        ToDoModel item = new ToDoModel();
                        item.setTask(text);
                        item.setStatus(0);
                        myDb.insertTask(item);
                    }
                }

                dismiss();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }
}


