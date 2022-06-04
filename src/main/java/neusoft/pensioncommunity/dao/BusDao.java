package neusoft.pensioncommunity.dao;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lombok.Getter;
import neusoft.pensioncommunity.model.Bus;

import neusoft.pensioncommunity.utils.FileUtil;
import neusoft.pensioncommunity.utils.GsonUtil;

public class BusDao extends AbstractDao<Bus>{
    @Getter
    private static final BusDao instance = new BusDao();

    @Override
    public void load() {
        try{
            String buffer = FileUtil.readFile("Bus.json");
            this.daoList.addAll(GsonUtil.getGson().fromJson(
                            buffer,Bus[].class
                    )
            );
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("数据文件读取错误，程序即将关闭！");
            alert.show();
            Platform.exit();
        }

    }

    @Override
    public void save(){
        String serialized = GsonUtil.getGson().toJson(daoList,ObservableList.class);
        FileUtil.writeFile("Bus.json",serialized);
    }


    private BusDao(){load();}
}
