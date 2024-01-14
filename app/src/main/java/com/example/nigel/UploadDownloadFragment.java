package com.example.nigel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

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

public class UploadDownloadFragment extends Fragment {

    private static final String HEROKU_URL = "https://nigel-c0b396b99759.herokuapp.com/";
    private static final int PICK_EXCEL_REQUEST = 123;

    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ActivityResultLauncher<String> pickExcelLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_download_test, container, false);

        Button templateButton = view.findViewById(R.id.btnDownloadTemplate);
        Button uploadButton = view.findViewById(R.id.btnUploadData);
        Button allDataButton = view.findViewById(R.id.btnDownloadAllData);

        templateButton.setOnClickListener(v -> download("templateButton"));
        uploadButton.setOnClickListener(v -> uploadData());
        allDataButton.setOnClickListener(v -> download("allDataButton"));

        pickExcelLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                handleSelectedExcelFile(result);
            }
        });

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
        // Log the start of the uploadData method
        Log.d("UploadData", "uploadData: Launching pickExcelLauncher.");
        pickExcelLauncher.launch("*/*");
    }

    public void onActivityResult(Uri result) {
        if (result != null) {
            // Log the selected file URI
            Log.d("UploadData", "onActivityResult: Selected file URI: " + result);

            // Check if storage permissions are granted
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Permissions are granted, proceed with handling the selected file
                handleSelectedExcelFile(result);
            } else {
                // Permissions are not granted, request them
                Log.d("Permission", "Storage permission not granted. Requesting permission.");
                requestPermissionLauncher.launch(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
            }
        }
    }

    private void handleSelectedExcelFile(Uri selectedFile) {
        // Log the start of the handleSelectedExcelFile method
        Log.d("UploadData", "handleSelectedExcelFile: Handling selected file.");

        String excelFilePath = getFilePathFromDocumentFile(selectedFile);

        if (excelFilePath != null && !excelFilePath.isEmpty()) {
            File excelFile = new File(excelFilePath);

            // Log the file details before uploading
            Log.d("UploadData", "handleSelectedExcelFile: File to upload - Name: " + excelFile.getName() + ", Path: " + excelFile.getAbsolutePath());

            RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), excelFile);
            MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", excelFile.getName(), reqBody);

            uploadFile(partFile);
        } else {
            // Log an error if the file path is null or empty
            Log.e("UploadData", "handleSelectedExcelFile: Excel file path is null or empty.");
            Toast.makeText(requireContext(), "Error: Invalid file", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile(MultipartBody.Part partFile) {
        // Log the start of the uploadFile method
        Log.d("UploadData", "uploadFile: Uploading file.");

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
                    // Log the successful upload
                    Log.d("UploadData", "uploadFile: Excel File Uploaded");
                    Toast.makeText(requireContext(), "Excel File Uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    // Log an error if the upload fails
                    Log.e("UploadData", "uploadFile: Error uploading Excel file");
                    Toast.makeText(requireContext(), "Error uploading Excel file", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log a network error
                Log.e("UploadData", "uploadFile: Network error");
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getFilePath(Uri uri) {
        Log.d("UploadData", "getting filepath... from uri: "+uri);
        // Try to get file path using DocumentFile for non-media files
        String pathFromDocumentFile = getFilePathFromDocumentFile(uri);
        if (pathFromDocumentFile != null) {
            Log.d("UploadData", "excelFilePath from DocumentFile: " + pathFromDocumentFile);
            return pathFromDocumentFile;
        }

        // If it's a media file, try to get the file path using MediaStore
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            Log.d("UploadData", "excelFilePath inside getFilePath: " + filePath);
            return filePath;
        }
        else{
            Log.e("UploadData", "curser is null");
        }
        Log.e("UploadData", "Failed to get file path");
        return null;
    }
    private String getFilePathFromDocumentFile(Uri uri) {
        try {
            DocumentFile documentFile = DocumentFile.fromSingleUri(requireActivity(), uri);
            if (documentFile != null && documentFile.exists()) {
                String filePath = documentFile.getUri().getPath();
                // Log the file path
                Log.d("UploadData", "excelFilePath from DocumentFile: " + filePath);

                return filePath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                if (result.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                    // Permission granted, you can proceed with the excel pick
                    pickExcelLauncher.launch("*/*");
                } else {
                    Toast.makeText(requireContext(), "Storage permission denied", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void verifyStoragePermissions() {
        int permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            Log.d("Permission", "Storage permission not granted. Requesting permission.");
            requestPermissionLauncher.launch(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
        } else {
            // Permission is already granted
            Log.d("Permission", "Storage permission already granted. Launching pickExcelLauncher.");
            pickExcelLauncher.launch("*/*");
        }
    }
}

