package neusoft.pensioncommunity.service;

import javafx.collections.ObservableList;
import neusoft.pensioncommunity.model.Model;

import java.util.List;

public interface Service<T extends Model>{
    public void add(T item);
    public boolean remove(int id);
    public void modify(int id,T item);
    public ObservableList<T> getAll();
    public void save();
    public int size();
    public int getNewId();
}
