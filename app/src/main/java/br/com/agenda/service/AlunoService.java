package br.com.agenda.service;

import java.util.List;

import br.com.agenda.dto.AlunoSync;
import br.com.agenda.modelo.Aluno;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AlunoService {

    @POST("aluno")
    Call<Void> insere(@Body Aluno aluno);

    @GET("aluno")
    Call<AlunoSync> lista();

    @DELETE("aluno/{id}")
    Call<Void> deleta(@Path("id") String id);

    @GET("aluno/diff")
    Call<AlunoSync> novos(@Header("datahora") String versao); //busca somente alunos novos, ou seja, alunos que ja estao presentes no app antes de determinada versão do servidor
                                                              //não serão buscados, só os alunos que foram adicionados depois de determinada versão

    @PUT("aluno/lista")
    Call<AlunoSync> atualiza(@Body List<Aluno> alunos); //envia lista de alunos que precisa ser atualizada no servidor e retorna a lista com os que foram atualizados
                                                        //com sucesso, para então atualizar estes alunos no aplicativo tbm

    //swipe.setOnRefreshListener - neste refresh na classe ListaAlunosActivity faz a sincronização de aplicativo e servidor e no fim do onCreate() tbm
}
