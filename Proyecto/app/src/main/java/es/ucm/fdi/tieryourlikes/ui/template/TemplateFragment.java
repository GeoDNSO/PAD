package es.ucm.fdi.tieryourlikes.ui.template;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import es.ucm.fdi.tieryourlikes.App;
import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.Category;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.utilities.MediaManager;

public class TemplateFragment extends Fragment {
    private View root;
    private TemplateViewModel mViewModel;

    private static final int ROW_LIMIT = 8;
    private int countView;
    private char nextTierRow;
    private int numberOfImages;

    private EditText et_template_name;
    private Button add_cover_button;
    private Button add_images_button;
    private Button add_label_button;
    private Button create_template;
    private ImageView imageView;
    private LinearLayout linearLayout;
    private LinearLayout template_linearLayout;
    private EditText et_row_label;
    private ImageView iv_row_label;
    private SearchableSpinner categoriesSpinner;

    private Bitmap bitmap;
    private List<Bitmap> bitmapList;
    private List<String> containerList;
    private List<String> rowStringList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.template_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(TemplateViewModel.class);

        init();
        listeners();
        observers();

        mViewModel.getCategories();

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
                Toast.makeText(getActivity(), getString(R.string.template_created), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(root).navigate(R.id.homeFragment);
            }
        });

        mViewModel.getCategoriesResponse().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<Category>>>() {
            @Override
            public void onChanged(ApiResponse<List<Category>> listApiResponse) {
                if(listApiResponse.getResponseStatus() == ResponseStatus.ERROR) {
                    //Toast.makeText(getActivity(), "Hubo un error:" + listApiResponse.getError(), Toast.LENGTH_SHORT).show();
                    //Si falla se vuelve a pedir al servidor
                    mViewModel.getCategories();
                    return;
                }
                List<String> categoriesList = listApiResponse.getObject().stream()
                        .map(category -> category.getName())
                        .collect(Collectors.toList());

                categoriesSpinner.setAdapter(new ArrayAdapter<>(getActivity(),
                                R.layout.support_simple_spinner_dropdown_item,
                                categoriesList));
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
        create_template = root.findViewById(R.id.create_template_button);

        countView = 0;
        nextTierRow = 'A';

        categoriesSpinner = root.findViewById(R.id.template_category_spinner);
        categoriesSpinner.setTitle(getString(R.string.select_category));
        categoriesSpinner.setPositiveButton(getString(R.string.category_select_ok));

        bitmapList = new ArrayList<>();
    }

    private void listeners(){
        add_cover_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.REQUEST_STORAGE);
                }else {
                    createImageChooser(false);
                }
            }
        });

        add_images_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.REQUEST_STORAGE);
                }else {
                    //createImageChooser(TemplateFragment.this.getActivity(), true);
                    createImageChooser(true);
                }
            }
        });

        add_label_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countView >= ROW_LIMIT){
                    Toast.makeText(getContext(), getString(R.string.cannot_add_more_rows), Toast.LENGTH_SHORT).show();
                }else {
                    countView++;
                    addView();
                }
            }
        });

        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 String template_category = (String) categoriesSpinner.getSelectedItem();
                 Log.d("TEMPLATE_SPINNER", "Item seleccionado " + template_category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), getString(R.string.select_category_message), Toast.LENGTH_SHORT).show();
            }
        });

        create_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTemplate();
            }
        });
    }

    private void addView(){
        View view = getLayoutInflater().inflate(R.layout.template_row_label, null ,false);
        et_row_label = view.findViewById(R.id.editText_row);
        iv_row_label = view.findViewById(R.id.remove_button_row);

        String rowLabelHint = String.valueOf(nextTierRow++);
        et_row_label.setHint(rowLabelHint);
        iv_row_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(view);
                nextTierRow--;
                countView--;
            }
        });
        template_linearLayout.addView(view);
    }

    private void removeView(View v){
        template_linearLayout.removeView(v);
    }

    public void createImageChooser(boolean allowMultipleSelect){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultipleSelect); //allowMultipleSelect --> true m??s de una imagen a seleccionar
        intent.setAction(Intent.ACTION_GET_CONTENT);
        String msg = (allowMultipleSelect) ? getActivity().getString(R.string.select_images) : getActivity().getString(R.string.select_image);
        int requestCode = (allowMultipleSelect) ? AppConstants.INSERT_IMAGES_RC_IMAGES : AppConstants.INSERT_IMAGES_RC;
        startActivityForResult(Intent.createChooser(intent, msg), requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case(AppConstants.INSERT_IMAGES_RC_IMAGES) : {
                if(resultCode == Activity.RESULT_OK){
                    bitmapList = new ArrayList<>();
                    removeImages();
                    if(data.getClipData() != null) {
                        numberOfImages = data.getClipData().getItemCount(); //devuelve el numero de imagenes seleccionadas
                        for (int i = 0; i < numberOfImages; ++i) {
                            try {
                                Uri uri = data.getClipData().getItemAt(i).getUri();
                                bitmapList.add(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        try {
                            Uri uri = data.getData();
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
                    removeImage();
                    try {
                        Uri uri = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    showImage();
                }
                break;
            }
        }
    }

    public void showImages(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        for (int i = 0; i < bitmapList.size(); ++i){
            View view = layoutInflater.inflate(R.layout.image_item_fragment, linearLayout, false);
            ImageView imageView = view.findViewById(R.id.imageView_template_item);
            imageView.setMaxHeight(200);
            imageView.setMaxWidth(200);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageBitmap(bitmapList.get(i));
            imageView.setPadding(10,0,10,0);
            linearLayout.addView(view);
        }
    }

    public void showImage(){
        imageView.setImageBitmap(bitmap);
        imageView.setMaxWidth(200);
        imageView.setMaxHeight(200);
    }

    public void removeImages(){
        linearLayout.removeAllViewsInLayout();
    }

    public void removeImage(){
        imageView.setImageDrawable(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TemplateViewModel.class);
    }

    private void createTemplate(){
        containerList = new ArrayList<>();
        if(bitmapList != null && bitmapList.size() > 0) {
            for (int i = 0; i < bitmapList.size(); ++i) {
                containerList.add(MediaManager.bitmapToBase64(bitmapList.get(i)));
            }
        }

        String image = (bitmap != null) ? MediaManager.bitmapToBase64(bitmap) : "";

        rowStringList = new ArrayList<>();
        for (int i = 0; i < countView; ++i) {
            EditText editText = template_linearLayout.getChildAt(i).findViewById(R.id.editText_row);
            String text = editText.getText().toString();
            if(text != null && !text.equals("")) {
                rowStringList.add(text);
            }else{
               text =  editText.getHint().toString();
               rowStringList.add(text);
            }
        }
        String template_name = et_template_name.getText().toString();

        String template_category = (String) categoriesSpinner.getSelectedItem();
        if (template_category == null) {
            Toast.makeText(getActivity(), getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        if (template_name.isEmpty() || template_category.isEmpty() || containerList.size() == 0 || image == "" || rowStringList.size() == 0) {
            Toast.makeText(getActivity(), getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
        }
        else{
            Template template = new Template("-1", template_name, template_category, App.getInstance().getUsername(), containerList, rowStringList, image, "");
            mViewModel.createTemplate(template);
        }
    }
}