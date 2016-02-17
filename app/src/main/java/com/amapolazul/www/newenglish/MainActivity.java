package com.amapolazul.www.newenglish;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amapolazul.www.newenglish.constants.Constants;
import com.amapolazul.www.newenglish.persitence.TranslatorWordsDAO;
import com.amapolazul.www.newenglish.persitence.TranslatorWordsInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;


public class MainActivity extends Activity {

    private ProgressDialog pd;
    private boolean isSpanish;
    private ImageView spanishImage;
    private ImageView englishImage;
    private TextView wordToFind;
    private TextView wordResult;
    private TranslatorWordsDAO translatorWordsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pd = new ProgressDialog(this);
        pd.setMessage("Cargando diccionario");
        pd.setTitle("English");
        pd.show();
        new CargarDiccionarioThread(this).execute();

        isSpanish = true;

        spanishImage = (ImageView) findViewById(R.id.espaniol);
        englishImage = (ImageView) findViewById(R.id.ingles);
        wordToFind = (TextView) findViewById(R.id.entrada);
        wordResult = (TextView) findViewById(R.id.resultado);
    }

    private class CargarDiccionarioThread extends AsyncTask<Void, Void, Void> {


        private Context context;

        public CargarDiccionarioThread(Context context){
            translatorWordsDAO = new TranslatorWordsDAO(context);
            this.context = context;
        }

        protected Void doInBackground(Void... args) {
            AssetManager assetManager = context.getAssets();
            try {
                InputStream csvStream = assetManager.open("a.csv");
                InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
                BufferedReader reader = new BufferedReader(csvStreamReader);
                translatorWordsDAO.open();
                //translatorWordsDAO.removeAll();
                String line;
                if(translatorWordsDAO.getWordEnglishSpanishInfo("a") == null) {
                    while ((line = reader.readLine()) != null) {
                        String[] array = line.split(";");
                        System.out.println("agregando linea " + line);
                        TranslatorWordsInfo translatorWordsInfo = new TranslatorWordsInfo();
                        translatorWordsInfo.setEnglish_audio_path(array[2]);
                        translatorWordsInfo.setEspanol_audio_path("pathSpa");
                        translatorWordsInfo.setEnglish_def(array[1]);
                        translatorWordsInfo.setEspanol_def(array[0]);
                        translatorWordsDAO.createWordDefinition(translatorWordsInfo);
                    }
                }
                pd.dismiss();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
        }
    }

    public void changeLanguage(View view) {
        if(isSpanish){
            spanishImage.setImageResource(R.drawable.ingles);
            englishImage.setImageResource(R.drawable.espaniol);
            isSpanish = false;
        } else {
            spanishImage.setImageResource(R.drawable.espaniol);
            englishImage.setImageResource(R.drawable.ingles);
            isSpanish = true;
        }
    }

    public void translate(View view){
        String wordTFind = wordToFind.getText().toString();
        StringBuffer wordToShow = new StringBuffer();
        StringBuffer phonetic = new StringBuffer();
        StringBuffer result = new StringBuffer();
        if(isSpanish){
            List<TranslatorWordsInfo> info = translatorWordsDAO.getWordSpanishEnglishInfo(wordTFind);
            if(info != null) {
                for(TranslatorWordsInfo inf: info) {
                    wordToShow.append(inf.getEnglish_def() + ", ");
                    phonetic.append("("+inf.getEnglish_audio_path()+")");
                    result.append(wordToShow.append(phonetic).append("\n"));
                }
            } else {
                result.append(Constants.WORD_NOT_FOUND);
            }
        } else {
            List<TranslatorWordsInfo> info = translatorWordsDAO.getWordEnglishSpanishInfo(wordTFind);
            if(info != null) {
                for(TranslatorWordsInfo inf: info) {
                    result.append(inf.getEspanol_def());
                    result.append("\n");
                }
            } else {
                result.append(Constants.WORD_NOT_FOUND);
            }
        }

        wordResult.setText(result.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
