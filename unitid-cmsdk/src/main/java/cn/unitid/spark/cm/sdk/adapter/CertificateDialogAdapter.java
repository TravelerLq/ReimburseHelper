package cn.unitid.spark.cm.sdk.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.unitid.spark.cm.sdk.R;
import cn.unitid.spark.cm.sdk.data.entity.Certificate;

import java.util.List;

/**
 * 证书列表数据
 */
public class CertificateDialogAdapter extends BaseAdapter {
    private List<Certificate> dataSource;
    private LayoutInflater inflater;

    public CertificateDialogAdapter(FragmentActivity context, List<Certificate> dataSource) {
        this.dataSource = dataSource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Certificate certificate = dataSource.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_certificate_choose, parent, false);
            holder.nameView = (TextView) convertView.findViewById(R.id.item_certificate_choose_ca);
            holder.aliasView = (TextView) convertView.findViewById(R.id.item_certificate_choose_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameView.setText(certificate.getName());
        holder.aliasView.setText(certificate.getAlias());
        return convertView;
    }

    private class ViewHolder {
        public TextView nameView;
        public TextView aliasView;
    }
}
