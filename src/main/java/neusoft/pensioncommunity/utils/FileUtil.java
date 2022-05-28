package neusoft.pensioncommunity.utils;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    private FileUtil(){}

    /**
     *
     * @param fileName 要读取的文件名
     * @return json文件字符串
     */
    public static String readFile(String fileName) {
        try {
            URL url = FileUtil.class.getResource("/neusoft/pensioncommunity/data/" + fileName);
            return new String(Files.readAllBytes(Paths.get(url.toURI())), Charset.forName("utf-8"));
        } catch (IOException | URISyntaxException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("读取文件失败！");
            alert.showAndWait();
            System.exit(1);
            e.printStackTrace();
        }
        return null;
    }
    public static boolean writeFile(String fileName,String serialized){
        try {
            URL url = FileUtil.class.getResource("/neusoft/pensioncommunity/data/" + fileName);
            Files.writeString(Paths.get(url.toURI()), serialized);
            return true;
        } catch (IOException | URISyntaxException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("写文件失败，所作修改将不会被保存！");
            alert.showAndWait();
            System.exit(1);
            e.printStackTrace();
        }
        return false;
    }
}
