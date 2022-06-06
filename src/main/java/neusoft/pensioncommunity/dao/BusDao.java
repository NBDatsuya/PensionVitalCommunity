package neusoft.pensioncommunity.dao;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lombok.Getter;
import neusoft.pensioncommunity.GlobalConfig;
import neusoft.pensioncommunity.model.Bus;

import neusoft.pensioncommunity.model.Reserve;
import neusoft.pensioncommunity.model.Senior;
import neusoft.pensioncommunity.utils.FileUtil;
import neusoft.pensioncommunity.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void remove(int id){
        daoList.remove(search(id));

        for(int i = id-1;i<size();i++){
            daoList.get(i).setId(i+1);

            List<Reserve> list
                    = new ArrayList<>(
                    GlobalConfig.reserveSevice.searchByBus(i+2,GlobalConfig.reserveSevice.getAll()));
            if(list.size()==0) continue;
            for (Reserve item : list)
                item.setBusId(i);

            for (Reserve item : list)
                GlobalConfig.reserveSevice.modify(item.getId(), item);
        }

    }


    private BusDao(){load();}
}
