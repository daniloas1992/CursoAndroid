package com.danilo.santos.my.app.olx.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.danilo.santos.my.app.olx.R;
import com.danilo.santos.my.app.olx.helper.Permissoes;
import com.santalu.maskara.widget.MaskEditText;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CadastrarAnuncioActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int SELECIONAR_IMAGEM_UM = 1;
    public static final int SELECIONAR_IMAGEM_DOIS = 2;
    public static final int SELECIONAR_IMAGEM_TRES = 3;

    private EditText campoTitulo;
    private EditText campoDescricao;
    private MaskEditText campoTelefone;
    private CurrencyEditText campoValor;
    private Button buttonCadastrarAnuncio;
    private ImageView imagem1;
    private ImageView imagem2;
    private ImageView imagem3;
    private Spinner campoEstado;
    private Spinner campoCategoria;

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private List<String> listaFotosRecuperadas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncio);

        Permissoes.validarPermissoes(permissoes, this,1);

        inicializarComponentes();
        carregarDadosSpinner();
    }

    private void inicializarComponentes(){
        campoTitulo = findViewById(R.id.editTitulo);
        campoDescricao = findViewById(R.id.editdescricao);
        campoValor = findViewById(R.id.editValor);
        campoTelefone = findViewById(R.id.editTelefone);
        imagem1 = findViewById(R.id.imageCadastro1);
        imagem2 = findViewById(R.id.imageCadastro2);
        imagem3 = findViewById(R.id.imageCadastro3);
        campoEstado = findViewById(R.id.spinnerEstado);
        campoCategoria = findViewById(R.id.spinnerCategoria);
        buttonCadastrarAnuncio = findViewById(R.id.buttonCadastrarAnuncio);

        imagem1.setOnClickListener(this);
        imagem2.setOnClickListener(this);
        imagem3.setOnClickListener(this);
        buttonCadastrarAnuncio.setOnClickListener(this);

        Locale locale = new Locale("pt","BR");
        campoValor.setLocale(locale);
    }

    private void carregarDadosSpinner(){

        String[] estados = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);;
        campoEstado.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonCadastrarAnuncio: {
                salvarAnuncio();
                break;
            }
            case R.id.imageCadastro1: {
                escolherImagem(SELECIONAR_IMAGEM_UM);
                break;
            }
            case R.id.imageCadastro2: {
                escolherImagem(SELECIONAR_IMAGEM_DOIS);
                break;
            }
            case R.id.imageCadastro3: {
                escolherImagem(SELECIONAR_IMAGEM_TRES);
                break;
            }
        }

    }

    private void escolherImagem(final int requetCode) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, requetCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            //Recuperar a imagem
            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            //Configura a imagem no ImageView
            if(requestCode == SELECIONAR_IMAGEM_UM){
                imagem1.setImageURI(imagemSelecionada);
            } else if(requestCode == SELECIONAR_IMAGEM_DOIS){
                imagem2.setImageURI(imagemSelecionada);
            } else if(requestCode == SELECIONAR_IMAGEM_TRES){
                imagem3.setImageURI(imagemSelecionada);
            }
            listaFotosRecuperadas.add(caminhoImagem);
        }
    }

    private void salvarAnuncio() {
        String valor = campoValor.getText().toString();
        String telefone = campoTelefone.getUnMasked();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                aletarValidacaoPermissao();
            }
        }
    }

    private void aletarValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões!");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}