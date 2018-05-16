package br.com.agenda.activitys;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.agenda.R;
import br.com.agenda.map.Localizador;
import br.com.agenda.fragments.MapaFragment;

public class MapaAlunosActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSOES = 1;
    private MapaFragment mapaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_alunos);

        //if(checkPermissions())

        iniciaMapaFrament();

        checkPermissions();
    }

    private void iniciaMapaFrament() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mapaFragment = new MapaFragment();
        transaction.replace(R.id.frame_mapa, mapaFragment);
        transaction.commit();
    }

    private boolean checkPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSOES);
            }
            else
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSOES){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //iniciaMapaFrament();
                new Localizador(this, mapaFragment);
            }
            else
                finish();
        }
    }
}
