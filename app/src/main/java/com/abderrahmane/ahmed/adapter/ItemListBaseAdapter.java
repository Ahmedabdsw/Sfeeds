package com.abderrahmane.ahmed.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.io.File;
import com.abderrahmane.ahmed.sfeeds.R;

import java.util.ArrayList;

public class ItemListBaseAdapter extends BaseAdapter {
	private static ArrayList<ItemDetails> itemDetailsrrayList;

	private final Integer[] imgid = {
			R.drawable.wiam ,
			R.drawable.att ,
			R.drawable.egchwrgit ,
			R.drawable.header ,
			R.drawable.elhora ,
			R.drawable.taqadoum ,
			R.drawable.eql ,
			R.drawable.rimtoday ,
			R.drawable.taq ,
			R.drawable.ess ,
			R.drawable.almarbi3 ,
			R.drawable.akw ,
			R.drawable.atl ,
			R.drawable.bel ,
			R.drawable.mor ,
			R.drawable.amico ,
			R.drawable.tawassol ,
			R.drawable.yawm ,
			R.drawable.nawafedh ,
			R.drawable.mou ,
			R.drawable.arabi ,
			R.drawable.horyamedia ,
			R.drawable.elilam ,
			R.drawable.eljemhorya ,
			R.drawable.akhbarnass ,
			R.drawable.ekh ,
			R.drawable.ndbinfo ,
			R.drawable.inchiri ,
			R.drawable.kif ,
			R.drawable.ittissal ,
			R.drawable.rkiz ,
			R.drawable.hayat ,
			R.drawable.elh ,
			R.drawable.taw ,
			R.drawable.afa9 ,
			R.drawable.rimmedia ,
			R.drawable.ro2yapost ,
			R.drawable.mauritani ,
			R.drawable.sondage ,
			R.drawable.gorgol ,
			R.drawable.foruminfo ,
			R.drawable.echer9 ,
			R.drawable.ami ,
			R.drawable.sah ,
			R.drawable.zhr ,
			R.drawable.jedid ,
			R.drawable.myadin ,
			R.drawable.jawahir ,
			R.drawable.esabq ,
			R.drawable.mus ,
			R.drawable.ser ,
			R.drawable.watan ,
			R.drawable.amn ,
			R.drawable.hheader ,
			R.drawable.bedil ,
			R.drawable.nov ,
			R.drawable.wassit ,
			R.drawable.qavila ,
			R.drawable.tqr ,
			R.drawable.arab ,
			R.drawable.sawt ,
			R.drawable.mauriactu ,
			R.drawable.cher9today ,
			R.drawable.horouv ,
			R.drawable.la78a ,
			R.drawable.nkttnow ,
			R.drawable.elmirat ,
			R.drawable.elvetach ,
			R.drawable.mauri7 ,
			R.drawable.chingi6media ,
			R.drawable.rabi3 ,
			R.drawable.elikhbari ,
			R.drawable.zwerat ,
			R.drawable.tiguend ,
			R.drawable.legwareb ,
			R.drawable.sev ,
			R.drawable.zwinfo ,
			R.drawable.elgad ,
			R.drawable.tawatur ,
			R.drawable.sohofi ,
			R.drawable.essada ,
			R.drawable.ndbinfo2 ,
			R.drawable.afaq ,
			R.drawable.ha9i9a ,
			R.drawable.lebjawi ,
			R.drawable.mederdra ,
			R.drawable.el7adeth ,
			R.drawable.tabrenkot ,
			R.drawable.isti9lal ,
			R.drawable.tvm ,
			R.drawable.nouakchot ,
			R.drawable.essebil ,
			R.drawable.mauri13 ,
			R.drawable.mauriweb ,
			R.drawable.sa5in ,
			R.drawable.kenkoussa ,
			R.drawable.melami7 ,
			R.drawable.elwava ,
			R.drawable.elwa9i3 ,
			R.drawable.amisport ,
			R.drawable.ani ,
			R.drawable.ra2y ,
			R.drawable.mounsif ,
			R.drawable.sawtakinfo ,
			R.drawable.intivada ,
			R.drawable.becham ,
			R.drawable.el2yam ,
			R.drawable.dolvin ,
			R.drawable.sawahil ,
			R.drawable.guerew ,
			R.drawable.aqssa ,
			R.drawable.akhbrsahil ,
			R.drawable.hiyad ,
			R.drawable.maurinews ,
			R.drawable.niefrar ,
			R.drawable.rakb ,
			R.drawable.enass ,
			R.drawable.elmohit ,
			R.drawable.elbeyan ,
			R.drawable.akid ,
			R.drawable.ndbmedia ,
			R.drawable.maghreb ,
			R.drawable.nemaelan ,
			R.drawable.sahiltv ,
			R.drawable.charah ,
			R.drawable.ndbnews ,
			R.drawable.elhawadithinfo ,
			R.drawable.mithak ,
			R.drawable.sawtccher9 ,
			R.drawable.chingitty ,
			R.drawable.atla ,
			R.drawable.rayainfo ,
			R.drawable.tahalil ,
			R.drawable.biremgrein ,
			R.drawable.rimafriq ,
			R.drawable.chilo7vchi ,
			R.drawable.sadaljomhoriye ,
			R.drawable.itminane ,
			R.drawable.messarat ,
			R.drawable.azaman ,
			R.drawable.albawaba ,
			R.drawable.allaam ,
			R.drawable.elmisda9iy ,
			R.drawable.isti6la3 ,
			R.drawable.watany ,
			R.drawable.chamil ,
			R.drawable.elmsa2 ,
			R.drawable.akhbarlywm ,
			R.drawable.elbarg,
//			R.drawable.chourou9
			R.drawable.rimnewslogo

	};
	
	private LayoutInflater l_Inflater;
	private Context c;
	private List <String> lis;

	public ItemListBaseAdapter(Context context, List <String> li)
	{
// TODO Auto-generated method stub
		lis = li;
		l_Inflater = LayoutInflater.from(context);
		c = context;
	}

	public int getCount() {
		return itemDetailsrrayList.size();
	}

	public Object getItem(int position) {
		return itemDetailsrrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
		 	convertView = l_Inflater.inflate(R.layout.item_details_notification, null);
			holder = new ViewHolder();
			holder.txt_itemPrice = (TextView) convertView.findViewById(R.id.pricen);
			holder.itemImage = (ImageView) convertView.findViewById(R.id.photon);
            String s =  holder.txt_itemPrice.getText().toString();

			if(s != null) {
			//	go(holder.txt_itemPrice);
			//	back(holder.txt_itemPrice);

			}
				convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txt_itemPrice.setText(itemDetailsrrayList.get(position).getTitre1());
		holder.itemImage.setBackgroundResource(imgid[itemDetailsrrayList.get(position).getImageNumber() ]);
		Log.d("",imgid[0]+"");
		Log.d("",""+imgid[itemDetailsrrayList.get(position).getImageNumber() ]);
		return convertView;
	}

	private List<String> getSD()
	{
		List <String> it = new ArrayList <String>();
		File f = new File ("/mnt/sdcard/picture/sfeeds");
		File[] files = f.listFiles ();
		for (int i = 0; i <files.length; i++)
		{
			File file = files[i];
			Log.d("Count", file.getPath());
			it.add(file.getPath());
		}
		return it;
	}

	static class ViewHolder {
		TextView txt_itemPrice;
		ImageView itemImage;
	
		}

}
