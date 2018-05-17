package br.com.agenda.webclient;

import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WebClient {
    public String post(String json) {
        String endereco = "https://www.caelum.com.br/mobile";
        return realizaRequisicao(json, endereco);
    }

    public void insere(String json) {
        String endereco = "192.168.0.14:8080/api/aluno";
        realizaRequisicao(json, endereco);
    }

    private String realizaRequisicao(String json, String endereco) {
        try {
            URL url = new URL(endereco);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Accept", "application/json");        //dis que esta utilizando json para comunicação, tanto para enviar quanto para receber
            connection.setRequestProperty("Content-type", "application/json");

            connection.setDoInput(true);        //configura para usar POST na comunicação com o servidor
            connection.setDoOutput(true);

            PrintStream saida = new PrintStream(connection.getOutputStream());
            saida.println(json);

            connection.connect();

            String resposta = new Scanner(connection.getInputStream()).next();

            return resposta;

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
