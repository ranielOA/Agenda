package br.com.agenda.activitys;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.agenda.fragments.DetalhesProvaFragment;
import br.com.agenda.fragments.ListaProvasFragment;
import br.com.agenda.R;
import br.com.agenda.modelo.Prova;

public class ProvasActivity extends AppCompatActivity {
    static Prova prova = new Prova();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();

        tx.replace(R.id.frame_principal, new ListaProvasFragment());
        if(estaNoModoPaisagem()){
            DetalhesProvaFragment detalhesFragment = new DetalhesProvaFragment();

            if(ProvasActivity.prova != null)
                detalhesFragment = populaDetalheProvas(ProvasActivity.prova);

            tx.replace(R.id.frame_secundario, detalhesFragment);
        }

        tx.commit();

    }

    private boolean estaNoModoPaisagem() {
        return getResources().getBoolean(R.bool.modoPaisagem);
    }

    public void selecionaProva(Prova prova){
        FragmentManager manager = getSupportFragmentManager();

        if(!estaNoModoPaisagem()){
            FragmentTransaction tx = manager.beginTransaction();

            //DetalhesProvaFragment detalhesFragment = new DetalhesProvaFragment();
            //Bundle parametros = new Bundle();
            //parametros.putSerializable("prova", prova);
            //detalhesFragment.setArguments(parametros);

            tx.replace(R.id.frame_principal, populaDetalheProvas(prova));
            tx.addToBackStack(null);
            tx.commit();

            ProvasActivity.prova = prova;
        }else{
            DetalhesProvaFragment detalhesFragment = (DetalhesProvaFragment) manager.findFragmentById(R.id.frame_secundario);
            detalhesFragment.populaCamposCom(prova);
        }
    }

    public DetalhesProvaFragment populaDetalheProvas(Prova prova){
        DetalhesProvaFragment detalhesFragment = new DetalhesProvaFragment();
        Bundle parametros = new Bundle();
        parametros.putSerializable("prova", prova);
        detalhesFragment.setArguments(parametros);


        return detalhesFragment;
    }
}
