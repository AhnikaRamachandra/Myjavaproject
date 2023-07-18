package com.example.myjavaproject;

import static com.example.myjavaproject.R.id;
import static com.example.myjavaproject.R.id.destinationLanguagechosebtn;
import static com.example.myjavaproject.R.id.sourceLanguageET;
import static com.example.myjavaproject.R.layout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ThirdActivity extends AppCompatActivity {
private EditText sourceLanguageEt;
    private TextToSpeech textToSpeech;
private MaterialButton destinationLanguageChooseBtn;
private MaterialButton sourceLanguageChooseBtn;
private TextView destinationLanguageTV;
private MaterialButton translateBtn;
private TranslatorOptions translatorOptions;
private Translator translator;

private ProgressDialog progressDialog;

private ArrayList<ModelLanguage> languageArrayList;
private static final String TAG="MAIN_TAG";
private String sourceLanguageCode="en";
    private String sourceLanguageTitle="English";
    private String destinationLanguageTitle="Urdu";
    private String destinationLanguageCode="ur";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_third);



  sourceLanguageChooseBtn=findViewById(destinationLanguagechosebtn);
   sourceLanguageEt=findViewById(sourceLanguageET);
   destinationLanguageTV=findViewById(id.destinationLanguageTV);
   destinationLanguageChooseBtn=findViewById(destinationLanguagechosebtn);
   translateBtn=findViewById(id.translateBtn);
progressDialog=new ProgressDialog(this);
progressDialog.setTitle("Please wait");
progressDialog.setCanceledOnTouchOutside(false);

   loadAvailableLanguages();

   sourceLanguageChooseBtn.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
     sourceLanguageChoose();
       }
   });
   destinationLanguageChooseBtn.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
destinationLanguageChoose();
       }
   });
   translateBtn.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
validateData();
       }
   });


    }

private String sourceLanguageText="";
    private void validateData() {
        sourceLanguageText=sourceLanguageEt.getText().toString().trim();
        Log.d(TAG,"validation:sourceLanguageText: "+sourceLanguageText);
        if (sourceLanguageText.isEmpty()){
            Toast.makeText(this,"enter text to translate....",Toast.LENGTH_SHORT).show();
        }
        else {
          startTranslation();  
        }
    }

    private void startTranslation() {
        progressDialog.setMessage("processing language model.....");
        progressDialog.show();
        translatorOptions=new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguageCode)
                .setTargetLanguage(destinationLanguageCode)
                .build();
        translator= Translation.getClient(translatorOptions);
        DownloadConditions downloadConditions=new DownloadConditions.Builder()
                .requireWifi()
                .build();
        translator.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "on success:model ready,starting translate...");
               progressDialog.setMessage("Translating");
               translator.translate(sourceLanguageText)
                       .addOnSuccessListener(new OnSuccessListener<String>() {
                           @Override
                           public void onSuccess(String translatedText) {

                               Log.d(TAG,"onSuccess: translatedText: "+translatedText);
                              progressDialog.dismiss();
                               destinationLanguageTV.setText(translatedText);
                           }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               progressDialog.dismiss();
                               Log.e(TAG,"on failure",e);
                       Toast.makeText(ThirdActivity.this,"failed to translate due to "+e.getMessage(),Toast.LENGTH_SHORT).show();
                           }
                       });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
progressDialog.dismiss();
                        Toast.makeText(ThirdActivity.this,"failed to ready model due to "+e.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.e(TAG,"on failure",e);
                    }
                });
    }

    private void sourceLanguageChoose(){
    PopupMenu popupMenu=new PopupMenu(this,sourceLanguageChooseBtn);
for(int i=0;i<languageArrayList.size();i++){
    popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).languageTitle);
}

popupMenu.show();

popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

    @Override

    public boolean onMenuItemClick(MenuItem item) {
       int position=item.getItemId();
sourceLanguageCode=languageArrayList.get(position).languageCode;
sourceLanguageTitle=languageArrayList.get(position).languageTitle;
sourceLanguageChooseBtn.setText(sourceLanguageTitle);
sourceLanguageEt.setHint("Enter"+sourceLanguageTitle);
Log.d(TAG,"onMenuItemClick: sourceLanguageCode "+sourceLanguageCode);
        Log.d(TAG,"onMenuItemClick: sourceLanguageTitle "+sourceLanguageTitle);



        return false;
    }
});
    }
private void destinationLanguageChoose(){
  PopupMenu popupMenu= new PopupMenu(this,destinationLanguageChooseBtn);
  for (int i=0;i<languageArrayList.size();i++){
      popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).getLanguageTitle());
  }
  popupMenu.show();
  popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
          int position=item.getItemId();
           destinationLanguageCode = languageArrayList.get(position).languageCode;
          destinationLanguageTitle=languageArrayList.get(position).languageTitle;

         destinationLanguageChooseBtn.setText(destinationLanguageTitle);
         Log.d(TAG,"onMenuItemClick: destinationLanguageCode:"+destinationLanguageCode);
          Log.d(TAG,"onMenuItemClick: destinationLanguageTitle:"+destinationLanguageTitle);



          return false;
      }
  });
}

    private void loadAvailableLanguages() {
    languageArrayList=new ArrayList<>();
        List<String> languageCodeList= TranslateLanguage.getAllLanguages();
   for(String languageCode: languageCodeList){
       String languageTitle = new Locale(languageCode).getDisplayLanguage();
   Log.d(TAG,"loadAvailableLanguage: languageCode:"+languageCode);
 Log.d(TAG,"loadAvailableLanguage: languageTitle:"+languageTitle);
ModelLanguage modelLanguage=new ModelLanguage(languageCode,languageTitle);
 languageArrayList.add(modelLanguage);
   }
    }

    public void speak(View view) {
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
   intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"start speaking");
startActivityForResult(intent,100);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @
            Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK){
            sourceLanguageEt.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
        }
    }
}
