package neusoft.pensioncommunity.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import neusoft.pensioncommunity.model.Model;

public abstract class AbstractDao<T extends Model> implements Dao<T>{
    protected ObservableList<T> daoList = FXCollections.observableArrayList();
    
    public void add(T item) {
        daoList.add(item);
    }

    @Override
    public void remove(int id) {
        daoList.remove(search(id));

        for(int i = id-1;i<size();i++)
            daoList.get(i).setId(i+1);
    }

    @Override
    public T search(int id) {
        for(T item : daoList)
            if (item.getId() == id)
                return item;

        return null;
    }

    @Override
    public void modify(int id, T item) {
        daoList.set(daoList.indexOf(search(id)),item);
    }

    @Override
    public ObservableList<T> getAll() {
        return daoList;
    }

    @Override
    public int size() {
        return daoList.size();
    }

    public int getNewId() {
        if(daoList==null || daoList.size()==0) return 1;
        else return daoList.get(daoList.size()-1).getId()+1;
    }
}
