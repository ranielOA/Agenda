package br.com.agenda.converter;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

import br.com.agenda.modelo.Aluno;

public class AlunoConverter {
    public String toJson(List<Aluno> alunos){
        try {
            JSONStringer js = new JSONStringer();
            js.object().key("list").array().object().key("aluno").array();

            for (Aluno aluno: alunos) {
                js.object()
                        .key("id").value(aluno.getId())
                        .key("nome").value(aluno.getNome())
                        .key("telefone").value(aluno.getTelefone())
                        .key("endereco").value(aluno.getEndereco())
                        .key("site").value(aluno.getSite())
                        .key("nota").value(aluno.getNota())
                        .endObject();
            }

            return js.endArray().endObject().endArray().endObject().toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String converterParaJsonCompleto(Aluno aluno) {
        JSONStringer js = new JSONStringer();

        try {
            js.object()
                    .key("id").value(aluno.getId())
                    .key("nome").value(aluno.getNome())
                    .key("telefone").value(aluno.getTelefone())
                    .key("endereco").value(aluno.getEndereco())
                    .key("site").value(aluno.getSite())
                    .key("nota").value(aluno.getNota())
                    .key("caminhoFoto").value(aluno.getCaminhoFoto())
                    .endObject();;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return js.toString();
    }
}
