package com.myapps.sdr.firebase_multiupload;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int RESULT_LOAD = 1;
    StorageReference storageReference;
    Button button1;
    RecyclerView recyclerView;
    private List<String> list;
    private List<String> listdone;
    private UploadList uploadList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storageReference = FirebaseStorage.getInstance().getReference();
        button1=findViewById(R.id.button);
        recyclerView=findViewById(R.id.recycler);
        list = new ArrayList<>();
        uploadList = new UploadList(list,listdone);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(uploadList);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent,"Select a file"),RESULT_LOAD);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD && resultCode==RESULT_OK)
        {
            if(data.getClipData()!=null)
            {
                for(int i=0;i<data.getClipData().getItemCount();++i)
                {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    String filename = getname(uri);
                    list.add(filename);
                    uploadList.notifyDataSetChanged();
                    StorageReference uploadref = storageReference.child("My Files").child(filename);
                    uploadref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MainActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                Toast.makeText(MainActivity.this,"Selected multiple files",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String getname(Uri uri)
    {
        String res = null;
        if(uri.getScheme().equals("content"))
        {
            Cursor cursor = getContentResolver().query(uri,null,null,null,null);
            try
            {
                if(cursor!=null && cursor.moveToFirst())
                {
                    res = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }finally {
                cursor.close();
            }
        }
        return res;
    }
}
