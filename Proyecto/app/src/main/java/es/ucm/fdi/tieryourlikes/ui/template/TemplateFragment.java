package es.ucm.fdi.tieryourlikes.ui.template;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.App;
import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.TierRow;
import es.ucm.fdi.tieryourlikes.model.serializers.TemplateSerializer;
import es.ucm.fdi.tieryourlikes.ui.home.HomeViewModel;
import es.ucm.fdi.tieryourlikes.ui.template.listeners.TierElementDragListener;
import es.ucm.fdi.tieryourlikes.ui.template.listeners.TierElementTouchListener;
import es.ucm.fdi.tieryourlikes.ui.template.listeners.TierRowDragListener;

public class TemplateFragment extends Fragment {

    private View root;

    private EditText et_template_name;
    private EditText et_template_category;
    private Button add_cover_button;
    private Button add_images_button;
    private Button add_label_button;
    private ImageView imageView;
    private LinearLayout linearLayout;
    private LinearLayout template_linearLayout;
    private int numberOfImages;
    private EditText et_row_label;
    private ImageView iv_row_label;
    private int countView;
    private List<Bitmap> bitmapList;
    private Bitmap bitmap;
    private List<String> imageString;
    private List<String> rowString;
    private List<byte[]> imageByte;
    private String selectedImagePath;
    private List<String> pathList;


    private TemplateViewModel mViewModel;

    private RecyclerView recyclerView;
    private FlexboxLayout flexboxLayout;
    private TemplateAdapter templateAdapter;

    private List<TierRow> tierRowList;

    public static TemplateFragment newInstance() {
        return new TemplateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.template_fragment, container, false);

        mViewModel = new ViewModelProvider(this).get(TemplateViewModel.class);

        setHasOptionsMenu(true);
        init();
        listeners();
        observers();

        /*
        configDemo();

        recyclerView = root.findViewById(R.id.tierMakerRecyclerView);
        templateAdapter = new TemplateAdapter(getActivity(), tierRowList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(templateAdapter);
        */
        return root;
    }

    private void observers() {
        mViewModel.getTemplateResponse().observe(getViewLifecycleOwner(), new Observer<ApiResponse<Template>>() {
            @Override
            public void onChanged(ApiResponse<Template> listApiResponse) {
                if(listApiResponse.getResponseStatus() == ResponseStatus.ERROR) {
                    Toast.makeText(getActivity(), "Hubo un error:" + listApiResponse.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity(), "Se ha creado el template", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(root).navigate(R.id.homeFragment);
            }
        });
    }

    private void init(){
        et_template_name = root.findViewById(R.id.template_editText_name);
        add_cover_button = root.findViewById(R.id.template_button_cover_photo);
        add_images_button = root.findViewById(R.id.template_button_images_photo);
        add_label_button = root.findViewById(R.id.template_button_add_label);
        template_linearLayout = root.findViewById(R.id.template_expandable_list_view);
        linearLayout = root.findViewById(R.id.template_linearLayout);
        imageView = root.findViewById(R.id.template_imageView_cover_photo);
        et_template_category = root.findViewById(R.id.template_editText_category);
        countView = 0;
    }

    private void listeners(){
        add_cover_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.REQUEST_STORAGE);
                }else {
                    insertImageFromGallery();
                }
            }
        });

        add_images_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.REQUEST_STORAGE);
                }else {
                    insertImagesFromGallery();
                }
            }
        });

        add_label_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countView >= 8){
                    Toast.makeText(getContext(), getString(R.string.cannot_add_more_rows), Toast.LENGTH_SHORT).show();
                }else {
                    countView++;
                    addView();
                }
            }
        });
    }

    private void addView(){
        View view = getLayoutInflater().inflate(R.layout.template_row_label, null ,false);

        et_row_label = view.findViewById(R.id.editText_row);
        iv_row_label = view.findViewById(R.id.remove_button_row);

        iv_row_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(view);
                countView--;
            }
        });

        template_linearLayout.addView(view);
    }

    private void removeView(View v){
        template_linearLayout.removeView(v);
    }

    //función para seleccionar imagenes de la galeria
    private void insertImagesFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagenes"), AppConstants.INSERT_IMAGES_RC_IMAGES);
    }

    //función para seleccionar imagenes de la galeria
    private void insertImageFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), AppConstants.INSERT_IMAGES_RC);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case(AppConstants.INSERT_IMAGES_RC_IMAGES) : {
                if(resultCode == Activity.RESULT_OK){
                    //uriList = new ArrayList<>();
                    bitmapList = new ArrayList<>();
                    pathList = new ArrayList<>();
                    removeImages();
                    if(data.getClipData() != null) {
                        numberOfImages = data.getClipData().getItemCount(); //devuelve el numero de imagenes seleccionadas
                        for (int i = 0; i < numberOfImages; ++i) {
                            try {
                                Uri uri = data.getClipData().getItemAt(i).getUri();
                                pathList.add(getPath(getContext(), uri));
                                bitmapList.add(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri));
                                //uriList.add(uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        try {
                            Uri uri = data.getData();
                            pathList.add(getPath(getContext(), uri));
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                            bitmapList.add(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    showImages();
                }
                break;
            }
            case(AppConstants.INSERT_IMAGES_RC) : {
                if(resultCode == Activity.RESULT_OK){
                    //uriList = new ArrayList<>();
                    //bitmapList = new ArrayList<>();
                    removeImage();

                    try {
                        Uri uri = data.getData();
                        selectedImagePath = getPath(getContext(), uri);
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                        //uriList.add(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /*
                    if(data.getClipData() != null) {
                        try {
                            Uri uri = data.getData();
                            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                            //uriList.add(uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        try {
                            Uri uri = data.getData();
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }*/
                    showImage();
                }
                break;
            }
        }
    }


    // Implementation of the getPath() method and all its requirements is taken from the StackOverflow Paul Burke's answer: https://stackoverflow.com/a/20559175/5426539
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.create_template_menu, menu);

        MenuItem add_template = menu.findItem(R.id.add_template_menu_item);

        add_template.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                createTemplate();
                return true;
            }
        });
    }

    public void showImages(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        for (int i = 0; i < bitmapList.size(); ++i){
            View view = layoutInflater.inflate(R.layout.image_item_fragment, linearLayout, false);
            ImageView imageView = view.findViewById(R.id.imageView_template_item);
            imageView.setImageBitmap(bitmapList.get(i));
            imageView.setPadding(10,0,10,0);
            linearLayout.addView(view);
        }
    }

    public void showImage(){
        imageView.setImageBitmap(bitmap);
    }

    public void removeImages(){
        linearLayout.removeAllViewsInLayout();
    }

    public void removeImage(){
        imageView.setImageDrawable(null);
    }

    private void configDemo(){
        /*flexboxLayout = root.findViewById(R.id.activityMainFlexBox);

        //Preparar y configurar el contenedor desde el que se van a mover las imagenes
        flexboxLayout.setOnDragListener(new TierRowDragListener());
        int numberOfElements = 20;
        for(int i = 0; i < numberOfElements; i++){
            ImageView imageView = new ImageView(getContext());
            if(i % 2 == 0){
                imageView.setImageResource(R.drawable.ic_baseline_tag_faces_24);
            }else{
                imageView.setImageResource(R.drawable.ic_baseline_search_24);
            }

            //imageView.setOnTouchListener(new TierElementTouchListener("vacio"));
            imageView.setOnDragListener(new TierElementDragListener());

            imageView.bringToFront();
            flexboxLayout.addView(imageView);
        }

        //Crear las filas de los distintos tiers
        tierRowList = new ArrayList<>();
        for(int i = 0; i < 8; ++i){
            tierRowList.add(new TierRow("Tier"+i, new ArrayList<>()));
        }*/

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TemplateViewModel.class);
        // TODO: Use the ViewModel
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }

    private void createTemplate(){
        imageString = new ArrayList<>();
        imageByte = new ArrayList<>();
        if(bitmapList.size() > 0) {
            for (int i = 0; i < bitmapList.size(); ++i) {
                imageString.add(bitmapToBase64(bitmapList.get(i)));
            }
        }
        String image = "";

        if(bitmap != null) {
            image = bitmapToBase64(bitmap);
        }

        rowString = new ArrayList<>();

        for (int i = 0; i < countView; ++i) {
            EditText editText = template_linearLayout.getChildAt(i).findViewById(R.id.editText_row);
            String text = editText.getText().toString();
            if(!text.equals("") || !text.equals(null)) {
                rowString.add(text);
            }
        }

        String template_name = et_template_name.getText().toString();
        String template_category = et_template_category.getText().toString().toLowerCase();

        if (template_name.isEmpty() || template_category.isEmpty() || imageString.size() == 0 || image == "" || rowString.size() == 0) {
            Toast.makeText(getActivity(), getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
        }
        else{
            Template template = new Template(template_name, template_category, "Jin", image, imageString, rowString);
            mViewModel.createTemplate(template);
        }
    }

}