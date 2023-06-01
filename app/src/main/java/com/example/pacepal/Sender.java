    //package com.example.pacepal;

//    import androidx.annotation.NonNull;
//    import androidx.appcompat.app.AppCompatActivity;
//
//    import android.os.AsyncTask;
//    import android.os.Bundle;
//    import android.os.Handler;
//    import android.os.Looper;
//    import android.os.Message;
//    import android.view.View;
//    import android.widget.Button;
//    import android.widget.EditText;
//    import android.widget.TextView;
//    import android.widget.Toast;
//
//    import java.io.IOException;
//    import java.io.ObjectInputStream;
//    import java.io.ObjectOutputStream;
//    import java.net.Socket;
//
//    public class Sender extends AppCompatActivity {
//
//        Button sendButton;
//        EditText input;
//        TextView label;
//        Handler handler;
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.send);
//
//            handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
//                @Override
//                public boolean handleMessage(@NonNull Message message) {
//                    String result= message.getData().getString("result");
//                    label.setText(result);
//                    return true;
//                }
//            });
//
//            //TODO findViewByID of three
//            sendButton = (Button) findViewById(R.id.btnSend);
//            sendButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(Sender.this,"Send", Toast.LENGTH_LONG).show();
//                    String txt = input.getText().toString();
//
//                    //TODO start the thread that sends the file
//                    SenderThread t = new SenderThread(txt,8 ,handler);
//
//                    AsyncTask<String,Void,String> myAsync = new AsyncTask<String, Void, String>() {
//                        @Override
//                        protected String doInBackground(String... strings) {
//                            try{
//                                Socket s = new Socket("localhost",4321);
//                                ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
//                                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
//                            }
//                            catch (IOException e){
//                                e.printStackTrace();
//                            }
//                            return "You wrote shit";
//
//
//                        }
//                        @Override
//                        protected void onPostExecute(String s){
//                            label.setText(s);
//                        }
//                    };
//
//
//                }
//            });
//
//
//
//        }
//
//
//        public void sendGpx(String name){
//
//
//        }
//    }