package neusoft.pensioncommunity.dao;

import javafx.collections.ObservableList;
import lombok.Getter;
import neusoft.pensioncommunity.model.Reserve;
import neusoft.pensioncommunity.utils.FileUtil;
import neusoft.pensioncommunity.utils.GsonUtil;

public class ReserveDao extends AbstractDao<Reserve>{
    @Getter
    private static final ReserveDao instance = new ReserveDao();

    @Override
    public void load() {
        String buffer = FileUtil.readFile("Reserve.json");
        this.daoList.addAll(GsonUtil.getGson().fromJson(
                        buffer, Reserve[].class
                )
        );
    }

    @Override
    public void save(){
        String serialized = GsonUtil.getGson().toJson(daoList,ObservableList.class);
        FileUtil.writeFile("Reserve.json",serialized);
    }

    private ReserveDao(){load();}

}
