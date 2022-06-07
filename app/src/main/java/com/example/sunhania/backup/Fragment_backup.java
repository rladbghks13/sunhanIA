package com.example.sunhania.backup;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sunhania.MainActivity;
import com.example.sunhania.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Fragment_backup extends Fragment {

    private View view;
    private String gshhis = null, zetta = null, pacs = null;
    ImageView gshhis_img , zetta_img , pacs_img ;




    private String TAG = "백업체크";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "백업체크 프래그먼트");
        view = inflater.inflate(R.layout.fragment_backup, container, false);
        Log.i("log", String.valueOf(MainActivity.bottomNavigationView.getSelectedItemId()));


        loadBackupFlg();
        setBackupImage();
        return view;
    }

    public void loadBackupFlg() {
        BackupRequest task = new BackupRequest();

        try{
            JSONArray jsonArray;
            String result;
            result = task.execute().get();
            JSONObject jsonMain = new JSONObject(result);
            jsonArray = jsonMain.getJSONArray("backupcheck");


            for(int i = 0;  i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                gshhis = jsonObject.getString("1");
                zetta = jsonObject.getString("2");
                pacs = jsonObject.getString("3");
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setBackupImage(){
        gshhis_img = view.findViewById(R.id.exp_backup_img);
        zetta_img = view.findViewById(R.id.zetta_backup_img);
        pacs_img = view.findViewById(R.id.pacs_backup_img);


        if (gshhis.equals("0")){
            gshhis_img.setImageResource(R.drawable.ic_baseline_do_disturb_on_24);
        }
        else {
            gshhis_img.setImageResource(R.drawable.ic_baseline_check_circle_24_green);
        }

        if (zetta.equals("0")){
            zetta_img.setImageResource(R.drawable.ic_baseline_do_disturb_on_24);
        }
        else {
            zetta_img.setImageResource(R.drawable.ic_baseline_check_circle_24_green);
        }

        if (pacs.equals("0")){
            pacs_img.setImageResource(R.drawable.ic_baseline_do_disturb_on_24);
        }
        else {
            pacs_img.setImageResource(R.drawable.ic_baseline_check_circle_24_green);
        }


    }

}
