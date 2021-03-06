package th.in.nattawut.plancrop.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import th.in.nattawut.plancrop.HomeActivity;
import th.in.nattawut.plancrop.R;
import th.in.nattawut.plancrop.utility.DeleteFammer;
import th.in.nattawut.plancrop.utility.EditRegister;
import th.in.nattawut.plancrop.utility.GetData;
import th.in.nattawut.plancrop.utility.Myconstant;
import th.in.nattawut.plancrop.utility.RegisterViewAdpter;

public class RegisterViewFragment extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //Create Toolbal
        CreateToolbal();

        //CreateLisView
        createLisView();

        //Swipe Refresh Layout
        swipeRefreshLayout();
    }
    private void swipeRefreshLayout() {
        mSwipeRefreshLayout = getView().findViewById(R.id.swiRefreshLayouRegister);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Create ListView
                        createLisView();

                    }
                },2);
            }
        });
    }


    private void createLisView() {
        final ListView listView = getView().findViewById(R.id.listViewRegister);
        Myconstant myconstant = new Myconstant();
        String[] columString = myconstant.getComlumRegisterString();

        try {
            GetData getData = new GetData(getActivity());
            getData.execute(myconstant.getUrlGetRegister());

            String jsonString = getData.get();
            Log.d("19jan","JSon register ==> "+ jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);

            final String[] userString = new String[jsonArray.length()];
            final String[] passwordString = new String[jsonArray.length()];
            final String[] nameString = new String[jsonArray.length()];
            final String[] idString = new String[jsonArray.length()];
            final String[] addressString = new String[jsonArray.length()];
            final String[] vidString = new String[jsonArray.length()];
            final String[] sidString = new String[jsonArray.length()];
            final String[] phonString = new String[jsonArray.length()];
            final String[] emailString = new String[jsonArray.length()];
            final String[] midString = new String[jsonArray.length()];

            for (int i=0; i<jsonArray.length(); i+=1){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    userString[i] = jsonObject.getString(columString[1]);
                    passwordString[i] = jsonObject.getString(columString[2]);
                    nameString[i] = jsonObject.getString(columString[3]);
                    idString[i] = jsonObject.getString(columString[4]);
                    addressString[i] = jsonObject.getString(columString[5]);
                    vidString[i] = jsonObject.getString(columString[6]);
                    sidString[i] = jsonObject.getString(columString[7]);
                    phonString[i] = jsonObject.getString(columString[8]);
                    emailString[i] = jsonObject.getString(columString[9]);
                    midString[i] = jsonObject.getString(columString[0]);
            }
            final RegisterViewAdpter registerAdpter = new RegisterViewAdpter(getActivity(),
                    userString, passwordString, nameString, idString, addressString,vidString,sidString, phonString, emailString);
            listView.setAdapter(registerAdpter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    deleteorEditRegister(midString[position],userString[position],passwordString[position],nameString[position],
                            idString[position],addressString[position],vidString[position],sidString[position],phonString[position],emailString[position]);

                }
            });
            mSwipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    //alertให้เลือกลบหรือแก้ไข
    private void deleteorEditRegister(final String midString,
                                      final String userString,
                                      final String passwordString,
                                      final String nameString,
                                      final String idString,
                                      final String addressString,
                                      final String vidString,
                                      final String sidString,
                                      final String phonString,
                                      final String emailString) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_action_draweruser);
        builder.setTitle("ข้อมูลสมาชิก");
        builder.setMessage("กรุณาเลือก ลบ หรือ ดูข้อมูล ?");
        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("ลบ" ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteRegister(midString);
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("ดูข้อมูล", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editRegister(midString,userString,passwordString,nameString,idString,addressString,vidString,sidString,phonString,emailString);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    //alertให้เลือกจะลบรายการหรือไม่
    private void deleteRegister(final String midString){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("ต้องการลบรายการนี้หรือไม่?");
        builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editDeleteRegister(midString);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    //แก้ไขประเทพืชเพาะปลูก
    private void editRegister(final String midString,
                              final String userString,
                              final String passwordString,
                              final String nameString,
                              final String idString,
                              final String addressString,
                              final String vidString,
                              final String sidString,
                              final String phonString,
                              final String emailString){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("ข้อมูลส่วนตัว");

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.edit_register, null);

        EditText EditEdtUsername = view.findViewById(R.id.EditEdtUsername);
        String newUsername = getActivity().getIntent().getExtras().getString("UserID",userString);
        EditEdtUsername.setText(newUsername);

        EditText EditEdtPassword = view.findViewById(R.id.EditEdtPassword);
        String newPassword = getActivity().getIntent().getExtras().getString("PWD",passwordString);
        EditEdtPassword.setText(newPassword);

        EditText EditEdtName = view.findViewById(R.id.EditEdtName);
        String newName = getActivity().getIntent().getExtras().getString("Name",nameString);
        EditEdtName.setText(newName);

        EditText EditEdtId = view.findViewById(R.id.EditEdtId);
        String newId = getActivity().getIntent().getExtras().getString("ID",idString);
        EditEdtId.setText(newId);

        EditText EditEdtAddress = view.findViewById(R.id.EditEdtAddress);
        String newAddress = getActivity().getIntent().getExtras().getString("Address",addressString);
        EditEdtAddress.setText(newAddress);

        EditText EditEdtPhone = view.findViewById(R.id.EditEdtPhone);
        String newPhone = getActivity().getIntent().getExtras().getString("Tel",phonString);
        EditEdtPhone.setText(newPhone);

        EditText EditEdtEmail = view.findViewById(R.id.EditEdtEmail);
        String newEmail = getActivity().getIntent().getExtras().getString("EMail",emailString);
        EditEdtEmail.setText(newEmail);


        builder.setView(view);

        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("แก้ไข", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText editText = view.findViewById(R.id.EditEdtUsername);
                String newUsername = editText.getText().toString();

                EditText EditEdtPassword = view.findViewById(R.id.EditEdtPassword);
                String newPassword = EditEdtPassword.getText().toString();


                EditText EditEdtName = view.findViewById(R.id.EditEdtName);
                String newName = EditEdtName.getText().toString();

                EditText EditEdtId = view.findViewById(R.id.EditEdtId);
                String newID = EditEdtId.getText().toString();

                EditText EditEdtAddress = view.findViewById(R.id.EditEdtAddress);
                String newAddress = EditEdtAddress.getText().toString();

                EditText EditEdtPhone = view.findViewById(R.id.EditEdtPhone);
                String newPhone = EditEdtPhone.getText().toString();

                EditText EditEdtEmail = view.findViewById(R.id.EditEdtEmail);
                String newEmail = EditEdtEmail.getText().toString();

                //if (newUsername.isEmpty() || newPassword.isEmpty() || newName.isEmpty() || newID.isEmpty() || newAddress.isEmpty() || newPhone.isEmpty() || newEmail.isEmpty()) {

                //}
                updateRegister(midString,newUsername,newPassword,newName,newID,newAddress,newPhone,newEmail);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    //updateข้อมูลประเภทพืชเพาะปลูก
    private void updateRegister(String midString, String newUsername ,String newPassword, String newName, String newID, String newAddress, String newPhone, String newEmail){
    //private void updateRegister(String midString,String newName){

        Myconstant myconstant = new Myconstant();

        try {
            EditRegister editRegister = new EditRegister(getActivity());
             editRegister.execute(midString,newUsername,newPassword,newName,newID,newAddress,newPhone,newEmail,
            //editRegister.execute(midString,newName,

                    myconstant.getUrlEditRegister());

            if (Boolean.parseBoolean(editRegister.get())) {

            }else {
                Toast.makeText(getActivity(),"แก้ไขข้อมูลสำเร็จ",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //ลบรายการประเภทพืชเพาะปลูก
    private void editDeleteRegister(String midString){

        Myconstant myconstant = new Myconstant();
        try {
            DeleteFammer deleteFammer = new DeleteFammer(getActivity());
            deleteFammer.execute(midString, myconstant.getUrlDeleteFammer());

            if (Boolean.parseBoolean(deleteFammer.get())) {
                createLisView();
            } else {
                Toast.makeText(getActivity(),"ลบรายการพืชเพาะปลูก",Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itemupload) {
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    /*getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contentHomeFragment, new RegisterFragment())
                            .addToBackStack(null)
                            .commit();*/
                    return false;
                }
            });

            // uploadValueToSever();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.manu_register, menu);

    }

    private void CreateToolbal() {
        Toolbar toolbar = getView().findViewById(R.id.toolbarRegister);
        ((HomeActivity)getActivity()).setSupportActionBar(toolbar);

        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("ข้อมูลสมาชิก");

        ((HomeActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frm_view_register,container, false);
        return view;
    }
}

