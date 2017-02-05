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

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class zipCheck extends PApplet {










String pth, reply;

String[] tif_nopsd;
String[] tif_nossp;

public void setup() {
  selectInput("Select an archive to check","chosenZip");

}

public void draw() {

}

public void chosenZip(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    println("User selected " + selection.getAbsolutePath());
    String name = selection.getName();

    pth = selection.getParent() + "\\" + name.substring(0, name.length()-4);
    println(pth);
    UnzipUtility unzipper = new UnzipUtility();
    try{
      unzipper.unzip(selection.getAbsolutePath(),pth);
    } catch (Exception ex) {
      // some errors occurred
      ex.printStackTrace();
    }
  }

  ArrayList<File> allFiles = listFilesRecursive(pth);

  stringsMagic(allFiles);

  // for( File f: allFiles) {
  //   println(f.getName());
  // }
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
    // a.add(file);
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

  ArrayList<String> tif_psd = new ArrayList<String>();
  ArrayList<String> tif_spp = new ArrayList<String>();
  ArrayList<String> tif_nosrc = new ArrayList<String>();

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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "zipCheck" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
