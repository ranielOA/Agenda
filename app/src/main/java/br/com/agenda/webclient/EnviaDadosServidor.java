package br.com.agenda.webclient;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import br.com.agenda.converter.AlunoConverter;
import br.com.agenda.dao.AlunoDAO;
import br.com.agenda.modelo.Aluno;

public class EnviaDadosServidor extends AsyncTask<Void, Void, String> {
    private Context context;
    ProgressBar progressBar;
    AppCompatActivity activity;

    public EnviaDadosServidor(Context context, ProgressBar progressBar, AppCompatActivity activity) {
        this.context = context;
        this.progressBar = progressBar;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);

        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    protected String doInBackground(Void... voids) {
        AlunoDAO dao = new AlunoDAO(context);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();

        AlunoConverter converter = new AlunoConverter();
        String json = converter.toJson(alunos);

        WebClient client = new WebClient();
        String resposta = client.post(json);

        return resposta;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
