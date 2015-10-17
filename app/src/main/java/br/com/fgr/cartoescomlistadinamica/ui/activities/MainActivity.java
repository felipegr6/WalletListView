package br.com.fgr.cartoescomlistadinamica.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;

import java.util.ArrayList;
import java.util.List;

import br.com.fgr.cartoescomlistadinamica.R;
import br.com.fgr.cartoescomlistadinamica.model.Cartao;
import br.com.fgr.cartoescomlistadinamica.model.CartaoAlimentacao;
import br.com.fgr.cartoescomlistadinamica.model.CartaoRestaurante;
import br.com.fgr.cartoescomlistadinamica.ui.adapters.CartaoAdapter;
import br.com.fgr.cartoescomlistadinamica.ui.custom_views.CustomDinamicListView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

//    @Bind(R.id.dynamiclistview)
//    DynamicListView listView;

    @Bind(R.id.custom_dynamic_list_view)
    CustomDinamicListView customListView;

//    @Bind(R.id.rl_container)
//    RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        List<Cartao> cartoes = new ArrayList<>();

        cartoes.add(new Cartao("João", "02165064684", new CartaoAlimentacao()));
        cartoes.add(new Cartao("José", "65445916549", new CartaoRestaurante()));
        cartoes.add(new Cartao("Mary", "37505025620", new CartaoRestaurante()));

        CartaoAdapter adapter = new CartaoAdapter(this, cartoes);
//        OverlapListView overlapListView = new OverlapListView(this, container);

//        overlapListView.setAdapter(adapter);

//        listView.setAdapter(adapter);
//        listView.enableDragAndDrop();
//        listView.setDraggableManager(new TouchViewDraggableManager(R.id.rl_cartao));

        customListView.setAdapter(adapter);
        customListView.enableDragAndDrop();
        customListView.setDraggableManager(new TouchViewDraggableManager(R.id.rl_cartao));

    }

}