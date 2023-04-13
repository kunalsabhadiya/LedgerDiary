package com.example.Ledgerdiary.onlinecustomer;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.Ledgerdiary.R;

import com.example.Ledgerdiary.reminder.occurancemodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Header;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class selectedtransactionview extends AppCompatActivity {
RecyclerView pdfrc;
String startdate,enddtae,senderroom,name,number;

TextView selectedname,selectednumber,selectedstartdate,selectedenddate,savepdf;
long endtimestamp,starttimestamp;
ArrayList<pdfmodel> list;
pdfadapter pdfadapter;
RelativeLayout pdflayout;
int total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedtransactionview);

        pdfrc=findViewById(R.id.pdfrc);
        selectedenddate=findViewById(R.id.selectedenddate);
        selectedname=findViewById(R.id.selectedpdfname);
        selectednumber=findViewById(R.id.selectednumber);
        selectedstartdate=findViewById(R.id.selectedstartdate);
        savepdf=findViewById(R.id.sabepdf);
        pdflayout=findViewById(R.id.pdflayout);



        startdate=getIntent().getStringExtra("startdate");
        enddtae=getIntent().getStringExtra("enddate");
        senderroom=getIntent().getStringExtra("senderroom");
        name=getIntent().getStringExtra("name");
        number=getIntent().getStringExtra("number");

        selectedstartdate.setText(startdate);
        selectedenddate.setText(enddtae);
        selectedname.setText(name);
        selectednumber.setText(number);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1=null,date2 = null;
        try {
            date1 = dateFormat.parse(startdate);
            date2 = dateFormat.parse(enddtae);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date1 != null && date2!=null) {
            starttimestamp = date1.getTime() ;
            endtimestamp = date2.getTime();

        }

        list=new ArrayList<>();
        pdfrc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        savepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to open the file manager
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent, 1);

// In the onActivityResult() method, get the selected directory and save the PDF file to it


            }
        });
        FirebaseDatabase.getInstance().getReference().child("transactions")
                .child(senderroom).orderByChild("timestamp").startAt(starttimestamp)
                .endAt(endtimestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        if(snapshot.exists()){
                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                pdfmodel model=snapshot1.getValue(pdfmodel.class);
                                if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(model.getSenderId())){
                                    total+=Integer.parseInt(model.getAmount());
                                }else{
                                    total-=Integer.parseInt(model.getAmount());
                                }

                                model.setTotal(total);
                                list.add(model);
                            }
                        }

                       pdfadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        pdfadapter=new pdfadapter(getApplicationContext(),list);
       pdfrc.setAdapter(pdfadapter);





    }
    private Bitmap combineScreenshots(List<Bitmap> screenshots,int totalHeight) {
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        Bitmap combinedBitmap = Bitmap.createBitmap(screenWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combinedBitmap);

        int currentHeight = 0;
        for (Bitmap screenshot : screenshots) {
            canvas.drawBitmap(screenshot, 0, currentHeight, null);
            currentHeight += screenshot.getHeight();
        }

        return combinedBitmap;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri treeUri = data.getData();
            if (treeUri != null) {
                DocumentFile pickedDir = DocumentFile.fromTreeUri(this, treeUri);
                if (pickedDir != null && pickedDir.canWrite()) {
                    try {
                        DocumentFile pdfFile = pickedDir.createFile("application/pdf", "my_document.pdf");
                        if (pdfFile != null) {
                            OutputStream outputStream = getContentResolver().openOutputStream(pdfFile.getUri());
                            Document dOcument=new Document();
                            PdfWriter.getInstance(dOcument, outputStream);
                            dOcument.open();
                            
                            PdfPTable table = new PdfPTable(5);
                            table.setWidthPercentage(100);
                            table.setSpacingBefore(10f);
                            table.setSpacingAfter(10f);

                            PdfPCell cell1 = new PdfPCell(new Paragraph("Transaction Date"));
                            PdfPCell cell2 = new PdfPCell(new Paragraph("Description"));
                            PdfPCell cell3 = new PdfPCell(new Paragraph("Send"));
                            PdfPCell cell4 = new PdfPCell(new Paragraph("Receive"));
                            PdfPCell cell5 = new PdfPCell(new Paragraph("Total"));

                            table.addCell(cell1);
                            table.addCell(cell2);
                            table.addCell(cell3);
                            table.addCell(cell4);
                            table.addCell(cell5);

                            for (pdfmodel transaction : list) {
                                table.addCell(transaction.getDate());
                                table.addCell(transaction.getDescription());
                                if(Objects.equals(FirebaseAuth.getInstance().getUid(), transaction.getSenderId())){
                                    table.addCell(transaction.getAmount());
                                    table.addCell("-");
                                }else{
                                    table.addCell("-");
                                    table.addCell(transaction.getAmount());
                                }
                                table.addCell(String.valueOf(transaction.getTotal()));
                            }

                            dOcument.add(table);
                            dOcument.close();

                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }



}