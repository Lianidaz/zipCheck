String[] exts = {"psd","spp","tif"};

String isPSD = "psd";
String isspp = "spp";
String isTIF = "tif";

void stringsMagic(ArrayList<File> list) {
  ArrayList<String> all = new ArrayList<String>();
  ArrayList<String> psd = new ArrayList<String>();
  ArrayList<String> spp = new ArrayList<String>();
  ArrayList<String> tif = new ArrayList<String>();

  // int allcount = 0;
  for (File t: list) {
    all.add(t.getName());
    // allcount++;
  }

  // int psdcount = 0;
  // int sppcount = 0;
  // int tifcount = 0;
  for (String s : all) {
    if (s.endsWith("psd")) {
      psd.add(s.substring(0, s.length()-4));
      // psdcount++;
    } else if (s.endsWith("spp")) {
      spp.add(s.substring(0, s.length()-4));
      // sppcount++;
    } else if (s.endsWith("tif")) {
      String ti = s.substring(0, s.length()-4);
      ti = ti.replaceAll("_\\w$","");
      if (!tif.contains(ti)) {
        tif.add(ti);
      }
      // tifcount++;
    }
  }

  println("-----PSD-----");
  printArray(psd);
  println("-----SPP-----");
  printArray(spp);
  println("-----TIF-----");
  printArray(tif);

  for (String t : tif) {
    if (psd.contains(t)) {
      tif_psd.add(t);
    } else if (spp.contains(t)) {
      tif_spp.add(t);
    } else {
      tif_nosrc.add(t);
    }
  }
  printArray(tif_psd);
  printArray(tif_spp);
  printArray(tif_nosrc);
}
