package info.pauek.notas;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class EditaNotaActivity extends AppCompatActivity {

    private Nota original;
    private int pos = -1;
    private EditText edit_titulo;
    private EditText edit_texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edita_nota);

        edit_titulo = (EditText) findViewById(R.id.edit_titulo);
        edit_texto = (EditText) findViewById(R.id.edit_texto);

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", -1);
        if (pos != -1) {
            original = ListaNotas.getNota(pos);
            edit_titulo.setText(original.getTitulo());
            edit_texto.setText(original.getTexto());
        } else {
            original = new Nota();
        }
    }

    private boolean hayCambios() {
        String titulo = edit_titulo.getText().toString();
        String texto  = edit_texto.getText().toString();
        return !original.getTitulo().equals(titulo) || !original.getTexto().equals(texto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edita_nota_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar:
                String titulo = edit_titulo.getText().toString();
                String texto  = edit_texto.getText().toString();
                if (pos != -1) {
                    ListaNotas.modifica(pos, titulo, texto);
                } else {
                    ListaNotas.nueva(titulo, texto);
                }
                setResult(RESULT_OK);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (hayCambios()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.confirmacion);
            builder.setMessage(R.string.confirma_descarta);
            builder.setPositiveButton(R.string.descarta, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditaNotaActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton(R.string.sigue_editando, null);
            builder.create().show();
        } else {
            super.onBackPressed();
        }
    }
}
