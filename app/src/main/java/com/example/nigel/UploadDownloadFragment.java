package com.example.nigel;

import android.Manifest;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        BabyApi api= retrofit.create(BabyApi.class);
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

    private void handleSelectedExcelFile(Uri selectedFile) {
        // Log the start of the handleSelectedExcelFile method
        Log.d("UploadData", "handleSelectedExcelFile: Handling selected file.");
        Log.d("UploadData", "uri: "+selectedFile);

        String excelFilePath = getFilePathFromDocumentFile(selectedFile);

        File testFile = new File(excelFilePath);

        if (testFile.exists() && isExcelFile(testFile)) {
            // Log that the file exists and is a valid Excel file
            Log.d("UploadData", "File exists and is a valid Excel file");

            // Log the file details before uploading
            Log.d("UploadData", "handleSelectedExcelFile: File to upload - Name: " + testFile.getName() + ", Path: " + testFile.getAbsolutePath());

            RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), testFile);
            MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", testFile.getName(), reqBody);

            uploadFile(partFile,excelFilePath);
        } else {
            // Log an error if the file does not exist or is not a valid Excel file
            Log.e("UploadData", "File does not exist at the specified path or is not a valid Excel file");
            Toast.makeText(requireContext(), "Error: Invalid file", Toast.LENGTH_SHORT).show();
        }
    }

    /*Add this method to check if the file has a valid Excel extension*/
    private boolean isExcelFile(File file) {
        String filename = file.getName();
        return filename.endsWith(".xlsx") || filename.endsWith(".xls");
    }
    private void uploadFile(MultipartBody.Part partFile, String excelFilePath) {
        // Log the start of the uploadFile method
        Log.d("UploadData", "uploadFile: Uploading file.");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HEROKU_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BabyApi api = retrofit.create(BabyApi.class);

        Call<ResponseBody> upload = api.uploadExcelFile(partFile);
        upload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Log the successful upload
                    Log.d("UploadData", "uploadFile: Excel File Uploaded");
                    Toast.makeText(requireContext(), "Excel File Uploaded", Toast.LENGTH_SHORT).show();
                    deleteTempFile(excelFilePath);
                } else {
                    // Log an error if the upload fails
                    Log.e("UploadData", "uploadFile: Error uploading Excel file. Code: " + response.code());
                    // Additional logging for debugging
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("UploadData", "uploadFile: Error Body: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    private String getFilePathFromDocumentFile(Uri uri) {
        try {
            DocumentFile documentFile = DocumentFile.fromSingleUri(requireActivity(), uri);
            if (documentFile != null && documentFile.exists()) {
                // Create a temporary file in your app's private storage
                File tempFile = new File(requireActivity().getCacheDir(), "tempFile.xlsx");

                try (InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
                     OutputStream outputStream = new FileOutputStream(tempFile)) {

                    // Copy the content from InputStream to FileOutputStream
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    // Log the file path of the temporary file
                    Log.d("UploadData", "Temporary file path: " + tempFile.getAbsolutePath());

                    return tempFile.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void deleteTempFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            File tempFile = new File(filePath);
            if (tempFile.exists()) {
                if (tempFile.delete()) {
                    Log.d("UploadData", "Temporary file deleted successfully");
                } else {
                    Log.e("UploadData", "Failed to delete temporary file");
                }
            } else {
                Log.e("UploadData", "Temporary file does not exist");
            }
        } else {
            Log.e("UploadData", "Invalid file path");
        }
    }
}

