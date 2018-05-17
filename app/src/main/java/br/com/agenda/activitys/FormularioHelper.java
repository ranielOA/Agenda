package br.com.agenda.activitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import br.com.agenda.modelo.Aluno;

public class FormularioHelper {
    private Aluno aluno;
    private EditText nome;
    private EditText endereco;
    private EditText telefone;
    private EditText site;
    private RatingBar nota;
    private ImageView foto;

    public FormularioHelper(FormularioActivity formulario) {
        this.aluno = new Aluno();
        this.nome = formulario.nome;
        this.endereco = formulario.endereco;
        this.telefone = formulario.telefone;
        this.site = formulario.site;
        this.nota = formulario.nota;
        this.foto = formulario.fotoAluno;
    }

    public Aluno pegaAluno(){
        aluno.setNome(nome.getText().toString());
        aluno.setEndereco(endereco.getText().toString());
        aluno.setTelefone(telefone.getText().toString());
        aluno.setSite(site.getText().toString());
        aluno.setNota(Double.parseDouble(String.valueOf(nota.getRating())));
        aluno.setCaminhoFoto((String) foto.getTag());

        return aluno;
    }

    public void preencheFormulario(Aluno aluno){
        this.aluno = aluno;
        nome.setText(aluno.getNome());
        endereco.setText(aluno.getEndereco());
        telefone.setText(aluno.getTelefone());
        site.setText(aluno.getSite());
        //this.nota.setProgress(this.aluno.getNota().intValue());
        this.nota.setRating(aluno.getNota().floatValue());

        if(aluno.getCaminhoFoto() != null){
            carregaFoto(aluno.getCaminhoFoto());
        }
    }

    public void carregaFoto(String localFoto) {
        Bitmap bitmap = BitmapFactory.decodeFile(localFoto);
        bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
        foto.setImageBitmap(bitmap);
        foto.setScaleType(ImageView.ScaleType.FIT_XY);
        foto.setTag(localFoto);
    }
}
