package es.ucm.fdi.tieryourlikes.ui.trial;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.App;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.Tier;
import es.ucm.fdi.tieryourlikes.model.TierRow;
import es.ucm.fdi.tieryourlikes.model.serializers.TemplateSerializer;
import es.ucm.fdi.tieryourlikes.model.serializers.TierSerializer;
import es.ucm.fdi.tieryourlikes.ui.home.HomeViewModel;

import static android.content.ContentValues.TAG;

public class TrialFragment extends Fragment {

    private View root;
    private TrialViewModel mViewModel;
    private TextView tvPrueba;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.prueba_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(TrialViewModel.class);

        Button button = root.findViewById(R.id.buttonDemo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navegar al fragmento con la demo del tier maker
                Navigation.findNavController(root).navigate(R.id.tierFragment);
            }
        });

        Button button2 = root.findViewById(R.id.buttonDemo2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navegar al fragmento con la demo del tier maker
                Navigation.findNavController(root).navigate(R.id.templateFragment);
            }
        });

        if(App.getInstance().isAdmin())
            Log.d(TAG, "onCreateView: "+ "Es admin");
        else
            Log.d(TAG, "onCreateView: "+ "No Es admin");

        Log.d(TAG, "onCreateView: " + App.getInstance().getUser().toString());

        prueba();

        prueba2();

        prueba3();

        return root;
    }

    private void prueba3() {
        //Prueba de tiers

        List<String> container = new ArrayList<>();
        List<TierRow> tierRows = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            container.add("url" + i);

        for (int i = 0; i < 5; i++)
            tierRows.add(new TierRow("Tier_"+i, "#B0B0B0", container.subList(0, i)));

        Tier tier = new Tier("3948i043", "09283ru83uy", "hola", container, tierRows, "");

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Tier.class, new TierSerializer()) //MIrar clase TemplateSerializer que es quien lo convierte a JSON
                .setPrettyPrinting()
                .create();

        String pretty = gson.toJson(tier);

        //Log.d("TAG_3", "prueba3: " + pretty);
    }

    private void prueba2() {
        mViewModel.getPruebaAPIResponse2().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<Template>>>() {
            @Override
            public void onChanged(ApiResponse<List<Template>> listApiResponse) {
                if(listApiResponse.getResponseStatus() == ResponseStatus.ERROR) {
                    Toast.makeText(getActivity(), "Hubo un error:" + listApiResponse.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Template.class, new TemplateSerializer()) //MIrar clase TemplateSerializer que es quien lo convierte a JSON
                        .setPrettyPrinting()
                        .create();

                String pretty = gson.toJson(listApiResponse.getObject());
                tvPrueba.setText(pretty);
            }
        });
    }

    private void prueba() {
        tvPrueba = root.findViewById(R.id.tvPruebaServer);

        mViewModel.getPruebaAPIResponse().observe(getViewLifecycleOwner(), new Observer<ApiResponse<String>>() {
            @Override
            public void onChanged(ApiResponse<String> s) {
                if(s.getResponseStatus() == ResponseStatus.ERROR) {
                    Toast.makeText(getActivity(), "Hubo un error:" + s.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }
                tvPrueba.setText(s.getObject());
            }
        });

        Button button = root.findViewById(R.id.buttonDB);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ejemplo llamada al viewmodel que llamará a la API
                mViewModel.listTemplates(1, 5, null);


                //Ejemplo de Serializar un template a JSON

                String image = "url1";
                //Crear template
                List<String> container = new ArrayList<>();
                container.add("url1");
                container.add("url2");
                container.add("url4");
                container.add("url3");
                List<String> tierRows = new ArrayList<>();
                tierRows.add("S");
                tierRows.add("A");
                tierRows.add("B");
                tierRows.add("C");

                Template template = new Template("Prueba", "Otro", "hola",
                        image, container, tierRows);

                //Crear el item de GSON que lo convertira a json
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Template.class, new TemplateSerializer()) //MIrar clase TemplateSerializer que es quien lo convierte a JSON
                        .setPrettyPrinting()
                        .create();

                String json = gson.toJson(template);
                Log.d("TAG", "onClick: " + json);

                //Ejemplo de Deserializar un template a JSON, es automatico
                Template template2 = gson.fromJson(json, Template.class);
                template2.setTitle("HA FUNCIONADO");
                Log.d("TAG2", "onClick: " + template2.toString());

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TrialViewModel.class);
        // TODO: Use the ViewModel
    }


}
