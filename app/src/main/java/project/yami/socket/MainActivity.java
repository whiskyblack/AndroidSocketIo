package project.yami.socket;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Random;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    Socket socket;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random=new Random();

        try {
            socket= IO.socket("http://192.168.253.2:1234");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
    }

    public void connect(View view) {
        try {
            final Handler handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Toast.makeText(getBaseContext(), msg.getData().getString("tong"), Toast.LENGTH_LONG).show();
                }
            };

            socket.emit("register", "agd");
            socket.on("register-success", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i("SERVER", (args[0]).toString());
                    Toast.makeText(getBaseContext(), "jsnvkacj", Toast.LENGTH_LONG).show();
                }
            });

            socket.on("accept", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i("SOCKETRETURN", args[0].toString());
                }
            });

            socket.on("tong", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Bundle bundle=new Bundle();
                    bundle.putString("tong", args[0].toString());
                    Message message=new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            });

            JSONObject object=new JSONObject();
            try {
                object.put("soa", random.nextInt(100));
                object.put("sob", random.nextInt(200));
                Log.i("SOCKETRETURN", object.toString());
                socket.emit("tong", object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
