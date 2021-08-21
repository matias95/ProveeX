package com.matiasep.proveex.webservice;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.matiasep.proveex.R;

import java.util.List;


public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.MyViewHolder> implements View.OnClickListener{
    //private String[] mDataset;
    //private Context mCtx;
    private List<Producto> listaProductos;
    private View.OnClickListener listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public ProductosAdapter(List<Producto>listaProductos){
        //this.mCtx=mCtx;
        this.listaProductos=listaProductos;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView txtCodigo, txtDescripcion, txtPrecioCosto, txtPrecioVenta, txtFabricante;
        ImageView imagen;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtCodigo= (TextView) itemView.findViewById(R.id.txtCodigo);
            txtDescripcion= (TextView) itemView.findViewById(R.id.txtProducto);
            txtPrecioCosto= (TextView) itemView.findViewById(R.id.txtPreciolc);
            txtPrecioVenta= (TextView) itemView.findViewById(R.id.txtPreciolv);
            txtFabricante= (TextView) itemView.findViewById(R.id.txtFabricante);
            imagen=(ImageView) itemView.findViewById(R.id.idImagen);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    /*public ProductosAdapter(String[] myDataset) {
        mDataset = myDataset;
    }*/

    // Create new views (invoked by the layout manager)
    @Override
    public ProductosAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        vista.setOnClickListener(this);
        return new MyViewHolder(vista);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //Producto producto= listaProductos.get(position);
        holder.txtCodigo.setText(listaProductos.get(position).getCodigo().toString());
        holder.txtDescripcion.setText(listaProductos.get(position).getDescripcion().toString());
        holder.txtPrecioCosto.setText(listaProductos.get(position).getPreciocosto().toString());
        holder.txtPrecioVenta.setText(listaProductos.get(position).getPrecioventa().toString());
        holder.txtFabricante.setText(listaProductos.get(position).getFabricante().toString());

        if(listaProductos.get(position).getImagen()!=null){
            holder.imagen.setImageBitmap(listaProductos.get(position).getImagen());
        }else {
            holder.imagen.setImageResource(R.drawable.sinimagen);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
}
