package com.example.nigel;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.nigel.Api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadDownloadFragment extends Fragment implements ActivityResultCallback<Uri> {

    private static final String HEROKU_URL = "https://nigel-c0b396b99759.herokuapp.com/";
    private static final int PICK_EXCEL_REQUEST = 123;

    private ActivityResultLauncher<String> pickExcelLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    handleSelectedExcelFile(uri);
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_download_test, container, false);

        Button templateButton = view.findViewById(R.id.btnDownloadTemplate);
        Button uploadButton = view.findViewById(R.id.btnUploadData);
        Button allDataButton = view.findViewById(R.id.btnDownloadAllData);

        templateButton.setOnClickListener(v -> download("templateButton"));
        uploadButton.setOnClickListener(v -> uploadData());
        allDataButton.setOnClickListener(v -> download("allDataButton"));

        return view;
    }


    private void download(String buttonId){
        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HEROKU_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api= retrofit.create(Api.class);
        // API call
        Call<ResponseBody> call;
        String successText;
        String errorText;
        String fileName;


        if (buttonId.equals("templateButton")) {
            call = api.downloadTemplate();
            successText = "Template downloaded successfully";
            errorText = "Error downloading template";
            fileName = "upload_template.xlsx";
        } else if (buttonId.equals("allDataButton")) {
            call = api.downloadAllData();
            successText = "All data downloaded successfully";
            errorText = "Error downloading all the data";
            fileName = "all_data.xlsx";
        } else {
            // Handle unknown button
            return;
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Save the downloaded template to external storage
                    saveFileToExternalStorage(response.body(), fileName);

                    // Toast indicating the file has successfully been downloaded
                    Toast.makeText(getContext(), successText, Toast.LENGTH_SHORT).show();
                } else {
                    // Handle error
                    Toast.makeText(getContext(), errorText, Toast.LENGTH_SHORT).show();
                }
            }

            private void saveFileToExternalStorage(ResponseBody body, String filename) {
                try {
                    File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Nigel");

                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    File file = new File(dir, filename);
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(body.bytes());
                    fos.close();
                    MediaScannerConnection.scanFile(
                            getContext(),
                            new String[]{file.getAbsolutePath()},
                            null,
                            null
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure, e.g., network issues
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadData() {
        pickExcelLauncher.launch("*/*");
    }

    @Override
    public void onActivityResult(Uri result) {
        if (result != null) {
            handleSelectedExcelFile(result);
        }
    }

    private void handleSelectedExcelFile(Uri selectedFile) {
        String excelFilePath = getFilePath(selectedFile);

        if (excelFilePath != null && !excelFilePath.isEmpty()) {
            File excelFile = new File(excelFilePath);
            RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), excelFile);
            MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", excelFile.getName(), reqBody);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(HEROKU_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Api api = retrofit.create(Api.class);

            Call<ResponseBody> upload = api.uploadExcelFile(partFile);
            upload.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Excel File Uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Error uploading Excel file", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String getFilePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }
        return null;
    }

}

