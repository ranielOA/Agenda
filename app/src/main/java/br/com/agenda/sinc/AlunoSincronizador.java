package br.com.agenda.sinc;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import br.com.agenda.activitys.ListaAlunosActivity;
import br.com.agenda.dao.AlunoDAO;
import br.com.agenda.dto.AlunoSync;
import br.com.agenda.event.AtualizaListaAlunoEvent;
import br.com.agenda.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoSincronizador {
    private final ListaAlunosActivity listaAlunosActivity;
    private EventBus eventbus = EventBus.getDefault();

    public AlunoSincronizador(ListaAlunosActivity listaAlunosActivity) {
        this.listaAlunosActivity = listaAlunosActivity;
    }

    public void buscaAlunos() {
        Call<AlunoSync> call = new RetrofitInicializador().getAlunoService().lista();

        call.enqueue(new Callback<AlunoSync>() {
            @Override
            public void onResponse(Call<AlunoSync> call, Response<AlunoSync> response) {
                AlunoSync alunoSync = response.body();

                AlunoDAO alunoDAO = new AlunoDAO(listaAlunosActivity);
                alunoDAO.sincroniza(alunoSync.getAlunos());
                alunoDAO.close();
                eventbus.post(new AtualizaListaAlunoEvent());
            }

            @Override
            public void onFailure(Call<AlunoSync> call, Throwable t) {
                Log.e("onFailure chamado", t.getMessage());
                eventbus.post(new AtualizaListaAlunoEvent());
            }
        });
    }
}