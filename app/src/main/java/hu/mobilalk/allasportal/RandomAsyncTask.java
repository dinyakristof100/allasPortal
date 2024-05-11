package hu.mobilalk.allasportal;

import android.os.AsyncTask;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class RandomAsyncTask extends AsyncTask<Void, Void, String> {
    private WeakReference<TextView> myTextView;

    public RandomAsyncTask(TextView textView) {
        this.myTextView= new WeakReference<TextView>(textView);
    }

    @Override
    protected String doInBackground(Void... voids) {
        Random random = new Random();
        int num = random.nextInt(11);
        int ms = num * 1500;

        try{
            Thread.sleep(ms);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        return "Login as guest";// after "+ ms + " ms.";
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        myTextView.get().setText(s);

    }
}
