String isPSD = "psd";
String isSSP = "ssp";
String isTIF = "tif";

void stringsMagic(ArrayList<File> list) {
  String[] all = new String[list.size()];
  String[] psd = new String[list.size()];
  String[] ssp = new String[list.size()];
  String[] tif = new String[list.size()];

  int allcount = 0;
  for (File t: list) {
    all[allcount] = t.getName();
    allcount++;
  }

  int psdcount = 0;
  int sspcount = 0;
  int tifcount = 0;
  for (int i = 0 ; i < all.length ; i++) {
    String que = all[i].substring(all[i].length()-3, all[i].length());
    if (que.equals(isPSD)) {
      psd[psdcount]  = all[i];
      psdcount++;
    } else if (que.equals(isSSP)) {
      ssp[psdcount]  = all[i];
      sspcount++;
    } else if (que.equals(isTIF)) {
      tif[tifcount]  = all[i];
      tifcount++;
    }
  }

  tif_nopsd = new String[psd.length];
  int tif_nopsd_count = 0;
  tif_nossp = new String[0];
  String[] psdSources = new String[psd.length];

  String ttt, ppp, sss;

  for(int i = 0 ; i < psd.length ; i++) {
    ppp =  psd[i].substring(0, psd[i].length()-4);
    psdSources[i] = ppp;
    for (int j = 0 ; j < tif.length ; j++) {
      ttt = tif[j].substring(0,psdSources[i].length());
      if ( !(psdSources[i].equals(ttt)) ) {
        tif_nopsd[tif_nopsd_count] = ttt;
        tif_nopsd_count++;
        println(tif_nopsd[tif_nopsd_count]);
      }
    }
  }

}
