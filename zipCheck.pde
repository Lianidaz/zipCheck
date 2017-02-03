import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.Date;

String pth, reply;

String[] tif_nopsd;
String[] tif_nossp;

void setup() {
  selectInput("Select an archive to check","chosenZip");

}

void draw() {

}

void chosenZip(File selection) {
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

  for( File f: allFiles) {
    println(f.getName());
  }
}
