package neusoft.pensioncommunity.dao;

import javafx.collections.ObservableList;
import lombok.Getter;
import neusoft.pensioncommunity.model.Senior;
import neusoft.pensioncommunity.utils.FileUtil;
import neusoft.pensioncommunity.utils.GsonUtil;

public class SeniorDao extends AbstractDao<Senior> {
    @Getter
    private static final SeniorDao instance = new SeniorDao();

    @Override
    public void load() {
        String buffer = FileUtil.readFile("Senior.json");
        this.daoList.addAll(GsonUtil.getGson().fromJson(
                        buffer,Senior[].class
                )
        );
    }

    @Override
    public void save(){
        String serialized = GsonUtil.getGson().toJson(daoList,ObservableList.class);
        FileUtil.writeFile("Senior.json",serialized);
    }

    private SeniorDao(){load();}
}
