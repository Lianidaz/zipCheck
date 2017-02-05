import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.Date;
import gifAnimation.*;

ArrayList<File> allFiles;
ArrayList<String> tif_psd = new ArrayList<String>();
ArrayList<String> tif_spp = new ArrayList<String>();
ArrayList<String> tif_nosrc = new ArrayList<String>();

Gif loader;
PImage warn, error, ok, img, ok_button, boxC, boxU;

String pth, reply;

String[] tif_nopsd;
String[] tif_nossp;

boolean loading = true;
boolean clickable = false;

Timer tim;
Checkbox cbox;

PrintWriter outtxt;

void setup() {
  size(600,400);
  selectInput("Select a ZIP to check","chosenZip");
  loader = new Gif(this, "loader.gif");
  loader.play();
  warn = loadImage("warn.png");
  ok = loadImage("ok.png");
  error = loadImage("error.png");
  ok_button = loadImage("ok-button.png");
  boxC = loadImage("checkedbox.png");
  boxU = loadImage("emptybox.png");
  cbox = new Checkbox(width/2+30, height-40, 40, 40, "Я в курсе, там SPP");
  imageMode(CENTER);
}

void draw() {
  background(255);
  if (loading) {
    image(loader , width/2 , height/2);
  } else {
    icons();
  }
}

void chosenZip(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
    exit();
  } else {
    println(selection.getAbsolutePath());
    String name = selection.getName();

    pth = selection.getParent() + "\\" + name.substring(0, name.length()-4);
    UnzipUtility unzipper = new UnzipUtility();
    try{
      unzipper.unzip(selection.getAbsolutePath(),pth);
    } catch (Exception ex) {
      // some errors occurred
      ex.printStackTrace();
    }

    loading = false;

    allFiles = listFilesRecursive(pth);

    stringsMagic(allFiles);
    tim = new Timer(2000);
    File whi = new File(pth);
    while (!(allFiles.size()==0)){
      if (!(whi.exists())) break;
      for (File f : allFiles) {
        if(f.delete()){};
      }
      allFiles = listFilesRecursive(pth);
    }
    // for( File f: allFiles) {
    //   println(f.getName());
    // }
  }
}

void mousePressed() {
  println("click: " + mouseX + " " + mouseY);
  if (tim.done()) {
    if (mouseX > width/4-75 && mouseY > height-40-25 && mouseX < width/4+75 && mouseY < height-40+25) {
      if (cbox.check || tif_spp.size() == 0){
        exit();
      }
    }
    if (mouseX > cbox.x-cbox.w/2 && mouseY > cbox.y-cbox.h/2 && mouseX < cbox.x+cbox.w/2 && mouseY < cbox.y+cbox.h/2) {
      cbox.check = true;
    }
  }
}
