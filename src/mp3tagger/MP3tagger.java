package mp3tagger;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MP3tagger {

    private static File file;
    private static List<Integer> mode;
    private static int command;
    
    private static boolean parsed = false;
    
    public static final Integer MODE_TITLE = 0;
    public static final Integer MODE_ARTIST = 1;
    public static final Integer MODE_ALBUM = 2;
    
    public static final int COMMAND_TAG = 0;
    public static final int COMMAND_REMOVE = 1;
    
    public static void main(String[] args) {
      if(parsed)parseParams(args);
      
      MP3tagger mP3tagger = new MP3tagger();
      
      switch(command){          
          case COMMAND_TAG:
              ArrayList<File> dirs = new ArrayList<>();
              dirs.add(file);
              mP3tagger.iterate(dirs);
              break;
          case COMMAND_REMOVE:
              mP3tagger.rm();
      }
             
    }

    private static void parseParams(String[] args) {
       parsed = true;
       mode = new ArrayList<>();
        
       if(args.length >= 1){
           switch(args[0]){
               case "tag":
                   command = COMMAND_TAG;
                   break;
               case "remove":
                   command = COMMAND_REMOVE;
                   break;
               default:
                    System.out.println("Invalid command: " + args[0]);
           }
       }else{
           System.out.println("Please specify command.");
           exit(0);
       }             
        
       if(args.length >= 2 && (file = new File(args[1])).isDirectory()){
           System.out.println("Using directory: " + file.getAbsolutePath());
       }else{
           System.out.println("Using current directory (Directory not specified or does not exist.");
           file = new File(".");
       }
       
       if(args.length >= 3){
           for(char c : args[2].toCharArray()){
               switch(c){
                   case 't':
                       mode.add(MODE_TITLE);
                       break;
                   case 'a':
                       mode.add(MODE_ARTIST);
                       break;
                   case 'l':
                       mode.add(MODE_ALBUM);
                       break;
                   default:
                       System.out.println("Invalid option: " + c);
               }
           }
       }else{
           mode.add(MODE_TITLE);
           mode.add(MODE_ARTIST);
           mode.add(MODE_ALBUM);
       }              
    }

    Mp3File mp3File;
    ID3v1 id3v1Tag;
    private void iterate(ArrayList<File> dirs) {
        
        
        for(File f : dirs.get(dirs.size()-1).listFiles()){ //parent directory
            if(f.isDirectory()){                           //children's directories
                dirs.add(f);
                iterate(dirs);
            }else if(f.toString().endsWith(".mp3")){
                try {
                    mp3File = new Mp3File(f.getAbsolutePath());                                       
                    if (mp3File.hasId3v1Tag()) {
                        id3v1Tag =  mp3File.getId3v1Tag();
                    } else {
                        id3v1Tag = new ID3v1Tag();
                        mp3File.setId3v1Tag(id3v1Tag);
                    }
                    if(mode.contains(MODE_ARTIST) && dirs.size() >= 2)id3v1Tag.setArtist(dirs.get(1).getName());
                    if(mode.contains(MODE_ALBUM) && dirs.size() >= 3)id3v1Tag.setAlbum(dirs.get(2).getName());
                    if(mode.contains(MODE_TITLE) && dirs.size() >= 4)id3v1Tag.setArtist(dirs.get(3).getName().split(".")[0]);
                    mp3File.save(f.getAbsolutePath());

                } catch (Exception ex) {
                    ex.printStackTrace();
                } 
            }
        }
    }

    private static void rm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
