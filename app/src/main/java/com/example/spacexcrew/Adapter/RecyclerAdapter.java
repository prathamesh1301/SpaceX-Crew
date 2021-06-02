package com.example.spacexcrew.Adapter;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spacexcrew.DAO.CrewDAO;
import com.example.spacexcrew.Database.CrewDatabase;
import com.example.spacexcrew.Model.CrewDetails;
import com.example.spacexcrew.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<CrewDetails> itemList; //crewDetails list
    private CrewDatabase crewDatabase;


    public RecyclerAdapter(Context context, List<CrewDetails> itemList,Application application) {
        this.context = context;
        this.itemList = itemList;
        this.crewDatabase=CrewDatabase.getCrewDatabaseInstance(application);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.crew_member_layout,parent,false);

        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        CrewDetails crewDetails=itemList.get(position);
        holder.name.setText("Name: "+crewDetails.getName());
        holder.agency.setText("Agency: "+crewDetails.getAgency());
        holder.wiki.setText(crewDetails.getWikipedia());
        holder.status.setText("Status: "+crewDetails.getStatus());
        Glide.with(context).load(crewDetails.getImage()).into(holder.img);

        holder.wiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=holder.wiki.getText().toString().trim();
                gotoURL(url);
            }
        });
        holder.memberCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialogBoxDelete=new AlertDialog.Builder(context);
                alertDialogBoxDelete.setTitle("Delete");
                alertDialogBoxDelete.setMessage("Are you sure?");
                alertDialogBoxDelete.setCancelable(true);
                alertDialogBoxDelete.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new DeleteAsync(crewDatabase).execute(crewDetails.getID());
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                alertDialogBoxDelete.show();
                notifyDataSetChanged();

                return true;
            }
        });


    }

    private void gotoURL(String url) {
        Uri uri=Uri.parse(url);
        context.startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public void setAllData(List<CrewDetails> itemList){
        this.itemList=itemList;
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,agency,wiki,status;
        ImageView img;
        CardView memberCardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.memberName);
            agency=itemView.findViewById(R.id.memberAgency);
            img=itemView.findViewById(R.id.memberImg);
            wiki=itemView.findViewById(R.id.memberWiki);
            status=itemView.findViewById(R.id.memberStatus);
            memberCardView=itemView.findViewById(R.id.memberCardView);
        }
    }
    static class DeleteAsync extends AsyncTask<Integer,Void,Void>{
        private CrewDAO crewDAO;

        public DeleteAsync(CrewDatabase crewDatabase) {
            this.crewDAO = crewDatabase.crewDao();
        }


        @Override
        protected Void doInBackground(Integer... integers) {
            crewDAO.deleteCrewMember(integers[0]);
            return null;
        }
    }
}
