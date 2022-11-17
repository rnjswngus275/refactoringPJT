package com.example.yoony.opensourceandroidproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentShop extends Fragment {

    GridView gridView;
    EditText editText;
    EditText editText2;
    Button button;
    SingerAdapter singerAdapter;
    ArrayList<SingerShopItem> items = new ArrayList<SingerShopItem>();
    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        dbHelper = new DBHelper(view.getContext(), "QuestApp.db", null, 1);

        if (dbHelper.isEmptyShopItem() == 0) {
            createShopList();
        }

        gridView = (GridView) view.findViewById(R.id.gridView);
        editText = (EditText) view.findViewById(R.id.editText);
        editText2 = (EditText) view.findViewById(R.id.editText2);
        button = (Button) view.findViewById(R.id.button);

        singerAdapter = new SingerAdapter();

        gridView.setAdapter(singerAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("구매하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (dbHelper.selectUserpoint() < items.get(i).getCost()) {
                                    Toast.makeText(getActivity().getApplicationContext(), (items.get(i).getCost() - dbHelper.selectUserpoint()) + "포인트가 부족하여 구매할 수 없습니다.", Toast.LENGTH_LONG).show();
                                } else {
                                    dbHelper.minusUserPoint(items.get(i).getCost());
                                    Toast.makeText(getActivity().getApplicationContext(), items.get(i).getCost() + "포인트를 지불하여 구매하였습니다.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity().getApplicationContext(), "아니오를 선택했습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                builder.show();
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("삭제하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.deleteShopItem(items.get(i).getName());
                                Toast.makeText(getActivity().getApplicationContext(), items.get(i).getName() + "아이템을 삭제하였습니다.", Toast.LENGTH_LONG).show();
                                items.remove(i);
                                singerAdapter.notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity().getApplicationContext(), "아니오를 선택했습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                builder.show();
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString().trim();
                int cost = Integer.parseInt(editText2.getText().toString().trim());
                items.add(new SingerShopItem(name, cost, getResources().getIdentifier("index" + items.size(), "drawable", getContext().getPackageName())));
                singerAdapter.notifyDataSetChanged();
                dbHelper.setShopItem(name, cost);
                //singerAdapter.addItem(new SingerShopItem(name, cost, R.drawable.gift));
            }
        });

        return view;
    }

    public void createShopList() {
        items.clear();
        String temp[] = dbHelper.selectShopItem().split("\n");
        String data[][] = new String[2][temp.length];
        for (int i = 0; i < temp.length; i++) {
            for (int k = 0; k < 2; k++) {
                data[k][i] = temp[i].split("\\|")[k];
            }
            Log.e("sangeun", data[0][i]);
            Log.e("sangeun", data[1][i]);
            items.add(new SingerShopItem(data[0][i], Integer.parseInt(data[1][i]), getResources().getIdentifier("index" + i, "drawable", getContext().getPackageName())));
        }
    }

    class SingerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(SingerShopItem singerItem) {
            items.add(singerItem);
        }

        @Override
        public SingerShopItem getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            SingerViewer singerViewer = new SingerViewer(getActivity().getApplicationContext());
            singerViewer.setItem(items.get(i));
            return singerViewer;
        }
    }
}
