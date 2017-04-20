package mp3tagger;

import java.io.File;
import java.util.List;

public class MP3tagger {

    private static String filepath;
    private static List<Integer> mode;
    
    public static final Integer MODE_TITLE = 0;
    public static final Integer MODE_ARTIST = 1;
    public static final Integer MODE_ALBUM = 2;
    
    public static void main(String[] args) {
       if(args[0] != null){
           
       }else{
           
       }
       
       File file;
        
       if(args[1] != null && (file = new File(args[1])).isDirectory()){
           filepath = args[0];
       }else if(args[1] != null){
           System.out.println("Using current directory");
           filepath = ".";
       }
       
       if(args[2] != null){
           for(char c : args[1].toCharArray()){
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
