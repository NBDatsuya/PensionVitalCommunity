package neusoft.pensioncommunity.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import neusoft.pensioncommunity.GlobalConfig;
import neusoft.pensioncommunity.dao.UserDao;
import neusoft.pensioncommunity.model.Senior;
import neusoft.pensioncommunity.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserService implements Service<User>{
    @Getter
    private static final UserService instance = new UserService();

    private UserService(){}
    private static final UserDao dao = UserDao.getInstance();

    public User verifyUser(String name, String pwd){
        for(User item : dao.getAll())
            if (item.getName().equals(name) && item.getPassword().equals(pwd))
                return item;

        return null;
    }

    public User searchById(int id, boolean admin) {
        User user = dao.search(id);
        return ((user==null || admin)?null:user);
    }
    public User searchById(int id) {
        return dao.search(id);
    }
    public ObservableList<User> searchByRole(int role){
        ObservableList<User> list = FXCollections.observableArrayList();
        for(User item : dao.getAll()){
            if(item.getRole()==0) continue;
            if((item.getRole()==role))
                list.add(item);
        }
        return list;
    }
    public ObservableList<User> searchNotByRole(int role){
        ObservableList<User> list = FXCollections.observableArrayList();
        for(User item : dao.getAll()){
            if(item.getRole()==role) continue;
            list.add(item);
        }

        return list;
    }
    public ObservableList<User> searchByName(String name){
        ObservableList<User> list = FXCollections.observableArrayList();
        for(User item : dao.getAll()){
            if(item.getRole()==0) continue;
            if((item.getName().contains(name)))
                list.add(item);
        }


        return list;
    }
    public ObservableList<User> searchByRealName(String realName){
        ObservableList<User> list = FXCollections.observableArrayList();
        for(User item : dao.getAll()){
            if(item.getRole()==0) continue;
            if((item.getRealName().contains(realName)))
                list.add(item);
        }

        return list;
    }
    public ObservableList<User> searchByTel(String tel){
        ObservableList<User> list = FXCollections.observableArrayList();
        for(User item : dao.getAll()){
            if(item.getRole()==0) continue;
            if((item.getTel().contains(tel)))
                list.add(item);
        }

        return list;
    }
    public ObservableList<User> searchByGender(int gender) {
        ObservableList<User> list = FXCollections.observableArrayList();
        for(User item : dao.getAll()){
            if(item.getRole()==0) continue;
            if((item.getGender() == gender))
                list.add(item);
        }


        return list;
    }
    public ObservableList<User> searchByBirthday(LocalDate birthday) {
        ObservableList<User> list = FXCollections.observableArrayList();
        for(User item : dao.getAll())
            if((item.getBirthDay().equals(birthday)))
                list.add(item);

        return list;
    }

    @Override
    public int getNewId() {
        return dao.getNewId();
    }

    @Override
    public void add(User item) {
        dao.add(item);
    }

    @Override
    public boolean remove(int id){
        List<Senior> list =
                new ArrayList<>(
                        GlobalConfig.seniorService.searchByAssitant(id)
                );

        if (list.size()==0){
            dao.remove(id);
            return true;
        }
        else{
            return false;
        }

    }

    @Override
    public void modify(int id, User item) {
        dao.modify(id,item);
    }

    @Override
    public ObservableList<User> getAll() {
        return FXCollections.observableArrayList(dao.getAll());
    }

    @Override
    public void save() {
        dao.save();
    }

    @Override
    public int size() {
        return dao.size();
    }

    public boolean verifyName(String name,User model){
        if(name.isEmpty()) return false;

        ObservableList<User> list = FXCollections.observableArrayList();
        for(User item : dao.getAll()){
            if((item.getName().equals(name)))
                if(model==null || model.getId()!=item.getId())
                    list.add(item);
        }
        return list.size()==0;
    }

    public boolean verifyPassword(String pwd){
        return pwd.length()>=8;
    }
}
