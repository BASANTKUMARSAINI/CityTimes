package services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mycity.NotificationActivity;
import com.example.mycity.R;
import com.firebase.ui.firestore.paging.FirestoreDataSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import model.Notification;
import seller.StoreActivity;

public class MyMessagingService extends FirebaseMessagingService {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

//    private void setNotificationToDatabase(String title,String message) {
//        String date=new SimpleDateFormat("yyyy:MM:dd", Locale.getDefault()).format(new Date());
//        Notification notification=new Notification();
//        notification.setDate(date);
//        notification.setMessage(message);
//        notification.setTitle(title);
//Log.d("TAG","inside");
//        db.collection("notifications").document(mAuth.getUid())
//                .collection("notification").add(notification).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentReference> task) {
//                Log.d("TAG","task");
//                SharedPreferences sharedPreferences=getSharedPreferences("MyData",MODE_PRIVATE);
//                int count=0;
//                count=sharedPreferences.getInt("notification",0);
//                SharedPreferences.Editor editor=sharedPreferences.edit();
//                editor.putInt("notification",count+1);
//            }
//        });
//    }
    public void   showNotification(String title,String message)
    {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"MyNotification")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_notifications)
                .setAutoCancel(true)
                .setContentText(message)
                .setVibrate(new long[]{100,200,300})
                ;
        Intent notificationIntent=new Intent(this, NotificationActivity.class);
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);
        managerCompat.notify(999,builder.build());







    }

}
