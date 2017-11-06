package com.sas.rh.reimbursehelper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class EnterpriseFragment extends Fragment {

    LinearLayout emb,smb,mmb,dmb,apb,pmb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enterprise, container, false);
        initview(view);
        return view;
    }

    private void initview(View view){
        emb = (LinearLayout) view.findViewById(R.id.enterprisedetail_btn);
        smb = (LinearLayout) view.findViewById(R.id.subjectsmanage_btn);
        mmb = (LinearLayout) view.findViewById(R.id.membersmanage_btn);
        dmb = (LinearLayout) view.findViewById(R.id.departmentsmanage_btn);
        apb = (LinearLayout) view.findViewById(R.id.approveprocedure_btn);
        pmb = (LinearLayout) view.findViewById(R.id.projectmanage_btn);

        emb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(),EnterpriseDetailActivity.class);
                startActivity(it);
            }
        });
        smb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(),SubjectsManagerActivity.class);
                startActivity(it);
            }
        });
        mmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(),MembersManageActivity.class);
                startActivity(it);
            }
        });
        dmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(),DepartmentsManageActivity.class);
                startActivity(it);
            }
        });
        apb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(),ApproveProcedureActivity.class);
                startActivity(it);
            }
        });
        pmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(),ProjectsManagerActivity.class);
                startActivity(it);
            }
        });
    }



}
