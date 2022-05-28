package neusoft.pensioncommunity.dao;

import javafx.collections.ObservableList;
import neusoft.pensioncommunity.model.Model;

import java.util.List;

public interface Dao<T extends Model> {

    public void add(T item);
    public void remove(int id);
    public T search(int id);
    public void modify(int id,T item);
    public ObservableList<T> getAll();
    public void load();
    public void save() throws Exception;
    public int size();
}
