package mp3tagger;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;
import java.io.File;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.List;

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
      if(!parsed)parseParams(args);
      
      MP3tagger mP3tagger = new MP3tagger();
      
      switch(command){          
          case COMMAND_TAG:
              mP3tagger.iterate(file);
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
    ID3v2 id3v2Tag;
    private void iterate(File dir) {
        
        
        for(File f : dir.listFiles()){ //parent directory
            if(f.isDirectory()){                           //children's directories
                iterate(f);
            }else if(f.toString().endsWith(".mp3")){
                try {
                    String path = f.getAbsolutePath();
                    mp3File = new Mp3File(path);                                       
                    if (mp3File.hasId3v2Tag()) {
                        id3v2Tag = mp3File.getId3v2Tag();
                    } else {
                    // mp3 does not have an ID3v2 tag, let's create one..
                        id3v2Tag = new ID3v24Tag();
                        mp3File.setId3v2Tag(id3v2Tag);
                    }
                    String[] paths = f.getAbsolutePath().split("\\\\");
                    if(mode.contains(MODE_ARTIST))id3v2Tag.setArtist(paths[paths.length-3]);
                    if(mode.contains(MODE_ALBUM))id3v2Tag.setAlbum(paths[paths.length-2]);
                    if(mode.contains(MODE_TITLE))id3v2Tag.setTitle(paths[paths.length-1].split(".mp3")[0]);
                    mp3File.save(path + ".new");
                    f.delete();
                    f = new File(path + ".new");
                    f.renameTo(new File(path));
                    System.out.println(f);

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
