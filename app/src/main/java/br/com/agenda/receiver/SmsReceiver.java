package br.com.agenda.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.telephony.SmsMessage;
import android.widget.Toast;

import br.com.agenda.R;
import br.com.agenda.dao.AlunoDAO;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");
        byte[] pdu = (byte[]) pdus[0];

        String format = (String) intent.getSerializableExtra("format");

        SmsMessage message = null;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            message = SmsMessage.createFromPdu(pdu, format);
        else
            message = SmsMessage.createFromPdu(pdu);

        String telefone = message.getDisplayOriginatingAddress();

        AlunoDAO dao = new AlunoDAO(context);
        if(dao.isAluno(telefone)){
            Toast.makeText(context, "Chegou SMS de um Aluno!", Toast.LENGTH_SHORT).show();
            MediaPlayer mp = MediaPlayer.create(context, R.raw.msg);
            mp.start();
        }
        dao.close();
    }
}
