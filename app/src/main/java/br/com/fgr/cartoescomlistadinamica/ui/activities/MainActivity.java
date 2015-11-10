package br.com.fgr.cartoescomlistadinamica.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

//    @Bind(R.id.dynamiclistview)
//    DynamicListView listView;

    @Bind(R.id.custom_dynamic_list_view)
    CustomDinamicListView customListView;

//    @Bind(R.id.rl_container)
//    OverlapListView container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        List<Cartao> cartoes = new ArrayList<>();
        ListView listView;

        cartoes.add(new Cartao("Eric", "02165064684", new CartaoAlimentacao()));
        cartoes.add(new Cartao("Henrique", "65445916549", new CartaoRestaurante()));
        cartoes.add(new Cartao("Paola", "37505025620", new CartaoRestaurante()));

        final CartaoAdapter adapter = new CartaoAdapter(this, cartoes);
//        container.setAdapter(adapter);

//        overlapListView.setAdapter(adapter);

//        listView.setAdapter(adapter);
//        listView.enableDragAndDrop();
//        listView.setDraggableManager(new TouchViewDraggableManager(R.id.rl_cartao));
//        listView.setOnItemLongClickListener(this);
//        listView.enableSwipeToDismiss(new OnDismissCallback() {
//
//            @Override
//            public void onDismiss(ViewGroup listView, int[] reverseSortedPositions) {
//
//                for (int position : reverseSortedPositions)
//                    adapter.remove(position);
//
//            }
//
//        });

        customListView.setAdapter(adapter);
        customListView.enableDragAndDrop();
        customListView.setDraggableManager(new TouchViewDraggableManager(R.id.rl_container));
        customListView.setOnItemLongClickListener(this);
        customListView.setOnItemClickListener(null);

        customListView.enableSwipeToDismiss(new OnDismissCallback() {

            @Override
            public void onDismiss(ViewGroup listView, int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions)
                    adapter.remove(position);
            }

        });

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        customListView.startDragging(position);

        return true;

    }

}