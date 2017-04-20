package mp3tagger;

import java.io.File;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.List;

public class MP3tagger {

    private static File file;
    private static List<Integer> mode;
    private static int command;
    
    public static final Integer MODE_TITLE = 0;
    public static final Integer MODE_ARTIST = 1;
    public static final Integer MODE_ALBUM = 2;
    
    public static final int COMMAND_TAG = 0;
    public static final int COMMAND_REMOVE = 1;
    
    public static void main(String[] args) {
      parseParams(args);
      
             
    }

    private static void parseParams(String[] args) {
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
    
}
