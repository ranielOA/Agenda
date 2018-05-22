package br.com.agenda.sinc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import br.com.agenda.activitys.ListaAlunosActivity;
import br.com.agenda.dao.AlunoDAO;
import br.com.agenda.dto.AlunoSync;
import br.com.agenda.event.AtualizaListaAlunoEvent;
import br.com.agenda.modelo.Aluno;
import br.com.agenda.preferences.AlunoPreferences;
import br.com.agenda.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoSincronizador {
    private final Context context;
    private EventBus eventbus = EventBus.getDefault();
    AlunoPreferences preferences;

    public AlunoSincronizador(Context contex) {
        this.context = contex;
        preferences = new AlunoPreferences(context);
    }

    public void buscaTodos(){
        if(preferences.temVersao()){
            buscaNovos();
        }
        else{
            buscaAlunos();
        }
    }

    private void buscaNovos() {
        Call<AlunoSync> call = new RetrofitInicializador().getAlunoService().novos(preferences.getVersao());
        call.enqueue(buscaAlunoCallback());
    }

    public void buscaAlunos() {
        Call<AlunoSync> call = new RetrofitInicializador().getAlunoService().lista();

        call.enqueue(buscaAlunoCallback());
    }

    public void sincronizaAlunosInternos(){
        final AlunoDAO dao = new AlunoDAO(context);
        List<Aluno> alunos = dao.listaNaoSincronizado();

        Call<AlunoSync> call = new RetrofitInicializador().getAlunoService().atualiza(alunos);
        call.enqueue(new Callback<AlunoSync>() {
            @Override
            public void onResponse(Call<AlunoSync> call, Response<AlunoSync> response) {
                AlunoSync alunoSync = response.body();
                dao.sincroniza(alunoSync.getAlunos());
                dao.close();
            }

            @Override
            public void onFailure(Call<AlunoSync> call, Throwable t) {

            }
        });
    }

    @NonNull
    private Callback<AlunoSync> buscaAlunoCallback() {
        return new Callback<AlunoSync>() {
            @Override
            public void onResponse(Call<AlunoSync> call, Response<AlunoSync> response) {
                AlunoSync alunoSync = response.body();
                String versao = alunoSync.getMomentoDaUltimaModificacao();

                preferences.salvaVersao(versao);

                AlunoDAO alunoDAO = new AlunoDAO(context);
                alunoDAO.sincroniza(alunoSync.getAlunos());
                alunoDAO.close();

                Log.i("versao", preferences.getVersao());

                eventbus.post(new AtualizaListaAlunoEvent());
            }

            @Override
            public void onFailure(Call<AlunoSync> call, Throwable t) {
                Log.e("onFailure chamado", t.getMessage());
                eventbus.post(new AtualizaListaAlunoEvent());
            }
        };
    }
}