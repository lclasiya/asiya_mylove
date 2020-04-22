package li.changlin.common.utils;


import it.sauronsoftware.jave.Encoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class VideoAnalysis {
    static String vtime;
    public static long length;
    /*public static void main(String[] args){
        File source = new File("E:\\迅雷下载\\project\\x1hn1k\\asiya\\src\\main\\resources\\static\\uploadFiles\\video476e94cb-55af-4ce1-aeb0-af1d8008c336.mp4");
        Encoder encoder = new Encoder();
        FileChannel fc= null;
        String size = "";
        try {
            it.sauronsoftware.jave.MultimediaInfo m = encoder.getInfo(source);
            long ls = m.getDuration();
            if ((ls / 60000) < 10) {
                vtime = "0" + ls / 60000 + ":" + ((ls / 1000) - ((ls / 60000) * 60));

            } else if ((ls / 60000) < 10 && ((ls / 1000) - ((ls / 60000) * 60)) < 10) {
                vtime = "0" + ls / 60000 + ":" + "0" + ((ls / 1000) - ((ls / 60000) * 60));

            } else if ((ls / 60000) > 9 && ((ls / 1000) - ((ls / 60000) * 60)) < 10) {
                vtime = ls / 60000 + ":" + "0" + ((ls / 1000) - ((ls / 60000) * 60));

            } else {
                vtime = ls / 60000 + ":" + ((ls / 1000) - ((ls / 60000) * 60));
            }
            System.out.println(vtime);
            //视频帧宽高
            System.out.println("此视频高度为:"+m.getVideo().getSize().getHeight());
            /*FileInputStream fis = new FileInputStream(source);
            fc= fis.getChannel();
            BigDecimal fileSize = new BigDecimal(fc.size());
            size = fileSize.divide(new BigDecimal(1048576), 2, RoundingMode.HALF_UP) + "MB";
            System.out.println("此视频大小为"+size);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null!=fc){
                try {
                    fc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
*/
    /**
     * @param path
     * @return Map
     */
    public static Map<String, Object> getVideoMsg(File file){

        Map<String, Object> map = new HashMap<>();
        Encoder encoder = new Encoder();

        if(file != null) {
            try {
                it.sauronsoftware.jave.MultimediaInfo m = encoder.getInfo(file);
                long ls = m.getDuration();
                length = ls/1000;
                if ((ls / 60000) < 10 && ((ls / 1000) - ((ls / 60000) * 60)) > 9) {
                    vtime = "0" + ls / 60000 + ":" + ((ls / 1000) - ((ls / 60000) * 60));

                } else if ((ls / 60000) < 10 && ((ls / 1000) - ((ls / 60000) * 60)) < 10) {
                    vtime = "0" + ls / 60000 + ":" + "0" + ((ls / 1000) - ((ls / 60000) * 60));

                } else if ((ls / 60000) > 9 && ((ls / 1000) - ((ls / 60000) * 60)) < 10) {
                    vtime = ls / 60000 + ":" + "0" + ((ls / 1000) - ((ls / 60000) * 60));

                } else {
                    vtime = ls / 60000 + ":" + ((ls / 1000) - ((ls / 60000) * 60));
                }
                map.put("videoLength", vtime);
                map.put("videoQua", m.getVideo().getSize().getHeight());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }}
