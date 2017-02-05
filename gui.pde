void icons() {
  ArrayList<String> msg = new ArrayList<String>();
  if (tif_nosrc.size() > 0) {
    msg.add("Нет исходников для:");
    img = error;
    for (String t : tif_nosrc) {
      msg.add(t);
    }
  } else if (tif_spp.size() > 0) {
    msg.add("Hет .PSD для:");
    img = warn;
    for (String t : tif_spp) {
      msg.add(t);
    }
    cbox.show();
  } else {
    msg.add("Все норм, молодец!");
    img = ok;
  }
  image(img,width/4,height/3,200,200);
  fill(0);
  textSize(18);
  for (int i=0 ; i < msg.size() ; i++) {
    text(msg.get(i) ,width/2+30,height/4+i*30);
  }
  if (tim.done()) {
    tint(255);
  } else {
    tint(66);
  }
  // delay(5000);
  image(ok_button,width/4,height-40, 150, 50);
  tint(255);
}

class Timer {
  int startms;
  int sleep;
  Timer(int _sleep) {
    startms = millis();
    sleep = _sleep;
  }
  boolean done() {
    if (millis() - startms >= sleep) return true;
    else return false;
  }
}

class Checkbox{
  boolean check = false;
  int x, y, w, h;
  String label;
  Checkbox (int _x, int _y, int _w, int _h, String _label) {
    x= _x; y=_y; w= _w; h= _h; label= _label;
  }
  void show() {
    if (check) image(boxC,x,y,w,h);
    else image(boxU,x,y,w,h);
    text(label,x+w, y);
  }
}
