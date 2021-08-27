package com.matiasep.proveex.SQLite.nuevo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.matiasep.proveex.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ProductosAdapterN extends RecyclerView.Adapter<ProductosAdapterN.MyViewHolder>{

    private ArrayList<ProductoN> listaProductosLn;
    private ArrayList<ProductoN> originalItems;


    public ProductosAdapterN(ArrayList<ProductoN>listaProductos){
        this.listaProductosLn=listaProductos;
        originalItems = new ArrayList<>();
        originalItems.addAll(listaProductos);
    }

    @Override
    public ProductosAdapterN.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,null,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        /*vista.setOnClickListener(this);*/
        return new MyViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.txtCodigo.setText(listaProductosLn.get(position).getCodigo().toString());
        holder.txtNombre.setText(listaProductosLn.get(position).getNombre());
        holder.txtPrecioCosto.setText(listaProductosLn.get(position).getPreciocosto());
        holder.txtPrecioVenta.setText(listaProductosLn.get(position).getPrecioventa());
        holder.txtFabricante.setText(listaProductosLn.get(position).getFabricante());

    }

    @Override
    public int getItemCount() {
        return listaProductosLn.size();
    }

    public void filter(String svSearch) {
        int longitud = svSearch.length();
        if (longitud == 0) {
            listaProductosLn.clear();
            listaProductosLn.addAll(originalItems);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<ProductoN> collect = listaProductosLn.stream()
                        .filter(i -> i.getNombre().toLowerCase().contains(svSearch.toLowerCase()))
                        .collect(Collectors.toList());

                listaProductosLn.clear();
                listaProductosLn.addAll(collect);
            }
            else {
                listaProductosLn.clear();
                for (ProductoN i : originalItems) {
                    if (i.getNombre().toLowerCase().contains(svSearch.toLowerCase())) {
                        listaProductosLn.add(i);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView txtCodigo, txtNombre, txtPrecioCosto, txtPrecioVenta, txtFabricante;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtCodigo= (TextView) itemView.findViewById(R.id.txtCodigo);
            txtNombre= (TextView) itemView.findViewById(R.id.txtProducto);
            txtPrecioCosto= (TextView) itemView.findViewById(R.id.txtPreciolc);
            txtPrecioVenta= (TextView) itemView.findViewById(R.id.txtPreciolv);
            txtFabricante= (TextView) itemView.findViewById(R.id.txtFabricante);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, DetalleProductoN.class);
                    intent.putExtra("CO", listaProductosLn.get(getAdapterPosition()).getCodigo());
                    context.startActivity(intent);
                }
            });*/
        }
    }


}
