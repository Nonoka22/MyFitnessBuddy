package com.example.myfitnessbuddy.fragments.dialogs;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.interfaces.APIService;
import com.example.myfitnessbuddy.interfaces.OnDeleteListItemClicked;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.adapters.CriteriaAdapter;
import com.example.myfitnessbuddy.adapters.EditListAdapter;
import com.example.myfitnessbuddy.databinding.DialogEditDataBinding;
import com.example.myfitnessbuddy.utils.Constants;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class EditDataDialog extends DialogFragment implements OnDeleteListItemClicked {

    private String data;
    private String dataType;
    private List<String> dataList = new ArrayList<>();
    private EditListAdapter adapter;
    private List<String> trainerTypes = new ArrayList<>();
    private List<String> helperList = new ArrayList<>();
    private ArrayAdapter<String> dataListAdapter;

    private APIService.OnUpdateClickedListener listener;

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private EditText dataEditText;
    private LinearLayout priceLL, listDataLL, profPicLL;
    private ImageView profilePicImgView;
    private Button chooseImageButton, dismissButton;
    private Spinner editSpinner, specialtySpinner;
    private RadioGroup radioGroup;
    private RadioButton yesButton, noButton;
    private RecyclerView recyclerView, criteriasRecyclerView;
    private TextView errorMessage;
    private String imageUrl;
    protected Uri imageUri;
    private ProgressBar progressBar;


    public EditDataDialog() {
    }

    public EditDataDialog(String dataType, APIService.OnUpdateClickedListener listener) {
        this.dataType = dataType;
        this.listener = listener;
    }

    public EditDataDialog(String data, String dataType, APIService.OnUpdateClickedListener listener) {
        this.data = data;
        this.dataType = dataType;
        this.listener = listener;
    }

    public EditDataDialog(String dataType, List<String> dataList, APIService.OnUpdateClickedListener listener) {
        this.dataType = dataType;
        this.dataList = dataList;
        this.listener = listener;
    }

    public EditDataDialog(String dataType, List<String> dataList, List<String> helperList, APIService.OnUpdateClickedListener listener) {
        this.dataType = dataType;
        this.dataList = dataList;
        this.helperList = helperList;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogEditDataBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.dialog_edit_data, container, false);
        View view = binding.getRoot();

        setCancelable(false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference("Images");

        progressBar = binding.picProgressBar;
        errorMessage = binding.errorMessageEditData;
        TextView textTV = binding.editDataText;
        criteriasRecyclerView = binding.recyclerViewCriterias;
        criteriasRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        specialtySpinner = binding.editSpecialtySpinner;
        yesButton = binding.editNeedNutriButton;
        noButton = binding.editDontNeedNutriButton;
        radioGroup = binding.editNutriRadioGroup;
        dismissButton = binding.dismissDialogButton;
        editSpinner = binding.editSpinner;
        priceLL = binding.priceLL;
        listDataLL = binding.listDataLL;
        profPicLL = binding.editProfilePic;
        chooseImageButton = binding.editChooseImgButton;
        profilePicImgView = binding.editProfPicImageView;
        Button updateButton = binding.updateDialogButton;
        dataEditText = binding.fieldEditData;
        NumberPicker hourPicker = binding.editHourNumberPicker;
        EditText costEditText = binding.editCostEditText;
        recyclerView = binding.recyclerViewEditList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Button addListItemButton = binding.editAddItemButton;
        trainerTypes.add("Group Instructor");
        trainerTypes.add("Personal Trainer");

        switch (dataType){
            case Constants.UPDATE_FIRST_NAME:
                dataEditText.setVisibility(View.VISIBLE);
                dataEditText.setHint(data);
                textTV.setText("Update your first name.");
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!dataEditText.getText().toString().isEmpty()){
                            //save data to database..
                            listener.firstNameUpdated(dataEditText.getText().toString());
                            dismiss();
                        }
                        else {
                            errorMessage.setText("You must enter a name.");
                            errorMessage.setVisibility(View.VISIBLE);
                        }

                    }
                });
                break;
            case Constants.UPDATE_LAST_NAME:
                dataEditText.setVisibility(View.VISIBLE);
                dataEditText.setHint(data);
                textTV.setText("Update your last name.");
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!dataEditText.getText().toString().isEmpty()){
                            //save data to database..
                            listener.lastNameUpdated(dataEditText.getText().toString());
                            dismiss();
                        }
                        else {
                            errorMessage.setText("You must enter a name.");
                            errorMessage.setVisibility(View.VISIBLE);
                        }

                    }
                });
                break;
            case Constants.UPDATE_GOAL:
                editSpinner.setVisibility(View.VISIBLE);
                textTV.setText("Update your goal.");

                dataListAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, dataList);
                dataListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editSpinner.setAdapter(dataListAdapter);

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get data from Spinner
                        String selected = editSpinner.getSelectedItem().toString();
                        //save data to database..
                        listener.goalUpdated(selected);
                        dismiss();
                    }
                });
                break;
            case Constants.UPDATE_CITY:
                editSpinner.setVisibility(View.VISIBLE);
                textTV.setText("Update your city.");

                dataListAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, dataList);
                dataListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editSpinner.setAdapter(dataListAdapter);

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get data from Spinner
                        String selected = editSpinner.getSelectedItem().toString();
                        //save data to database..
                        listener.cityUpdated(selected);
                        dismiss();
                    }
                });
                break;
            case Constants.UPDATE_GYM:
                editSpinner.setVisibility(View.VISIBLE);
                //must get data according to city
                //a data nem a gym-et fogja tartalmazni, hanem a city-t.
                textTV.setText("Update your gym.");

                dataListAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, dataList);
                dataListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editSpinner.setAdapter(dataListAdapter);

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.gymUpdated(editSpinner.getSelectedItem().toString());
                        dismiss();
                    }
                });
                break;
            case Constants.UPDATE_NUTRI_NEEDED:
                radioGroup.setVisibility(View.VISIBLE);
                textTV.setText("Do you need nutritionist?");

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ha megegyezik a kapott infoval, ne csinaljon semmit
                        if(yesButton.isChecked()){
                            if(data.equals("false")){
                                listener.nutriNeededUpdated(true);
                            }
                        }
                        else if(noButton.isChecked()){
                            if(data.equals("true")){
                                listener.nutriNeededUpdated(false);
                            }
                        }
                        dismiss();
                    }
                });
                break;
            case Constants.UPDATE_INTRODUCTION:
                dataEditText.setVisibility(View.VISIBLE);
                dataEditText.setHint(data);
                textTV.setText("Update your introduction.");
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!dataEditText.getText().toString().isEmpty()){
                            //save data to database..
                            listener.introductionUpdated(dataEditText.getText().toString());
                            dismiss();
                        }
                        else {
                            errorMessage.setText("You must enter a name.");
                            errorMessage.setVisibility(View.VISIBLE);
                        }
                    }
                });
                break;
            case Constants.UPDATE_PRICE:
                priceLL.setVisibility(View.VISIBLE);
                textTV.setText("Update the price information.");

                hourPicker.setMinValue(0);
                hourPicker.setMaxValue(5);
                String[] pickerValues = new String[]{"0.5", "1", "1.5", "2", "2.5", "3"};
                hourPicker.setDisplayedValues(pickerValues);

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!costEditText.getText().toString().isEmpty()){
                            errorMessage.setVisibility(View.GONE);
                            //save data to database..
                            listener.priceUpdated(pickerValues[hourPicker.getValue()],costEditText.getText().toString());
                            dismiss();
                        }
                        else{
                            errorMessage.setText("You must provide the amount of money..");
                            errorMessage.setVisibility(View.VISIBLE);
                        }

                    }
                });
                break;
            case Constants.UPDATE_SPECIALTY_LIST:
                listDataLL.setVisibility(View.VISIBLE);
                textTV.setText("Update the specialty list.");


                //set adapter... with the list
                adapter = new EditListAdapter(dataList,"SPECIALTY",EditDataDialog.this);
                recyclerView.setAdapter(adapter);

                for(String s : dataList){
                    helperList.remove(s);
                }

                //and when I remove an item from the list, I add it to the specialties list..

                dataListAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, helperList);
                dataListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                specialtySpinner.setAdapter(dataListAdapter);

                addListItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //add data from spinner to the list
                        if(specialtySpinner != null && specialtySpinner.getSelectedItem() != null){
                            errorMessage.setVisibility(View.GONE);
                            String specialty = specialtySpinner.getSelectedItem().toString();
                            dataList.add(specialty);
                            helperList.remove(specialty);
                            //update listview
                            dataListAdapter.notifyDataSetChanged();
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            errorMessage.setText("There is nothing to add to the list..");
                            errorMessage.setVisibility(View.VISIBLE);
                        }
                    }
                });

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //check if list has 5 or less items, and it must have at least one. max 5.
                        if(adapter.getItemCount() == 0){
                            //set error message
                            errorMessage.setText("You must add at least one specialty.");
                            errorMessage.setVisibility(View.VISIBLE);
                        }
                        else if(adapter.getItemCount() > 5){
                            //error message
                            errorMessage.setText("You can select max 5 specialties.");
                            errorMessage.setVisibility(View.VISIBLE);
                        }
                        else {
                            //save updated list to database
                            listener.specialtiesUpdated(dataList);
                            dismiss();
                        }
                    }
                });
                break;
            case Constants.UPDATE_TRAINER_TYPE:
                listDataLL.setVisibility(View.VISIBLE);
                textTV.setText("Update the trainer type list.");

                //Log.i("Noemi","Datalist: " + dataList.toString());
                //set adapter... with the list
                adapter = new EditListAdapter(dataList,"TRAINER TYPE",EditDataDialog.this);
                recyclerView.setAdapter(adapter);

                for(String s : dataList){
                    trainerTypes.remove(s);
                }

                //and when I remove an item from the list, I add it to the trainerTypes list..

                dataListAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, trainerTypes);
                dataListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                specialtySpinner.setAdapter(dataListAdapter);

                addListItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //add data from spinner to the list
                        if(specialtySpinner != null && specialtySpinner.getSelectedItem() != null){
                            errorMessage.setVisibility(View.GONE);
                            String tType = specialtySpinner.getSelectedItem().toString();
                            dataList.add(tType);
                            trainerTypes.remove(tType);
                            //update listview
                            dataListAdapter.notifyDataSetChanged();
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            errorMessage.setText("There is nothing to add to the list..");
                            errorMessage.setVisibility(View.VISIBLE);
                        }
                    }
                });

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //at least one has to be selected
                        if(adapter.getItemCount() == 0){
                            errorMessage.setText("You must add at least one trainer type");
                            errorMessage.setVisibility(View.VISIBLE);
                        }
                        else{
                            Log.i("Noemi","save updated list to database");
                            //save updated list to database
                            listener.trainerTypeUpdated(dataList);
                            dismiss();
                        }

                    }
                });
                break;
            case Constants.UPDATE_PROFILE_PIC:
                profPicLL.setVisibility(View.VISIBLE);
                textTV.setText("Update your profile picture");

                chooseImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //select new picture and set imageView
                        openFileChooser();
                    }
                });

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadImages(imageUri,progressBar);
                        //delete previous picture from database
                        //set user's imageURL in database to the new one..
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listener.profilePicUpdated(imageUrl,data);
                                dismiss();
                            }
                        },4000);

                    }
                });
                break;
            case Constants.UPDATE_CRITERIA_LIST:
                criteriasRecyclerView.setVisibility(View.VISIBLE);
                textTV.setText("Update the criteria list.");

                CriteriaAdapter criteriaAdapter = new CriteriaAdapter(dataList);

                criteriasRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                criteriasRecyclerView.setAdapter(criteriaAdapter);

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(criteriasRecyclerView);

                //set adapter... with the list
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //save updated list to database
                        listener.criteriaUpdated(dataList);
                        dismiss();
                    }
                });
                break;
        }

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START
            | ItemTouchHelper.END,0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(dataList,fromPosition,toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);

            //Log.i("Noemi",criterias.toString());
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    @Override
    public void editItemTrashClicked(int position, String listType) {
        if(listType.equals("TRAINER TYPE")){
            trainerTypes.add(dataList.get(position));
        }
        else if(listType.equals("SPECIALTY")){
            helperList.add(dataList.get(position));
        }
        dataList.remove(position);
        dataListAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    protected String getFileExtension(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    protected void uploadImages(Uri imageUri, final ProgressBar progressBar){
        if(imageUri != null){
            final StorageReference storageRef = storageReference.child(System.currentTimeMillis()+ "." + getFileExtension(imageUri));
            StorageTask<UploadTask.TaskSnapshot> uploadTask = storageRef.putFile(imageUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {

                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        Log.i("Noemi", "Uploading image successful: " + downloadUri);
                        imageUrl = downloadUri.toString();
                    }
                    else {
                        Log.i("Noemi", "Error uploading image: ");

                    }
                }
            });

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Noemi","Here I am. req");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            profilePicImgView.setImageURI(imageUri);
        }

    }


}
