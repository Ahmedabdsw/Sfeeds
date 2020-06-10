package com.abderrahmane.ahmed.sfeeds;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import db.dbHelper;

/**
 * Created by Ahmed on 2/17/2018.
 */

public class
UpdateNews1 extends BroadcastReceiver{
    Context c;

    private static String[] sites = { "http://www.alwiam.info/taxonomy/term/1/feed","http://www.atlasinfo.info/taxonomy/term/1/feed","http://aghchorguit.info/taxonomy/term/1/feed"
            ,"http://essahraa.net/?q=taxonomy/term/1/feed","http://elhora.info/?feed=rss2","http://taqadoum.mr/taxonomy/term/1/feed"
            ,"http://www.aqlame.com/spip.php?page=backend","http://rimtoday.net/?q=taxonomy/term/17/feed","http://www.taqadoumi.net/index.php?format=feed&type=rss"
            ,"http://essaha.info/taxonomy/term/1/feed","http://www.al-maraabimedias.net/?feed=rss2","http://www.akhbarw.net/feed/"
            ,"http://atlanticmedia.info/?q=rss.xml","https://www.bellewarmedia.com/feed/","http://elmourageb.com/feed","http://amicinfo.com/?q=rss.xml"
            ,"http://feeds.feedburner.com/tawassoul/gpvU","http://elyowm.info/?q=taxonomy/term/1/feed","http://nawafedh.com/?q=taxonomy/term/1/feed"
            ,"http://mourassiloun.com/taxonomy/term/6/all/feed","http://alaraby.info/taxonomy/term/12/feed","http://houriyamedia.info/rss.xml"
            ,"http://elilam.net/rss.xml","http://www.eljemhourriya.info/taxonomy/term/1/feed","http://akhbarnass.net/taxonomy/term/1/feed"
            ,"http://alakhbar.info/?q=taxonomy/term/1/feed","http://nouadhiboutoday.info/taxonomy/term/1/feed","http://inchiri.net/taxonomy/term/1/feed"
            ,"http://kiffainfo.net/spip.php?page=backend","http://elitissal.net/feed","http://www.rkizinfo.com/taxonomy/term/1/feed"
            ,"http://hayatte.info/taxonomy/term/1/feed","http://www.elhourriya.net/feed","http://tawary.com/spip.php?page=backend"
            ,"http://aloufouq.org/feed","http://www.rimmedia.net/spip.php?page=backend"
            ,"http://royapost.net/feed/","http://www.elmouritany.info/taxonomy/term/12/feed","http://sondagemr.net/?feed=rss2"
            ,"http://gorgolinfo.net/feed/","http://foruminfo.info/taxonomy/term/1/feed"
            ,"http://echourouqmedia.net/?q=taxonomy/term/1/feed","http://www.ami.mr/rss.xml","https://www.saharamedias.net/feed/"
            ,"http://www.zahraa.mr/taxonomy/term/1/feed","http://jedidtoday.com/taxonomy/term/1/feed","http://meyadin.net/taxonomy/term/1/feed"
            ,"http://www.eljewahir.com/?q=taxonomy/term/9758/feed","http://essabq.info/rss.xml","http://mushahide.com/rss.xml"
            ,"http://essirage.net/taxonomy/term/1/feed","http://elwatan.info/rss.xml","http://mauripress.net/taxonomy/term/1/feed"
            ,"http://elilami.info/index.php?format=feed&type=rss","http://www.elbadil.info/2013/index.php?format=feed&type=rss"
            ,"http://www.28novembre.info/taxonomy/term/998/feed"
            ,"http://www.elwassat.info/index.php/3amme?format=feed&type=rss"
            ,"http://elghavila.info/?feed=rss2","http://taqadoumy.co/feed/","http://allarab.info/taxonomy/term/1/feed","http://sewt.info/taxonomy/term/5/feed","http://mauriactu.info/ar/rss.xml"
            ,"http://echarghtoday.com/taxonomy/term/1/feed","http://ethaira.info/spip.php?page=backend","http://elhakika.info/category/info/feed/"
            ,"http://www.nouakchottnow.com/taxonomy/term/1/feed","http://elmiraat.info/feed/","http://elvetach.info/taxonomy/term/3743/feed","http://mauri7.info/ar/feed/"
            ,"http://chinguitmedia.com/feed/","http://www.rabi3.net/feed/","http://alikhbari.net/taxonomy/term/15/feed"
            ,"http://zoueratemedia.info/taxonomy/term/1/feed","http://tiguend.com/feed/","http://legwareb.info/taxonomy/term/1/feed","http://essevir.mr/rss.xml"
            ,"http://zouerate.info/rss.xml","http://alqad.info/rss.xml","http://www.tawatur.net/index.php?option=com_obrss&task=feed&id=4&format=feed"
            ,"http://souhoufi.com/spip.php?page=backend","http://www.essada.info/feed/","http://nouadibou.info/news.feed?type=rss","http://www.elafaq.net/rss.xml"
            ,"http://www.alhakika.info/taxonomy/term/14/feed","http://lebjawi.info/index.php/ar/actualites-mauritanie?format=feed&type=rss","http://www.mederdra.net/index.php?format=feed&type=rss"
            ,"http://elhadeth.mr/taxonomy/term/1/feed","http://www.tabrenkout.com/?feed=rss2","http://elistiklal.info/taxonomy/term/1/feed"
            ,"http://tvm.mr/ar/feed/","http://nouakchot.com/?format=feed&type=rss","http://www.essebil.com/feed/"
            ,"http://mauritania13.com/taxonomy/term/1/feed","http://mauriweb.info/ar/taxonomy/term/1/feed","http://alkhabar.mr/index.php?format=feed&type=rss","http://www.kankossatoday.net/?feed=rss2"
            ,"http://www.melame7.com/feeds/posts/default?alt=rss"
            ,"http://elweva.info/index.php?format=feed&type=rss","http://alwaqie.net/feed/"
            ,"http://amisports.net/index.php?format=feed&type=rss","http://ani.mr/?q=taxonomy/term/1/feed","http://www.arayalmostenir.com/rss.xml","http://mounsif.net/feed/"
            ,"http://sawtak.info/spip.php?page=backend","http://entalfa.info/index.php?format=feed&type=rss","http://elbecham.info/rss.xml"
            ,"http://elayam.info/index.php/2016-06-15-16-32-41?format=feed&type=rss","http://doulvinepresse.com/ar/feed/","http://www.sawahell.info/index.php?format=feed&type=rss"
            ,"http://www.guerou.info/?feed=rss2&cat=2","http://alaqssa.org/?q=rss.xml","http://sahelnews.info/rss.xml"
            ,"http://www.elhiyad.info/rss.xml","https://maurinews.info/feed/","http://niefrar.org/wordpress/?feed=rss2"
            ,"http://errakb.info/feed","http://enass.info/index.php?format=feed&type=rss","http://elmohit.net/rss.xml","http://elbeyan.info/taxonomy/term/1/feed"
            ,"http://www.ekiid.com/feed/","http://ndbmedias.com/?feed=rss2"
            ,"http://newsmaghreb.info/index.php?format=feed&type=rss","http://nemaelan.info/rss.xml","http://sahel.tv/?feed=rss2"
            ,"http://echarah.info/?feed=rss2","http://www.ndbnews.info/taxonomy/term/1/feed","http://elhawadith.info/taxonomy/term/1/feed"
            ,"http://mithak.com/rss.xml","http://sawtchargh.net/feed/","http://chinguitty.net/spip.php?page=backend"
            ,"http://anbaatlas.com/taxonomy/term/1/feed","http://al-raya.info/feed/","http://www.journaltahalil.com/ar/flux.xml"
            ,"http://birmoghrein.info/index.php?format=feed&type=rss","http://rimafric.info/rss.xml","http://www.chilouhefchi.net/spip.php?page=backend"
            ,"http://sadaljomhoriye.net/rss.xml","http://www.itminane.net/feed","http://massarat.info/feed/","http://azzaman.info/index.php?format=feed&type=rss"
            ,"http://albewaba.info/taxonomy/term/16/all/feed","http://www.allaam.com/index.php?format=feed&type=rss"
            ,"http://almisdaquiya.net/?feed=rss2","http://elistitlaa.info/index.php?format=feed&type=rss","http://www.wateni.com/feed/"
            ,"http://echamel.info/?feed=rss2"
            ,"http://elmesa.info/rss.xml"
            ,"http://www.akhbarelyoum.info/?feed=rss2"
            ,"http://www.albargh.net/feed"
    };

    public final   static RssReader[] str = new RssReader[147];

    public UpdateNews1(){
        str[0] = new RssReader(sites[0],0);
        str[1] = new RssReader(sites[1],1);
        str[2] = new RssReader(sites[2],2);
        str[3] = new RssReader(sites[3],3);
        str[4] = new RssReader(sites[4],4);
        str[5] = new RssReader(sites[5],5);
        str[6] = new RssReader(sites[6],6);
        str[7] = new RssReader(sites[7],7);
        str[8] = new RssReader(sites[8],8);
        str[9] = new RssReader(sites[9],9);
        str[10] = new RssReader(sites[10],10);
        str[11] = new RssReader(sites[11],11);
        str[12] = new RssReader(sites[12],12);
        str[13] = new RssReader(sites[13],13);
        str[14] = new RssReader(sites[14],14);
        str[15] = new RssReader(sites[15],15);
        str[16] = new RssReader(sites[16],16);
        str[17] = new RssReader(sites[17],17);
        str[18] = new RssReader(sites[18],18);
        str[19] = new RssReader(sites[19],19);
        str[20] = new RssReader(sites[20],20);
        str[21] = new RssReader(sites[21],21);
        str[22] = new RssReader(sites[22],22);
        str[23] = new RssReader(sites[23],23);
        str[24] = new RssReader(sites[24],24);
        str[25] = new RssReader(sites[25],25);
        str[26] = new RssReader(sites[26],26);
        str[27] = new RssReader(sites[27],27);
        str[28] = new RssReader(sites[28],28);
        str[29] = new RssReader(sites[29],29);
        str[30] = new RssReader(sites[30],30);
        str[31] = new RssReader(sites[31],31);
        str[32] = new RssReader(sites[32],32);
        str[33] = new RssReader(sites[33],33);
        str[34] = new RssReader(sites[34],34);
        str[35] = new RssReader(sites[35],35);
        str[36] = new RssReader(sites[36],36);
        str[37] = new RssReader(sites[37],37);
        str[38] = new RssReader(sites[38],38);
        str[39] = new RssReader(sites[39],39);
        str[40] = new RssReader(sites[40],40);
        str[41] = new RssReader(sites[41],41);
        str[42] = new RssReader(sites[42],42);
        str[43] = new RssReader(sites[43],43);
        str[44] = new RssReader(sites[44],44);
        str[45] = new RssReader(sites[45],45);
        str[46] = new RssReader(sites[46],46);
        str[47] = new RssReader(sites[47],47);
        str[48] = new RssReader(sites[48],48);
        str[49] = new RssReader(sites[49],49);
        str[50] = new RssReader(sites[50],50);
        str[51] = new RssReader(sites[51],51);
        str[52] = new RssReader(sites[52],52);
        str[53] = new RssReader(sites[53],53);
        str[54] = new RssReader(sites[54],54);
        str[55] = new RssReader(sites[55],55);
        str[56] = new RssReader(sites[56],56);
        str[57] = new RssReader(sites[57],57);
        str[58] = new RssReader(sites[58],58);
        str[59] = new RssReader(sites[59],59);
        str[60] = new RssReader(sites[60],60);
        str[61] = new RssReader(sites[61],61);
        str[62] = new RssReader(sites[62],62);
        str[63] = new RssReader(sites[63],63);
        str[64] = new RssReader(sites[64],64);
        str[65] = new RssReader(sites[65],65);
        str[66] = new RssReader(sites[66],66);
        str[67] = new RssReader(sites[67],67);
        str[68] = new RssReader(sites[68],68);
        str[69] = new RssReader(sites[69],69);
        str[70] = new RssReader(sites[70],70);
        str[71] = new RssReader(sites[71],71);
        str[72] = new RssReader(sites[72],72);
        str[73] = new RssReader(sites[73],73);
        str[74] = new RssReader(sites[74],74);
        str[75] = new RssReader(sites[75],75);
        str[76] = new RssReader(sites[76],76);
        str[77] = new RssReader(sites[77],77);
        str[78] = new RssReader(sites[78],78);
        str[79] = new RssReader(sites[79],79);
        str[80] = new RssReader(sites[80],80);
        str[81] = new RssReader(sites[81],81);
        str[82] = new RssReader(sites[82],82);
        str[83] = new RssReader(sites[83],83);
        str[84] = new RssReader(sites[84],84);
        str[85] = new RssReader(sites[85],85);
        str[86] = new RssReader(sites[86],86);
        str[87] = new RssReader(sites[87],87);
        str[88] = new RssReader(sites[88],88);
        str[89] = new RssReader(sites[89],89);
        str[90] = new RssReader(sites[90],90);
        str[91] = new RssReader(sites[91],91);
        str[92] = new RssReader(sites[92],92);
        str[93] = new RssReader(sites[93],93);
        str[94] = new RssReader(sites[94],94);
        str[95] = new RssReader(sites[95],95);
        str[96] = new RssReader(sites[96],96);
        str[97] = new RssReader(sites[97],97);
        str[98] = new RssReader(sites[98],98);
        str[99] = new RssReader(sites[99],99);
        str[100] = new RssReader(sites[100],100);
        str[101] = new RssReader(sites[101],101);
        str[102] = new RssReader(sites[102],102);
        str[103] = new RssReader(sites[103],103);
        str[104] = new RssReader(sites[104],104);
        str[105] = new RssReader(sites[105],105);
        str[106] = new RssReader(sites[106],106);
        str[107] = new RssReader(sites[107],107);
        str[108] = new RssReader(sites[108],108);
        str[109] = new RssReader(sites[109],109);
        str[110] = new RssReader(sites[110],110);
        str[111] = new RssReader(sites[111],111);
        str[112] = new RssReader(sites[112],112);
        str[113] = new RssReader(sites[113],113);
        str[114] = new RssReader(sites[114],114);
        str[115] = new RssReader(sites[115],115);
        str[116] = new RssReader(sites[116],116);
        str[117] = new RssReader(sites[117],117);
        str[118] = new RssReader(sites[118],118);
        str[119] = new RssReader(sites[119],119);
        str[120] = new RssReader(sites[120],120);
        str[121] = new RssReader(sites[121],121);
        str[122] = new RssReader(sites[122],122);
        str[123] = new RssReader(sites[123],123);
        str[124] = new RssReader(sites[124],124);
        str[125] = new RssReader(sites[125],125);
        str[126] = new RssReader(sites[126],126);
        str[127] = new RssReader(sites[127],127);
        str[128] = new RssReader(sites[128],128);
        str[129] = new RssReader(sites[129],129);
        str[130] = new RssReader(sites[130],130);
        str[131] = new RssReader(sites[131],131);
        str[132] = new RssReader(sites[132],132);
        str[133] = new RssReader(sites[133],133);
        str[134] = new RssReader(sites[134],134);
        str[135] = new RssReader(sites[135],135);
        str[136] = new RssReader(sites[136],136);
        str[137] = new RssReader(sites[137],137);
        str[138] = new RssReader(sites[138],138);
        str[139] = new RssReader(sites[139],139);
        str[140] = new RssReader(sites[140],140);
        str[141] = new RssReader(sites[141],141);
        str[142] = new RssReader(sites[142],142);
        str[143] = new RssReader(sites[143],143);
        str[144] = new RssReader(sites[144],144);
        str[145] = new RssReader(sites[145],145);
        str[146] = new RssReader(sites[146],146);

 }


    @Override
    public void onReceive(Context context, Intent intent) {
        c = context;


        new Thread(new Runnable() {
            public void run() {
                str[26].run();
                str[28].run();
                str[36].run();
                str[37].run();
                str[41].run();
                str[45].run();
                str[46].run();
                str[52].run();
                str[56].run();
                str[57].run();
                str[60].run();
                str[72].run();
                str[75].run();
                str[76].run();
                str[85].run();
                str[89].run();
                str[90].run();
                str[92].run();
                str[101].run();
                str[105].run();
                str[121].run();
                str[21].run();
                str[22].run();
                str[29].run();
                str[32].run();
                str[35].run();
                str[38].run();
                str[42].run();
                str[48].run();
                str[51].run();
                str[53].run();
                str[55].run();
                str[59].run();
                str[63].run();
                str[65].run();
                str[68].run();
                str[70].run();
                str[71].run();
                str[77].run();
                str[80].run();
            }
        }).start();
    }

    }
