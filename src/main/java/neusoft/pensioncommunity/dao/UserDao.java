package neusoft.pensioncommunity.dao;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lombok.Getter;
import neusoft.pensioncommunity.GlobalConfig;
import neusoft.pensioncommunity.model.*;
import neusoft.pensioncommunity.utils.FileUtil;
import neusoft.pensioncommunity.utils.GsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends AbstractDao<User> {

    @Getter
    private static final UserDao instance = new UserDao();

    @Override
    public void load() {
        try{
            String buffer = FileUtil.readFile("User.json");
            this.daoList.addAll(GsonUtil.getGson().fromJson(
                            buffer,User[].class
                    )
            );
        }catch (Exception e){

        }
    }

    @Override
    public void save() {
        String serialized = GsonUtil.getGson().toJson(daoList,ObservableList.class);
        FileUtil.writeFile("User.json",serialized);
    }

    private UserDao(){
        load();
    }

    @Override
    public void remove(int id) {
        daoList.remove(search(id));
        for(int i = id;i<size();i++){
            daoList.get(i).setId(i);
            //修改关联数据
            switch (daoList.get(i).getRole()) {
                case 1:
                    List<Senior> list1
                            = new ArrayList<>(
                            GlobalConfig.seniorService.searchByAssitant(i + 1));
                    if(list1.size()==0) continue;
                    for (Senior item : list1)
                        item.setAssistantId(i);

                    for (Senior item : list1)
                        GlobalConfig.seniorService.modify(item.getId(), item);

                    break;

                case 2:
                    List<Reserve> list2
                            = new ArrayList<>(
                            GlobalConfig.reserveSevice.searchByLogistic(i + 1));
                    if(list2.size()==0) continue;
                    for (Reserve item : list2)
                        item.setLogisticId(i);

                    for (Reserve item : list2)
                        GlobalConfig.reserveSevice.modify(item.getId(), item);
            }
        }
    }
}
