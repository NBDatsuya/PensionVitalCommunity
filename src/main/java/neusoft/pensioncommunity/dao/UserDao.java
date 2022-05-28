package neusoft.pensioncommunity.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import neusoft.pensioncommunity.model.*;
import neusoft.pensioncommunity.utils.FileUtil;
import neusoft.pensioncommunity.utils.GsonUtil;

import java.util.List;

public class UserDao implements Dao<User> {

    @Getter
    private static final UserDao instance = new UserDao();
    private final ObservableList<User> daoList = FXCollections.observableArrayList();
    @Override
    public void add(User item) {
        daoList.add(item);
    }

    @Override
    public void remove(int id) {
        daoList.remove(search(id));
    }

    @Override
    public User search(int id) {
        for(User item : daoList)
            if (item.getId() == id)
                return item;

        return null;
    }

    @Override
    public void modify(int id, User item) {
        daoList.set(daoList.indexOf(search(id)),item);
    }

    @Override
    public ObservableList<User> getAll() {
        return daoList;
    }

    @Override
    public void load() {
        String buffer = FileUtil.readFile("User.json");
        this.daoList.addAll(GsonUtil.getGson().fromJson(
                    buffer,User[].class
                )
        );
    }

    @Override
    public void save() {
        String serialized = GsonUtil.getGson().toJson(daoList,ObservableList.class);
        FileUtil.writeFile("User.json",serialized);
    }

    @Override
    public int size() {
        return daoList.size();
    }

    //searchByDate?

    private UserDao(){
        load();
    }

}
