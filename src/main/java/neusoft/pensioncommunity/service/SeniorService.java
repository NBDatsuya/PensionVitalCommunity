package neusoft.pensioncommunity.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import neusoft.pensioncommunity.dao.SeniorDao;
import neusoft.pensioncommunity.model.Senior;
import neusoft.pensioncommunity.model.User;

import java.util.List;

public class SeniorService implements Service<Senior>{
    @Getter
    private static final SeniorService instance = new SeniorService();

    private SeniorService(){
    }
    private static final SeniorDao dao = SeniorDao.getInstance();
    @Getter
    private static final ObservableList<Senior> serviceList = FXCollections.observableArrayList();

    @Override
    public void add(Senior item) {
        dao.add(item);
    }

    @Override
    public boolean remove(int id) {
        dao.remove(id);
        return true;
    }

    @Override
    public void modify(int id, Senior item) {
        dao.modify(id,item);
    }

    @Override
    public ObservableList<Senior> getAll() {
        setStatus(1);
        return serviceList;
    }


    @Override
    public void save() {
        dao.save();
    }

    @Override
    public int size() {
        return dao.size();
    }

    public ObservableList<Senior> getAll(int status) {
        setStatus(status);
        return serviceList;
    }

    public ObservableList<Senior> searchByAssitant(int id) {
        ObservableList<Senior> list = FXCollections.observableArrayList();
        for (Senior item : dao.getAll())
            if (item.getAssistantId() == id && item.getStatus()!=0)
                list.add(item);
        return list;
    }
    public ObservableList<Senior> searchByAssitant(int id, int status) {
        ObservableList<Senior> list = FXCollections.observableArrayList();
        for (Senior item : dao.getAll())
            if (item.getAssistantId() == id && item.getStatus()==status)
                list.add(item);
        return list;
    }

    public ObservableList<Senior> searchByName(String name){
        ObservableList<Senior> list = FXCollections.observableArrayList();
        for (Senior item : serviceList){
            if (item.getName().trim().contains(name))
                list.add(item);
        }
        return list;
    }

    public ObservableList<Senior> searchByGender(int gender) {
        ObservableList<Senior> list = FXCollections.observableArrayList();
        for (Senior item : serviceList)
            if (item.getGender()==gender)
                list.add(item);
        return list;
    }

    /**
     * @param operator 0 = noLessThan, 1 = noMoreThan
     */
    public ObservableList<Senior> searchByAge(int operator, int age) {
        ObservableList<Senior> list = FXCollections.observableArrayList();
        for (Senior item : serviceList)
            if (operator==0?item.getAge()>=age:item.getAge()<=age)
                list.add(item);
        return list;
    }

    public ObservableList<Senior> searchByIN(String in) {
        ObservableList<Senior> list = FXCollections.observableArrayList();
        for (Senior item : serviceList)
            if (item.getIdentity().trim().contains(in))
                list.add(item);
        return list;
    }

    public ObservableList<Senior> searchByTelSelf(String tel) {
        ObservableList<Senior> list = FXCollections.observableArrayList();
        for (Senior item : serviceList){
            if (item.getIdentity().trim().contains(tel))
                list.add(item);
        }
        return list;
    }

    public ObservableList<Senior> searchByTelRelative(String tel) {
        ObservableList<Senior> list = FXCollections.observableArrayList();
        for (Senior item : serviceList){
            if (item.getIdentity().contains(tel))
                list.add(item);
        }
        return list;
    }

    public void setStatus(int status){
        if (serviceList!=null) serviceList.clear();
        for(Senior item : dao.getAll())
            if(item.getStatus()==status)
                serviceList.add(item);
    }

    @Override
    public int getNewId() {
        return dao.getNewId();
    }

}
