package neusoft.pensioncommunity.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import neusoft.pensioncommunity.model.*;

import java.util.ArrayList;
import java.util.List;

public class UserDao implements Dao<User> {

    private ObservableList<User> daoList = FXCollections.observableArrayList();
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
            if (item.getID() == id)
                return item;

        return null;
    }

    @Override
    public void modify(int id, User item) {
        daoList.set(daoList.indexOf(search(id)),item);
    }

    @Override
    public List<User> getAll() {
        return daoList;
    }

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }

    public UserDao(){
        load();
    }
    public UserDao(ObservableList<User> subList){
        this.daoList = subList;
    }
}
