package br.com.agenda.tasks;

import android.os.AsyncTask;

import br.com.agenda.converter.AlunoConverter;
import br.com.agenda.modelo.Aluno;
import br.com.agenda.webclient.WebClient;

public class InsereAlunoTask  extends AsyncTask{
    private final Aluno aluno;

    public InsereAlunoTask(Aluno aluno) {
        this.aluno = aluno;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json = new AlunoConverter().converterParaJsonCompleto(aluno);
        new WebClient().insere(json);
        return null;
    }
}
