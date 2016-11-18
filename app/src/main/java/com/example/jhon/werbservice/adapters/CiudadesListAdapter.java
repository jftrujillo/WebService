package com.example.jhon.werbservice.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jhon.werbservice.R;
import com.example.jhon.werbservice.models.Ciudades;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by jhon on 16/11/16.
 */

public class CiudadesListAdapter extends BaseAdapter {
    List<Ciudades> data;
    Context context;

    public CiudadesListAdapter(List<Ciudades> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null){
            v = View.inflate(context, R.layout.template_list_ciudades,null);
        }
        TextView ciudad = (TextView) v.findViewById(R.id.ciudad);
        TextView pais = (TextView) v.findViewById(R.id.pais);
        ciudad.setText(data.get(position).getCiudad());
        pais.setText(data.get(position).getPais());
        return v;
    }
}
