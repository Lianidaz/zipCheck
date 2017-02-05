import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.io.BufferedOutputStream; 
import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.io.IOException; 
import java.util.zip.ZipEntry; 
import java.util.zip.ZipInputStream; 
import java.util.Date; 
import gifAnimation.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class zipCheck extends PApplet {











ArrayList<File> allFiles;
ArrayList<String> tif_psd = new ArrayList<String>();
ArrayList<String> tif_spp = new ArrayList<String>();
ArrayList<String> tif_nosrc = new ArrayList<String>();

Gif loader;
PImage warn, error, ok, img, ok_button, boxC, boxU;

String pth, reply;

String[] tif_nopsd;
String[] tif_nossp;
String parent;

boolean loading = true;
boolean clickable = false;

Timer tim;
Checkbox cbox;

File outtxt;
PrintWriter outtext;

public void setup() {
  
  selectInput("Select a ZIP to check","chosenZip");
  loader = new Gif(this, "loader.gif");
  loader.play();
  warn = loadImage("warn.png");
  ok = loadImage("ok.png");
  error = loadImage("error.png");
  ok_button = loadImage("ok-button.png");
  boxC = loadImage("checkedbox.png");
  boxU = loadImage("emptybox.png");
  cbox = new Checkbox(width/2+30, height-40, 40, 40, "\u042f \u0432 \u043a\u0443\u0440\u0441\u0435, \u0442\u0430\u043c SPP");
  imageMode(CENTER);
}

public void draw() {
  background(255);
  if (loading) {
    image(loader , width/2 , height/2);
  } else {
    icons();
  }
}

public void chosenZip(File selection) {
  parent = selection.getParent();
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
    exit();
  } else {
    File[] contF = listFiles(parent);
    for (int i=0 ; i < contF.length ; i++) {
      switch (contF[i].getName()) {
        case "OK.txt" :
          contF[i].delete();
        case "ERROR.txt" :
          contF[i].delete();
        case "WARN.txt" :
          contF[i].delete();
      }
    }

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

public void mousePressed() {
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
public void icons() {
  ArrayList<String> msg = new ArrayList<String>();
  if (tif_nosrc.size() > 0) {
    msg.add("\u041d\u0435\u0442 \u0438\u0441\u0445\u043e\u0434\u043d\u0438\u043a\u043e\u0432 \u0434\u043b\u044f:");
    img = error;
    for (String t : tif_nosrc) {
      msg.add(t);
    }
  } else if (tif_spp.size() > 0) {
    msg.add("H\u0435\u0442 .PSD \u0434\u043b\u044f:");
    img = warn;
    for (String t : tif_spp) {
      msg.add(t);
    }
    cbox.show();
  } else {
    msg.add("\u0412\u0441\u0435 \u043d\u043e\u0440\u043c, \u043c\u043e\u043b\u043e\u0434\u0435\u0446!");
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
  public boolean done() {
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
  public void show() {
    if (check) image(boxC,x,y,w,h);
    else image(boxU,x,y,w,h);
    text(label,x+w, y);
  }
}

// This function returns all the files in a directory as an array of Strings
public String[] listFileNames(String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    String names[] = file.list();
    return names;
  } else {
    // If it's not a directory
    return null;
  }
}

// This function returns all the files in a directory as an array of File objects
// This is useful if you want more info about the file
public File[] listFiles(String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    File[] files = file.listFiles();
    return files;
  } else {
    // If it's not a directory
    return null;
  }
}

// Function to get a list of all files in a directory and all subdirectories
public ArrayList<File> listFilesRecursive(String dir) {
  ArrayList<File> fileList = new ArrayList<File>();
  recurseDir(fileList, dir);
  return fileList;
}

// Recursive function to traverse subdirectories
public void recurseDir(ArrayList<File> a, String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    // If you want to include directories in the list
    a.add(file);
    File[] subfiles = file.listFiles();
    for (int i = 0; i < subfiles.length; i++) {
      // Call this function on all files in this directory
      recurseDir(a, subfiles[i].getAbsolutePath());
    }
  } else {
    a.add(file);
  }
}
String[] exts = {"psd","spp","tif"};

String isPSD = "psd";
String isspp = "spp";
String isTIF = "tif";

public void stringsMagic(ArrayList<File> list) {
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

  if (tif_nosrc.size() > 0) {
    outtext = createWriter(parent + "\\ERROR.txt");
  } else if (tif_spp.size() > 0) {
    outtext = createWriter(parent + "\\WARNINIG.txt");
  } else {
    outtext = createWriter(parent + "\\OK.txt");
  }
  outtext.println("-----PSD-----");
  for (String s : psd) outtext.println(s);
  outtext.println("-----SPP-----");
  for (String s : spp) outtext.println(s);
  outtext.println("-----TIF-----");
  for (String s : tif) outtext.println(s);
  outtext.flush();
  outtext.close();
}
/**
 * This utility extracts files and directories of a standard zip file to
 * a destination directory.
 * @author www.codejava.net
 *
 */
public class UnzipUtility {
    /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;
    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
  public void settings() {  size(600,400); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "zipCheck" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
