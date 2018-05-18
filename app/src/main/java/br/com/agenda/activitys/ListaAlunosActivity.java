package br.com.agenda.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import br.com.agenda.dto.AlunoSync;
import br.com.agenda.retrofit.RetrofitInicializador;
import br.com.agenda.webclient.EnviaDadosServidor;
import br.com.agenda.R;
import br.com.agenda.adapter.AlunosAdapter;
import br.com.agenda.dao.AlunoDAO;
import br.com.agenda.modelo.Aluno;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaAlunosActivity extends AppCompatActivity {
    ListView listaAlunos;
    Button novoAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 124);
        }

        listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        novoAluno = (Button) findViewById(R.id.novo_aluno);

        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intent);
            }
        });

        registerForContextMenu(listaAlunos);

        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Aluno aluno = (Aluno) lista.getItemAtPosition(position);
                Toast.makeText(ListaAlunosActivity.this, "Aluno " + aluno.getNome(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intent.putExtra("aluno", aluno);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Call<AlunoSync> call = new RetrofitInicializador().getAlunoService().lista();

        call.enqueue(new Callback<AlunoSync>() {
            @Override
            public void onResponse(Call<AlunoSync> call, Response<AlunoSync> response) {
                AlunoSync alunoSync = response.body();

                AlunoDAO alunoDAO = new AlunoDAO(ListaAlunosActivity.this);
                alunoDAO.sincroniza(alunoSync.getAlunos());
                alunoDAO.close();
                carregaLista();
            }

            @Override
            public void onFailure(Call<AlunoSync> call, Throwable t) {
                Log.e("onFailure chamado", t.getMessage());
            }
        });

        carregaLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_aluno, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.enviar_notas:
                RelativeLayout layout = findViewById(R.id.activity_lista_alunos_id);

                ProgressBar progressBar = new ProgressBar(ListaAlunosActivity.this, null, android.R.attr.progressBarStyleLarge);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                layout.addView(progressBar,params);

                new EnviaDadosServidor(this, progressBar, ListaAlunosActivity.this).execute();
                break;
            case R.id.menu_baixar_provas:
                Intent vaiParaProvas = new Intent(this, ProvasActivity.class);
                startActivity(vaiParaProvas);
                break;
            case R.id.menu_mapa:
                Intent vaiParaMapa = new Intent(this, MapaAlunosActivity.class);
                startActivity(vaiParaMapa);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

        MenuItem del = menu.add("Deletar");
        MenuItem itemligar = menu.add("Ligar");
        MenuItem itemSite = menu.add("Ver Site");
        MenuItem itemsms = menu.add("Sms");
        MenuItem itemMapa = menu.add("Vai Para o Mapa");

        String site = aluno.getSite();
        if (!site.startsWith("http://"))
            site = "http://" + site;

        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        intentSite.setData(Uri.parse(site));
        itemSite.setIntent(intentSite);

        Intent intentSms = new Intent(Intent.ACTION_VIEW);
        intentSms.setData(Uri.parse("sms:" + aluno.getTelefone()));
        itemsms.setIntent(intentSms);

        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        itemMapa.setIntent(intentMapa);

        itemligar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 123);
                }
                else{
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(intentLigar);
                }

                return false;
            }
        });

        del.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.deleta( aluno);
                dao.close();

                carregaLista();
                return false;
            }
        });
    }

    private void carregaLista(){
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.buscaAlunos();

        for (Aluno aluno:
             alunos) {
            Log.i("id do aluno", String.valueOf(aluno.getId()));
        }

        dao.close();

        AlunosAdapter adapter = new AlunosAdapter(this, alunos);

        listaAlunos.setAdapter(adapter);

    }
}
