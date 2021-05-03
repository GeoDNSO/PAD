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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
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
    private EditText et_template_category;
    private Button add_cover_button;
    private Button add_images_button;
    private Button add_label_button;
    private ImageView imageView;
    private LinearLayout linearLayout;
    private LinearLayout template_linearLayout;
    private EditText et_row_label;
    private ImageView iv_row_label;

    private Bitmap bitmap;
    private List<Bitmap> bitmapList;
    private List<String> imageString;
    private List<String> rowString;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.template_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(TemplateViewModel.class);
        setHasOptionsMenu(true);

        init();
        listeners();
        observers();

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
        nextTierRow = 'A';
    }

    private void listeners(){
        add_cover_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.REQUEST_STORAGE);
                }else {
                    MediaManager.createImageChooser(TemplateFragment.this.getActivity(), false);
                }
            }
        });

        add_images_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.REQUEST_STORAGE);
                }else {
                    MediaManager.createImageChooser(TemplateFragment.this.getActivity(), true);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TemplateViewModel.class);
    }

    private void createTemplate(){
        imageString = new ArrayList<>();
        if(bitmapList.size() > 0) {
            for (int i = 0; i < bitmapList.size(); ++i) {
                imageString.add(MediaManager.bitmapToBase64(bitmapList.get(i)));
            }
        }
        String image = (bitmap != null) ? MediaManager.bitmapToBase64(bitmap) : "";
        rowString = new ArrayList<>();
        for (int i = 0; i < countView; ++i) {
            EditText editText = template_linearLayout.getChildAt(i).findViewById(R.id.editText_row);
            String text = editText.getText().toString();
            if(!text.equals("") || !text.equals(null)) {
                rowString.add(text);
            }
        }
        String template_name = et_template_name.getText().toString();
        String template_category = et_template_category.getText().toString();

        if (template_name.isEmpty() || template_category.isEmpty() || imageString.size() == 0 || image == "" || rowString.size() == 0) {
            Toast.makeText(getActivity(), getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
        }
        else{
            Template template = new Template(template_name, template_category, "Jin", image, imageString, rowString);
            mViewModel.createTemplate(template);
        }
    }
}