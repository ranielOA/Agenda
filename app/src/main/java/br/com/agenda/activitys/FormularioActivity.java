package br.com.agenda.activitys;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.File;

import br.com.agenda.BuildConfig;
import br.com.agenda.R;
import br.com.agenda.dao.AlunoDAO;
import br.com.agenda.modelo.Aluno;
import br.com.agenda.tasks.InsereAlunoTask;

public class FormularioActivity extends AppCompatActivity {
    EditText nome, endereco, telefone, site;
    RatingBar nota;
    ImageView fotoAluno;
    private FormularioHelper formularioHelper;
    private Aluno aluno = new Aluno();
    public static final int CODIGO_CAMERA = 123;
    private String localFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        Intent intent = getIntent();
        aluno = (Aluno) intent.getSerializableExtra("aluno");

        nome = findViewById(R.id.formulario_nome);
        endereco = findViewById(R.id.formulario_endereco);
        telefone = findViewById(R.id.formulario_telefone);
        site = findViewById(R.id.formulario_site);
        nota = findViewById(R.id.formulario_nota);
        fotoAluno = findViewById(R.id.foto_aluno_form);


        formularioHelper = new FormularioHelper(this);
        if(aluno != null)
            formularioHelper.preencheFormulario(aluno);

        Button botaoCamera = findViewById(R.id.formulario_botao_camera);

        botaoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vaiParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                localFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                vaiParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(FormularioActivity.this, BuildConfig.APPLICATION_ID + ".provider", new File(localFoto))); //codigo para funcionar no android 7 + criação de FileProvider no res/xml + inclusão da tag <provider> no manifest
                startActivityForResult(vaiParaCamera, CODIGO_CAMERA);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODIGO_CAMERA) {
            if(resultCode == RESULT_OK) {
                formularioHelper.carregaFoto(localFoto);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_formulario_ok:
                Aluno aluno = formularioHelper.pegaAluno();

                // Aqui instanciamos o DAO e inserimos o novo aluno no banco
                AlunoDAO dao = new AlunoDAO(this);

                if(aluno.getId() ==null)
                    dao.insere(aluno);
                else
                    dao.altera(aluno);

                dao.close();

                new InsereAlunoTask(aluno);

                Toast.makeText(FormularioActivity.this, "Aluno " + aluno.getNome() + " salvo!", Toast.LENGTH_SHORT).show();

                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
